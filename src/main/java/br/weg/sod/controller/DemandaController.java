package br.weg.sod.controller;

import br.weg.sod.dto.DemandaCriacaoDTO;
import br.weg.sod.dto.DemandaEdicaoDTO;
import br.weg.sod.model.entities.*;
import br.weg.sod.model.entities.enuns.StatusDemanda;
import br.weg.sod.model.entities.enuns.StatusHistorico;
import br.weg.sod.model.entities.enuns.Tarefa;
import br.weg.sod.model.service.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    private ArquivoDemandaService arquivoDemandaService;

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

    @Transactional
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid DemandaCriacaoDTO demandaCriacaoDTO) {
        Demanda demanda = new Demanda();
        BeanUtils.copyProperties(demandaCriacaoDTO, demanda);
        demanda.setStatusDemanda(StatusDemanda.BACKLOG);

        Demanda demandaCadastrada = demandaService.save(demanda);



        HistoricoWorkflow historicoWorkflow = new HistoricoWorkflow(Tarefa.AVALIARDEMANDA, StatusHistorico.EMAGUARDO, demandaCadastrada);
        historicoWorkflowService.save(historicoWorkflow);

        return ResponseEntity.status(HttpStatus.OK).body(demandaCadastrada);
    }

    @PutMapping("/{idDemanda}/{idAnalista}")
    public ResponseEntity<Object> edit(@RequestBody @Valid DemandaEdicaoDTO demandaCriacaoDTO, @PathVariable(name = "idDemanda") Integer idDemanda, @PathVariable(name = "idAnalista") Integer idAnalista) {
        if (!demandaService.existsById(idDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma demanda com o ID informado");
        }

        Demanda demanda = demandaService.findById(idDemanda).get();
        BeanUtils.copyProperties(demandaCriacaoDTO, demanda);
        demanda.setIdDemanda(idDemanda);
        demanda.setStatusDemanda(StatusDemanda.BACKLOG);
        demanda.setPrazoElaboracao(new Time(demandaCriacaoDTO.getMiliSegundosPrazoElaboracao()));
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

        List<Beneficio> beneficiosDemanda = beneficioService.findByDemanda(demandaDeletada);

        for(Beneficio beneficio : beneficiosDemanda){
            beneficioService.deleteById(beneficio.getIdBeneficio());
        }

        List<HistoricoWorkflow> historicosDemanda = historicoWorkflowService.findByDemanda(demandaDeletada);

        for(HistoricoWorkflow historico : historicosDemanda){
            historicoWorkflowService.deleteById(historico.getIdHistoricoWorkflow());
        }

        List<ArquivoDemanda> arquivosDemanda = arquivoDemandaService.findByDemanda(demandaDeletada);

        for(ArquivoDemanda arquivoDemanda : arquivosDemanda){
            arquivoDemandaService.deleteById(arquivoDemanda.getIdArquivoDemanda());
        }

        demandaService.deleteById(idDemanda);
        return ResponseEntity.status(HttpStatus.OK).body("Demanda deletada com sucesso!");
    }
}
