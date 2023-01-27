package br.weg.sod.controller;

import br.weg.sod.dto.ATACriacaoDTO;
import br.weg.sod.dto.ATAEdicaoDTO;
import br.weg.sod.dto.DecisaoPropostaATADTO;
import br.weg.sod.dto.DecisaoPropostaPautaCriacaoDTO;
import br.weg.sod.model.entities.*;
import br.weg.sod.model.entities.enuns.TipoDocumento;
import br.weg.sod.model.service.ATAService;
import br.weg.sod.model.service.DecisaoPropostaPautaService;
import br.weg.sod.model.service.HistoricoWorkflowService;
import br.weg.sod.model.service.UsuarioService;
import br.weg.sod.util.ATAUtil;
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
@RequestMapping("/sod/ata")
public class ATAController {

    private ATAService ataService;
    private HistoricoWorkflowService historicoWorkflowService;
    private DecisaoPropostaPautaService decisaoPropostaPautaService;
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<ATA>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(ataService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "id") Integer idATA) {
        if (!ataService.existsById(idATA)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma ATA com este ID");
        }

        return ResponseEntity.status(HttpStatus.OK).body(ataService.findById(idATA));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid ATACriacaoDTO ATACriacaoDTO) {
        ATA ata = new ATA();
        BeanUtils.copyProperties(ATACriacaoDTO, ata);
        return ResponseEntity.status(HttpStatus.OK).body(ataService.save(ata));
    }

    @PutMapping("/{idATA}/{idAnalista}")
    public ResponseEntity<Object> edit(@RequestParam("ata") @Valid String ataJSON, @RequestParam(value = "arquivos", required = false) MultipartFile[] multipartFiles, @PathVariable(name = "idATA") Integer idATA, @PathVariable(name = "idAnalista") Integer idAnalista) throws IOException {
        if (!ataService.existsById(idATA)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma ATA com o ID informado");
        }

        ATAUtil util = new ATAUtil();

        ATAEdicaoDTO ataDTO = util.convertJsontoDto(ataJSON);
        ATA ata = ataService.findById(idATA).get();
        BeanUtils.copyProperties(ataDTO, ata);
        ata.setIdATA(idATA);

        if(multipartFiles != null){
            AnalistaTI analistaTIresponsavel = (AnalistaTI) usuarioService.findById(idAnalista).get();
            List<TipoDocumento> tipoDocumentos = ataDTO.getTipoDocumentos();

            for(int i = 0; i < multipartFiles.length; i++){
                ata.getPauta().getArquivosPauta().add(new ArquivoPauta(multipartFiles[i], tipoDocumentos.get(i), analistaTIresponsavel));
            }
        }

        for (DecisaoPropostaATADTO decisaoPropostaATADTO : ataDTO.getPropostasAta()) {
            DecisaoPropostaATA decisaoPropostaPauta = new DecisaoPropostaATA();
            BeanUtils.copyProperties(decisaoPropostaATADTO, decisaoPropostaPauta);
            ata.getPropostasAta().add(decisaoPropostaPauta);
        }

        ATA ataSalva = ataService.save(ata);


//        List<DecisaoPropostaPauta> listaDecisaoPropostaPauta = ata.getPauta().getPropostasPauta();
//
//        for (DecisaoPropostaPauta deicasaoProposta : listaDecisaoPropostaPauta) {
//            Proposta proposta = deicasaoProposta.getProposta();
//            historicoWorkflowService.finishHistoricoByProposta(proposta, Tarefa.INFORMARPARECERDG);
//        }

        return ResponseEntity.status(HttpStatus.OK).body(ataSalva);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idATA) {
        if (!ataService.existsById(idATA)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma ATA com este ID");
        }
        ataService.deleteById(idATA);
        return ResponseEntity.status(HttpStatus.OK).body("ATA deletada com sucesso!");
    }
}
