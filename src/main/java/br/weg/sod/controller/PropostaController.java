package br.weg.sod.controller;

import br.weg.sod.dto.CentroCustoPaganteDTO;
import br.weg.sod.dto.LinhaTabelaDTO;
import br.weg.sod.dto.PropostaDTO;
import br.weg.sod.dto.TabelaCustoDTO;
import br.weg.sod.model.entities.*;
import br.weg.sod.model.entities.enuns.StatusDemanda;
import br.weg.sod.model.entities.enuns.StatusHistorico;
import br.weg.sod.model.entities.enuns.Tarefa;
import br.weg.sod.model.service.*;
import br.weg.sod.util.DemandaUtil;
import br.weg.sod.util.PropostaUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@CrossOrigin
@AllArgsConstructor
@Controller
@RequestMapping("/sod/proposta")
public class PropostaController {

    private PropostaService propostaService;
    private HistoricoWorkflowService historicoWorkflowService;
    private UsuarioService usuarioService;
    private DemandaService demandaService;
    private ArquivoDemandaService arquivoDemandaService;

    @GetMapping
    public ResponseEntity<List<Proposta>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(propostaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "id") Integer idProposta) {
        if (!propostaService.existsById(idProposta)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma proposta com o ID informado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(propostaService.findById(idProposta));
    }

    @Transactional
    @PostMapping("/{idUsuario}")
    public ResponseEntity<Object> save(@RequestParam("proposta") @Valid String propostaJSON, @RequestParam("files") MultipartFile[] multipartFiles, @PathVariable("idUsuario") Integer idUsuario) throws IOException  {
        PropostaUtil util = new PropostaUtil();
        Proposta proposta = util.convertJsonToModel(propostaJSON);
        proposta.setIdProposta(proposta.getDemanda().getIdDemanda());

        Integer valorPayback = 2; //depois fazer a conta com payback e custo totais e os caralho

        proposta.setPayback(valorPayback);
        Proposta propostaSalva = propostaService.save(proposta);
        Demanda demandaRelacionada = propostaSalva.getDemanda();

        for(MultipartFile multipartFile : multipartFiles){
            demandaRelacionada.getArquivosDemanda().add(new ArquivoDemanda(multipartFile, usuarioService.findById(idUsuario).get()));
        }

        demandaService.save(demandaRelacionada);

        //encerra o histórico da criação de proposta
        historicoWorkflowService.finishHistoricoByProposta(proposta, Tarefa.CRIARPAUTA);

        return ResponseEntity.status(HttpStatus.OK).body(propostaSalva);
    }

    @PutMapping("/{idProposta}/{idAnalista}")
    public ResponseEntity<Object> edit(@RequestParam("proposta") @Valid String propostaJSON, @RequestParam("files") MultipartFile[] multipartFiles, @PathVariable(name = "idProposta") Integer idProposta, @PathVariable(name = "idAnalista") Integer idAnalista) throws IOException {
        if (!propostaService.existsById(idProposta)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma proposta com o ID informado");
        }

        PropostaUtil util = new PropostaUtil();
        Proposta proposta = util.convertJsonToModel(propostaJSON);
        Proposta propostaBanco = propostaService.findById(idProposta).get();
        BeanUtils.copyProperties(propostaBanco, proposta);
        proposta.setIdProposta(idProposta);

        Proposta propostaSalva = propostaService.save(proposta);
        Demanda demandaRelacionada = propostaSalva.getDemanda();

        for(MultipartFile multipartFile : multipartFiles){
            demandaRelacionada.getArquivosDemanda().add(new ArquivoDemanda(multipartFile, usuarioService.findById(idAnalista).get()));
        }

        demandaService.save(demandaRelacionada);

        if (proposta.getEmWorkflow() && proposta.getAprovadoWorkflow() == null) {
            //inicia o histórico de aprovação em workflow
            Demanda demandaVinculada = demandaService.findById(proposta.getIdProposta()).get();
            Usuario solicitanteDemanda = usuarioService.findById(demandaVinculada.getUsuario().getIdUsuario()).get();
            GerenteNegocio gerenteDoSolicitante = usuarioService.findGerenteByDepartamento(solicitanteDemanda.getDepartamento());


            historicoWorkflowService.initializeHistoricoByProposta(new Timestamp(new Date().getTime()), Tarefa.AVALIARWORKFLOW, StatusHistorico.EMANDAMENTO, gerenteDoSolicitante, proposta);
        }

        return ResponseEntity.status(HttpStatus.OK).body(propostaService.save(proposta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idProposta) {
        if (!propostaService.existsById(idProposta)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma proposta com o ID informado");
        }
        propostaService.deleteById(idProposta);
        return ResponseEntity.status(HttpStatus.OK).body("Proposta deletada com sucesso!");
    }
}
