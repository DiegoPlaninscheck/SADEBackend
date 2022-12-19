package br.weg.sod.controller;

import br.weg.sod.dto.DecisaoPropostaPautaCriacaoDTO;
import br.weg.sod.dto.DecisaoPropostaPautaEdicaoDTO;
import br.weg.sod.dto.PautaCriacaoDTO;
import br.weg.sod.dto.PautaEdicaoDTO;
import br.weg.sod.model.entities.*;
import br.weg.sod.model.service.HistoricoWorkflowService;
import br.weg.sod.model.service.PautaService;
import br.weg.sod.model.service.UsuarioService;
import br.weg.sod.util.DemandaUtil;
import br.weg.sod.util.PautaUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@AllArgsConstructor
@Controller
@RequestMapping("/sod/pauta")
public class PautaController {

    private PautaService pautaService;
    private HistoricoWorkflowService historicoWorkflowService;
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Pauta>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(pautaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "id") Integer idPauta) {
        if (!pautaService.existsById(idPauta)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma pauta com o ID informado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(pautaService.findById(idPauta));
    }

    @PostMapping("/{idAnalista}")
    public ResponseEntity<Object> save(@RequestBody @Valid PautaCriacaoDTO pautaCriacaoDTO, @PathVariable(name = "idAnalista") Integer idAnalista) {
        Pauta pauta = new Pauta();
        BeanUtils.copyProperties(pautaCriacaoDTO, pauta);

        List<DecisaoPropostaPauta> propostaPautas = new ArrayList<>();

        for (DecisaoPropostaPautaCriacaoDTO decisaoPropostaPautaDTO : pautaCriacaoDTO.getPropostasPauta()) {
            DecisaoPropostaPauta decisaoPropostaPauta = new DecisaoPropostaPauta();
            BeanUtils.copyProperties(decisaoPropostaPautaDTO, decisaoPropostaPauta);
            propostaPautas.add(decisaoPropostaPauta);
        }

        pauta.setPropostasPauta(propostaPautas);

        Pauta pautaSalva = pautaService.save(pauta);

//        for(DecisaoPropostaPauta decisaoPropostaPauta : pautaSalva.getPropostasPauta()){
////            encerrar historico criar pauta
//            historicoWorkflowService.finishHistoricoByProposta(decisaoPropostaPauta.getProposta(), Tarefa.CRIARPAUTA);
//
////            inicio informar parecer da comissao
//            Timestamp time = new Timestamp(pautaSalva.getDataReuniao().getTime());
//            AnalistaTI analistaResponsavel = (AnalistaTI) usuarioService.findById(idAnalista).get();
//
//            historicoWorkflowService.initializeHistoricoByProposta(time,Tarefa.INFORMARPARECERFORUM, StatusHistorico.EMAGUARDO, analistaResponsavel, decisaoPropostaPauta.getProposta());
//        }

        return ResponseEntity.status(HttpStatus.OK).body(pautaSalva);
    }

    @PutMapping("/{idPauta}/{idAnalista}")
    public ResponseEntity<Object> edit(@RequestParam("pauta") @Valid String pautaJSON, @RequestParam(value = "ata", required = false) MultipartFile multipartFile, @PathVariable(name = "idPauta") Integer idPauta, @PathVariable(name = "idAnalista") Integer idAnalista) throws IOException {
        if (!pautaService.existsById(idPauta)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma pauta com o ID informado");
        }

        PautaUtil util = new PautaUtil();

        Pauta pauta = util.convertJsonToModel(pautaJSON);
        pauta.setIdPauta(idPauta);

        if (multipartFile != null) {
            AnalistaTI analistaTIresponsavel = (AnalistaTI) usuarioService.findById(idAnalista).get();
            List<ArquivoPauta> arquivosPauta = new ArrayList<>();

            arquivosPauta.add(new ArquivoPauta(multipartFile, analistaTIresponsavel));

            pauta.setArquivosPauta(arquivosPauta);
        }

        //fazer a pauta converter uma lista de DTO de decisao pauta pra uma lista de decisao pauta

//        List<DecisaoPropostaPauta> propostaPautas = new ArrayList<>();
//
//        for(DecisaoPropostaPautaEdicaoDTO decisaoPropostaPautaDTO : pautaEdicaoDTO.getPropostasPauta()){
//            DecisaoPropostaPauta decisaoPropostaPauta = new DecisaoPropostaPauta();
//            BeanUtils.copyProperties(decisaoPropostaPautaDTO, decisaoPropostaPauta);
//            propostaPautas.add(decisaoPropostaPauta);
//        }

//        pauta.setPropostasPauta(propostaPautas);

        //pautaService.save(pauta)

        return ResponseEntity.status(HttpStatus.OK).body(pautaService.save(pauta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idPauta) {
        if (!pautaService.existsById(idPauta)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma pauta com o ID informado");
        }
        pautaService.deleteById(idPauta);
        return ResponseEntity.status(HttpStatus.OK).body("Pauta deletada com sucesso!");
    }
}
