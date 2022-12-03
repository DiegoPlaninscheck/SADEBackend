package br.weg.sod.controller;

import br.weg.sod.dto.PropostaDTO;
import br.weg.sod.model.entities.*;
import br.weg.sod.model.entities.enuns.StatusHistorico;
import br.weg.sod.model.entities.enuns.Tarefa;
import br.weg.sod.model.service.HistoricoWorkflowService;
import br.weg.sod.model.service.PropostaService;
import br.weg.sod.model.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
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
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid PropostaDTO propostaDTO) {
        Proposta proposta = new Proposta();
        BeanUtils.copyProperties(propostaDTO, proposta);

        System.out.println(propostaDTO.toString());

        //encerra o histórico da criação de proposta
        historicoWorkflowService.finishHistoricoByProposta(proposta, Tarefa.CRIARPAUTA);

        return ResponseEntity.status(HttpStatus.OK).body(propostaService.save(proposta));
    }

    @PutMapping("/{idProposta}/{idAnalista}")
    public ResponseEntity<Object> edit(@RequestBody @Valid PropostaDTO propostaDTO, @PathVariable(name = "idProposta") Integer idProposta, @PathVariable(name = "idAnalista") Integer idAnalista) {
        if (!propostaService.existsById(idProposta)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma proposta com o ID informado");
        }

        Proposta proposta = propostaService.findById(idProposta).get();
        BeanUtils.copyProperties(propostaDTO, proposta);
        proposta.setIdProposta(idProposta);

        if (proposta.getEmWorkflow() && proposta.getAprovadoWorkflow() == null) {
            //inicia o histórico de aprovação em workflow
            GerenteNegocio gerenteNegocio = usuarioService.findGerenteBySolicitante(proposta.getDemanda().getUsuario().getDepartamento());

            historicoWorkflowService.initializeHistoricoByProposta(new Timestamp(new Date().getTime()), Tarefa.AVALIARWORKFLOW, StatusHistorico.EMANDAMENTO, gerenteNegocio, proposta);
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
