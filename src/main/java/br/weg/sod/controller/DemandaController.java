package br.weg.sod.controller;

import br.weg.sod.dto.DemandaCriacaoDTO;
import br.weg.sod.dto.DemandaEdicaoDTO;
import br.weg.sod.model.entities.*;
import br.weg.sod.model.entities.enuns.StatusDemanda;
import br.weg.sod.model.entities.enuns.StatusHistorico;
import br.weg.sod.model.entities.enuns.Tarefa;
import br.weg.sod.model.service.*;
import br.weg.sod.util.DemandaUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import org.apache.coyote.Request;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin
@AllArgsConstructor
@Controller
@RequestMapping("/sod/demanda")
public class DemandaController {

    private DemandaService demandaService;
    private HistoricoWorkflowService historicoWorkflowService;
    private UsuarioService usuarioService;
    private BeneficioService beneficioService;

    @GetMapping
    public ResponseEntity<List<Demanda>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(demandaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "id") Integer idDemanda) {
        if (!demandaService.existsById(idDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma demanda com o ID informado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(demandaService.findById(idDemanda));
    }

    @GetMapping("/{id}/arquivos")
    public ResponseEntity<Object> findArquivosDemanda(@PathVariable(name = "id") Integer idDemanda){
        if (!demandaService.existsById(idDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma demanda com o ID informado");
        }

        return ResponseEntity.status(HttpStatus.OK).body(demandaService.findById(idDemanda).get().getArquivosDemanda());
    }

    @Transactional
    @PostMapping
    public ResponseEntity<Object> save(@RequestParam("demanda") @Valid String demandaJSON, @RequestParam("files") MultipartFile[] multipartFiles) throws IOException {
        DemandaUtil util = new DemandaUtil();

        Demanda demanda = util.convertJsonToCreationModel(demandaJSON);
        demanda.setStatusDemanda(StatusDemanda.BACKLOG);

        for(MultipartFile multipartFile : multipartFiles){
            demanda.getArquivosDemanda().add(new ArquivoDemanda(multipartFile, demanda.getUsuario() ));
        }

        Demanda demandaSalva = demandaService.save(demanda);

        HistoricoWorkflow historicoWorkflow = new HistoricoWorkflow(Tarefa.AVALIARDEMANDA, StatusHistorico.EMAGUARDO, demandaSalva);
        historicoWorkflowService.save(historicoWorkflow);

        return ResponseEntity.status(HttpStatus.OK).body(demandaSalva);
    }

    @PutMapping("/{idDemanda}/{idAnalista}")
    public ResponseEntity<Object> edit(@RequestParam("demanda") @Valid String demandaJSON, @RequestParam("files") MultipartFile[] multipartFiles, @PathVariable(name = "idDemanda") Integer idDemanda, @PathVariable(name = "idAnalista") Integer idAnalista) throws IOException  {
        if (!demandaService.existsById(idDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma demanda com o ID informado");
        }

        DemandaUtil util = new DemandaUtil();

        Demanda demanda = util.convertJsonToEditionModel(demandaJSON);
        Demanda demandaBanco = demandaService.findById(idDemanda).get();
        BeanUtils.copyProperties(demandaBanco, demanda);
        demanda.setIdDemanda(idDemanda);

        for(MultipartFile multipartFile : multipartFiles){
            demanda.getArquivosDemanda().add(new ArquivoDemanda(multipartFile, demanda.getUsuario() ));
        }

        Demanda demandaSalva = demandaService.save(demanda);

        if (demanda.getLinkJira() == null) {
            //concluindo histórico da classificacao do analista de TI
            historicoWorkflowService.finishHistoricoByDemanda(demanda, Tarefa.CLASSIFICAR);

            Usuario solicitante = usuarioService.findById(demanda.getUsuario().getIdUsuario()).get();

            //iniciando o histórico de avaliacao do gerente de negócio
            GerenteNegocio gerenteNegocio = usuarioService.findGerenteByDepartamento(solicitante.getDepartamento());
            historicoWorkflowService.initializeHistoricoByDemanda(new Timestamp(new Date().getTime()), Tarefa.AVALIARDEMANDA, StatusHistorico.EMANDAMENTO, gerenteNegocio, demandaSalva);

        } else {
            //conclui o histórico de adicionar informações
            historicoWorkflowService.finishHistoricoByDemanda(demandaSalva, Tarefa.ADICIONARINFORMACOES);

            //inicia o histórico de criar proposta
            AnalistaTI analistaResponsavel = (AnalistaTI) usuarioService.findById(idAnalista).get();
            historicoWorkflowService.initializeHistoricoByDemanda(new Timestamp(new Date().getTime()), Tarefa.CRIARPROPOSTA, StatusHistorico.EMANDAMENTO, analistaResponsavel, demandaSalva);

        }

        return ResponseEntity.status(HttpStatus.OK).body(demandaSalva);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idDemanda) {
        if (!demandaService.existsById(idDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma demanda com o ID informado");
        }

        Demanda demandaDeletada = demandaService.findById(idDemanda).get();

        demandaService.deleteById(idDemanda);
        return ResponseEntity.status(HttpStatus.OK).body("Demanda deletada com sucesso!");
    }
}
