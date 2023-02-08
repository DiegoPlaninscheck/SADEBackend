package br.weg.sod.controller;

import br.weg.sod.dto.DemandaCriacaoDTO;
import br.weg.sod.model.entities.*;
import br.weg.sod.model.entities.enuns.StatusDemanda;
import br.weg.sod.model.entities.enuns.StatusHistorico;
import br.weg.sod.model.entities.enuns.Tarefa;
import br.weg.sod.model.service.*;
import br.weg.sod.util.DemandaUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.Timestamp;
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
    private CentroCustoService centroCustoService;

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
    public ResponseEntity<Object> save(@RequestParam("demanda") @Valid String demandaJSON, @RequestParam(value = "files", required = false) MultipartFile[] multipartFiles, @RequestParam("pdfVersaoHistorico") MultipartFile versaoPDF) throws IOException {
        if(versaoPDF.isEmpty()){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("PDF da versão não informado");
        }

        DemandaUtil util = new DemandaUtil();
        DemandaCriacaoDTO demandaCriacaoDTO = util.convertJsontoDtoCriacao(demandaJSON);
        Demanda demanda = new Demanda();
        BeanUtils.copyProperties(demandaCriacaoDTO, demanda);

        ResponseEntity<Object> demandaValidada = validarDemanda(demanda);

        if(demandaValidada != null){
            return demandaValidada;
        }

        demanda.setStatusDemanda(StatusDemanda.BACKLOG);

        if(multipartFiles != null){
            for(MultipartFile multipartFile : multipartFiles){
                demanda.getArquivosDemanda().add(new ArquivoDemanda(multipartFile, demanda.getUsuario() ));
            }
        }

        Demanda demandaSalva = demandaService.save(demanda);
        Timestamp momento = new Timestamp(new Date().getTime());
        HistoricoWorkflow historicoWorkflowCriacao = new HistoricoWorkflow(Tarefa.CRIARDEMANDA, StatusHistorico.CONCLUIDO, new ArquivoHistoricoWorkflow(versaoPDF), momento, Tarefa.CRIARDEMANDA, demandaSalva);
        HistoricoWorkflow historicoWorkflowAvaliacao = new HistoricoWorkflow(Tarefa.AVALIARDEMANDA, StatusHistorico.EMAGUARDO, demandaSalva);
        historicoWorkflowService.save(historicoWorkflowCriacao);
        historicoWorkflowService.save(historicoWorkflowAvaliacao);

        return ResponseEntity.status(HttpStatus.OK).body(demandaSalva);
    }

    @PutMapping("/{idDemanda}/{idAnalista}")
    public ResponseEntity<Object> edit(@RequestParam("demanda") @Valid String demandaJSON, @RequestParam(value = "files", required = false) MultipartFile[] multipartFiles, @PathVariable(name = "idDemanda") Integer idDemanda, @PathVariable(name = "idAnalista") Integer idAnalista) throws IOException  {
        if (!demandaService.existsById(idDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma demanda com o ID informado");
        }

        if(!(usuarioService.findById(idAnalista).get() instanceof AnalistaTI)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ID de usuário informado inválido para essa ação");
        }

        DemandaUtil util = new DemandaUtil();
        Demanda demanda = util.convertJsonToModel(demandaJSON, 2);

        ResponseEntity<Object> demandaValidada = validarDemanda(demanda);

        if(demandaValidada != null){
            return demandaValidada;
        }

        demanda.setIdDemanda(idDemanda);

        if(multipartFiles != null){
            for(MultipartFile multipartFile : multipartFiles){
                demanda.getArquivosDemanda().add(new ArquivoDemanda(multipartFile, demanda.getUsuario() ));
            }
        }

        Demanda demandaSalva = demandaService.save(demanda);

//        if (demanda.getLinkJira() == null) {
//            //concluindo histórico da classificacao do analista de TI
//            historicoWorkflowService.finishHistoricoByDemanda(demanda, Tarefa.CLASSIFICAR);
//
//            Usuario solicitante = usuarioService.findById(demanda.getUsuario().getIdUsuario()).get();
//
//            //iniciando o histórico de avaliacao do gerente de negócio
//            GerenteNegocio gerenteNegocio = usuarioService.findGerenteByDepartamento(solicitante.getDepartamento());
//            historicoWorkflowService.initializeHistoricoByDemanda(new Timestamp(new Date().getTime()), Tarefa.AVALIARDEMANDA, StatusHistorico.EMANDAMENTO, gerenteNegocio, demandaSalva);
//
//        } else {
//            //conclui o histórico de adicionar informações
//            historicoWorkflowService.finishHistoricoByDemanda(demandaSalva, Tarefa.ADICIONARINFORMACOES);
//
//            //inicia o histórico de criar proposta
//            AnalistaTI analistaResponsavel = (AnalistaTI) usuarioService.findById(idAnalista).get();
//            historicoWorkflowService.initializeHistoricoByDemanda(new Timestamp(new Date().getTime()), Tarefa.CRIARPROPOSTA, StatusHistorico.EMANDAMENTO, analistaResponsavel, demandaSalva);
//        }

        return ResponseEntity.status(HttpStatus.OK).body(demandaSalva);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idDemanda) {
        if (!demandaService.existsById(idDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma demanda com o ID informado");
        }

        demandaService.deleteById(idDemanda);
        return ResponseEntity.status(HttpStatus.OK).body("Demanda deletada com sucesso!");
    }

    private ResponseEntity<Object> validarDemanda(Demanda demanda) {
        List<Beneficio> beneficiosDemanda = demanda.getBeneficiosDemanda();

        if(beneficiosDemanda != null && beneficiosDemanda.size() != 0){
            try{
                beneficioService.checarBeneficios(beneficiosDemanda);
            } catch (RuntimeException exception){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Um dos benefícios passados está com inconformidades em seus dados");
            }
        }

        if(!centroCustoService.validarCentrosCusto(demanda.getCentroCustoDemanda())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Um dos centros de custo é inválido");
        }

        if(!usuarioService.existsById(demanda.getUsuario().getIdUsuario())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID de usuário inválido");
        }

        return null;
    }
}
