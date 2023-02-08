package br.weg.sod.controller;

import br.weg.sod.dto.ATACriacaoDTO;
import br.weg.sod.dto.ATAEdicaoDTO;
import br.weg.sod.dto.DecisaoPropostaATADTO;
import br.weg.sod.dto.DecisaoPropostaPautaCriacaoDTO;
import br.weg.sod.model.entities.*;
import br.weg.sod.model.entities.enuns.TipoDocumento;
import br.weg.sod.model.service.*;
import br.weg.sod.util.ATAUtil;
import br.weg.sod.util.PautaUtil;
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
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@AllArgsConstructor
@Controller
@RequestMapping("/sod/ata")
public class ATAController {

    private ATAService ataService;
    private HistoricoWorkflowService historicoWorkflowService;
    private DecisaoPropostaATAService decisaoPropostaATAService;
    private UsuarioService usuarioService;
    private PautaService pautaService;

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
        if(!pautaService.existsById(ATACriacaoDTO.getPauta().getIdPauta())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id de pauta informado inválido");
        }

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
        ATA ata = ataService.findById(idATA).get();
        ATAEdicaoDTO ataDTO = util.convertJsontoDto(ataJSON);

        if(ataService.existsByNumeroDG(ataDTO.getNumeroDG())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Número da DG já registrado em ourta ATA");
        }

        if(multipartFiles.length != ataDTO.getTipoDocumentos().size()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Os documentos passados não são coesos com as informações passadas sobre eles");
        }

        if(ata.getPauta().getPropostasPauta().size() != ataDTO.getPropostasAta().size()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("A quantidade de decisões de ata informada é inválida");
        }

        if(!decisaoPropostaATAService.decisoesValidas(ata.getPauta().getPropostasPauta(), ataDTO.getPropostasAta())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Decisões da ata contém número de id de proposta inválido ou número sequencial já registrado/repetido");
        }

        BeanUtils.copyProperties(ataDTO, ata, UtilFunctions.getPropriedadesNulas(ataDTO));
        ata.setIdATA(idATA);
        AnalistaTI analistaTIresponsavel = (AnalistaTI) usuarioService.findById(idAnalista).get();

        if(multipartFiles != null){
            List<TipoDocumento> tipoDocumentos = ataDTO.getTipoDocumentos();
            List<ArquivoPauta> arquivosAta = ata.getPauta().getArquivosPauta();

            for(int i = 0; i < multipartFiles.length; i++){
                arquivosAta.add(new ArquivoPauta(multipartFiles[i], tipoDocumentos.get(i), analistaTIresponsavel));
            }

            ata.getPauta().setArquivosPauta(arquivosAta);
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
