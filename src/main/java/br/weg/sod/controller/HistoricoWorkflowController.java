package br.weg.sod.controller;

import br.weg.sod.dto.HistoricoWorkflowCriacaoDTO;
import br.weg.sod.dto.HistoricoWorkflowEdicaoDTO;
import br.weg.sod.model.entities.*;
import br.weg.sod.model.entities.enuns.StatusDemanda;
import br.weg.sod.model.entities.enuns.StatusHistorico;
import br.weg.sod.model.entities.enuns.Tarefa;
import br.weg.sod.model.service.DemandaService;
import br.weg.sod.model.service.HistoricoWorkflowService;
import br.weg.sod.model.service.PropostaService;
import br.weg.sod.model.service.UsuarioService;
import br.weg.sod.util.HistoricoWorkflowUtil;
import br.weg.sod.util.UtilFunctions;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@CrossOrigin
@AllArgsConstructor
@Controller
@RequestMapping("/sod/historicoWorkflow")
public class HistoricoWorkflowController {

    private HistoricoWorkflowService historicoWorkflowService;
    private UsuarioService usuarioService;
    private DemandaService demandaService;
    private PropostaService propostaService;

    @GetMapping
    public ResponseEntity<List<HistoricoWorkflow>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(historicoWorkflowService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "id") Integer idHistoricoWorkflow) {
        if (!historicoWorkflowService.existsById(idHistoricoWorkflow)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum historico workflow com o ID informado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(historicoWorkflowService.findById(idHistoricoWorkflow));
    }

    @GetMapping("/demanda/{id}")
    public ResponseEntity<Object> findByDemanda(@PathVariable(name = "id") Integer idDemanda) {
        if (!demandaService.existsById(idDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum historico workflow com o ID informado");
        }

        return ResponseEntity.status(HttpStatus.OK).body(historicoWorkflowService.findHistoricoWorkflowByDemanda(demandaService.findById(idDemanda).get()));
    }

    @GetMapping("/arquivo/{idDemanda}")
    public ResponseEntity<Object> findLastArquivoHistorico(@PathVariable(name = "idDemanda") Integer idDemanda) {
        if (!demandaService.existsById(idDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum historico workflow com o ID informado");
        }

        return ResponseEntity.status(HttpStatus.OK).body(historicoWorkflowService.findArquivoLastHistoricoByDemanda(demandaService.findById(idDemanda).get()));
    }

    @GetMapping("/aprovadaGerente/{id}")
    public ResponseEntity<Object> demandaIsAprovadaByGerente(@PathVariable(name = "id") Integer idDemanda) {
        if (!demandaService.existsById(idDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma demanda com o ID informado");
        }

        Demanda demandaHistorico = demandaService.findById(idDemanda).get();
        List<HistoricoWorkflow> historicosDemanda = historicoWorkflowService.findHistoricoWorkflowByDemanda(demandaHistorico);

        for(HistoricoWorkflow historicoWorkflow : historicosDemanda){
            if(historicoWorkflow.getUsuario() == null){
                continue;
            }
            Usuario usuarioResponsavel = usuarioService.findById(historicoWorkflow.getUsuario().getIdUsuario()).get();

            if(usuarioResponsavel instanceof GerenteNegocio && historicoWorkflow.getTarefa() == Tarefa.AVALIARDEMANDA && historicoWorkflow.getAcaoFeita() == Tarefa.APROVARDEMANDA){
                return ResponseEntity.status(HttpStatus.OK).body(true);
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(false);
    }

    @PostMapping("/{idUsuario}")
    public ResponseEntity<Object> save(
            @RequestParam("historico") @Valid String historicoJSON,
            @PathVariable(name = "idUsuario") Integer idUsuario)
            throws IOException {
        if (!usuarioService.existsById(idUsuario)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID de usuário não encontrado");
        }
        Usuario usuarioResponsavel = usuarioService.findById(idUsuario).get();

        HistoricoWorkflowUtil util = new HistoricoWorkflowUtil();
        HistoricoWorkflowCriacaoDTO historicoWorkflowDTO = util.convertJsontoDtoCriacao(historicoJSON);
        ResponseEntity<Object> historicoValido;

        if(historicoWorkflowDTO.getUsuario() != null) {
            Usuario usuarioProximoHistorico = usuarioService.findById(historicoWorkflowDTO.getUsuario().getIdUsuario()).get();
             historicoValido = historicoWorkflowService.validaCriacaoHistorico(historicoWorkflowDTO, usuarioProximoHistorico);
        } else {
            historicoValido = historicoWorkflowService.validaCriacaoHistorico(historicoWorkflowDTO, null);
        }

        if (historicoValido != null) {
            return historicoValido;
        }

        HistoricoWorkflow historicoWorkflow = new HistoricoWorkflow();
        BeanUtils.copyProperties(historicoWorkflowDTO, historicoWorkflow);
        historicoWorkflow.setStatus(StatusHistorico.EMANDAMENTO);

        Demanda demandaRelacionada = demandaService.findById(historicoWorkflowDTO.getDemanda().getIdDemanda()).get();

        historicoWorkflowService.finishHistoricoByDemanda(
                demandaRelacionada,
                historicoWorkflowDTO.getAcaoFeitaHistoricoAnterior(),
                usuarioResponsavel,
                historicoWorkflowDTO.getMotivoDevolucaoAnterior(),
                null
        );
        HistoricoWorkflow historicoSalvo = historicoWorkflowService.save(historicoWorkflow);

        if(historicoWorkflowDTO.getAcaoFeitaHistoricoAnterior() == Tarefa.REPROVARWORKFLOW){
            Proposta propostaAlterada = propostaService.findById(historicoSalvo.getDemanda().getIdDemanda()).get();
            propostaAlterada.setEmWorkflow(false);
            propostaService.save(propostaAlterada);
        }

        return ResponseEntity.status(HttpStatus.OK).body(historicoSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> edit(@RequestParam("historico") @Valid String historicoJSON, @RequestParam(value = "pdf", required = false) MultipartFile versaoPDF, @PathVariable(name = "id") Integer idHistoricoWorkflow) throws IOException {
        if (!historicoWorkflowService.existsById(idHistoricoWorkflow)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum historico workflow com o ID informado");
        }

        HistoricoWorkflowUtil util = new HistoricoWorkflowUtil();
        HistoricoWorkflowEdicaoDTO historicoWorkflowDTO = util.convertJsontoDtoEdicao(historicoJSON);
        HistoricoWorkflow historicoWorkflow = historicoWorkflowService.findById(idHistoricoWorkflow).get();

        ResponseEntity<Object> historicoValido = historicoWorkflowService.validaEdicaoHistorico(historicoWorkflowDTO, historicoWorkflow);

        if (historicoValido != null) {
            return historicoValido;
        }

        BeanUtils.copyProperties(historicoWorkflowDTO, historicoWorkflow, UtilFunctions.getPropriedadesNulas(historicoWorkflowDTO));

        historicoWorkflow.setIdHistoricoWorkflow(idHistoricoWorkflow);

        if (versaoPDF != null) {
            historicoWorkflow.setArquivoHistoricoWorkflow(new ArquivoHistoricoWorkflow(versaoPDF));
        }

        HistoricoWorkflow historicoWorkflowSalvo = historicoWorkflowService.save(historicoWorkflow);

        return ResponseEntity.status(HttpStatus.OK).body(historicoWorkflowSalvo);
    }

    @PutMapping("/demanda/{idDemanda}")
    public ResponseEntity<Object> editLastByDemanda(@RequestParam("historico") @Valid String historicoJSON, @RequestParam(value = "pdf", required = false) MultipartFile versaoPDF, @PathVariable(name = "idDemanda") Integer idDemanda) throws IOException {
        if (!demandaService.existsById(idDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma demanda com o ID informado");
        }

        HistoricoWorkflowUtil util = new HistoricoWorkflowUtil();
        HistoricoWorkflowEdicaoDTO historicoWorkflowDTO = util.convertJsontoDtoEdicao(historicoJSON);
        HistoricoWorkflow historicoWorkflow = historicoWorkflowService.findLastHistoricoByDemanda(demandaService.findById(idDemanda).get());

        ResponseEntity<Object> historicoValido = historicoWorkflowService.validaEdicaoHistorico(historicoWorkflowDTO, historicoWorkflow);

        if (historicoValido != null) {
            return historicoValido;
        }

        BeanUtils.copyProperties(historicoWorkflowDTO, historicoWorkflow, UtilFunctions.getPropriedadesNulas(historicoWorkflowDTO));

        if (versaoPDF != null) {
            historicoWorkflow.setArquivoHistoricoWorkflow(new ArquivoHistoricoWorkflow(versaoPDF));
        }

        if(historicoWorkflowDTO.getAcaoFeita() == Tarefa.REPROVARDEMANDA) {
            Demanda demandaAtualizar = demandaService.findById(historicoWorkflowDTO.getDemanda().getIdDemanda()).get();
            demandaAtualizar.setStatusDemanda(StatusDemanda.CANCELED);
            demandaService.save(demandaAtualizar);
        }

        HistoricoWorkflow historicoWorkflowSalvo = historicoWorkflowService.save(historicoWorkflow);

        return ResponseEntity.status(HttpStatus.OK).body(historicoWorkflowSalvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idHistoricoWorkflow) {
        if (!historicoWorkflowService.existsById(idHistoricoWorkflow)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhum historico workflow com o ID informado");
        }
        historicoWorkflowService.deleteById(idHistoricoWorkflow);
        return ResponseEntity.status(HttpStatus.OK).body("Historico Workflow deletado com sucesso!");
    }

}
