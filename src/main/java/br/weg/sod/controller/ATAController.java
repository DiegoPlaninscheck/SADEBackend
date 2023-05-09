package br.weg.sod.controller;

import br.weg.sod.dto.ATACriacaoDTO;
import br.weg.sod.dto.ATAEdicaoDTO;
import br.weg.sod.dto.DecisaoPropostaATADTO;
import br.weg.sod.model.entities.*;
import br.weg.sod.model.entities.enuns.StatusDemanda;
import br.weg.sod.model.entities.enuns.StatusHistorico;
import br.weg.sod.model.entities.enuns.Tarefa;
import br.weg.sod.model.service.*;
import br.weg.sod.util.ATAUtil;
import br.weg.sod.util.UtilFunctions;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/sod/ata")
public class ATAController {

    private ATAService ataService;
    private HistoricoWorkflowService historicoWorkflowService;
    private DecisaoPropostaATAService decisaoPropostaATAService;
    private UsuarioService usuarioService;
    private PautaService pautaService;
    private DemandaService demandaService;

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
    public ResponseEntity<Object> save(@RequestBody @Valid ATACriacaoDTO ataDTO) {
        if(!pautaService.existsById(ataDTO.getPauta().getIdPauta())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id de pauta informado inválido");
        }

        if(ataDTO.getFinalReuniao().getTime() < ataDTO.getInicioReuniao().getTime()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Horários de reunião inválidos");
        }

        ATA ata = new ATA();
        BeanUtils.copyProperties(ataDTO, ata);
        ata.setUsuariosReuniaoATA(usuarioService.findGerentesTI());

        Pauta pautaDaAta = pautaService.findById(ata.getPauta().getIdPauta()).get();
        List<DecisaoPropostaATA> decisoesPropostasATA = new ArrayList<>();

        for(DecisaoPropostaPauta decisaoPropostaPauta : pautaDaAta.getPropostasPauta()){
            DecisaoPropostaATA decisaoPropostaATA = new DecisaoPropostaATA();
            decisaoPropostaATA.setProposta(decisaoPropostaPauta.getProposta());

            decisoesPropostasATA.add(decisaoPropostaATA);
        }

        ata.setPropostasAta(decisoesPropostasATA);

//        pautaDaAta.setPertenceUmaATA(true);
        pautaService.save(pautaDaAta);

        return ResponseEntity.status(HttpStatus.OK).body(ataService.save(ata));
    }

    @PutMapping("/{idATA}/{idAnalista}")
    public ResponseEntity<Object> edit(
            @RequestParam("ata") @Valid String ataJSON,
            @RequestParam(value = "arquivos", required = false) MultipartFile[] multipartFiles,
            @PathVariable(name = "idATA") Integer idATA,
            @PathVariable(name = "idAnalista") Integer idAnalista)
            throws IOException {
        if (!ataService.existsById(idATA)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma ATA com o ID informado");
        }

        ATAUtil util = new ATAUtil();
        ATA ata = ataService.findById(idATA).get();
        ATAEdicaoDTO ataDTO = util.convertJsontoDto(ataJSON);

        ResponseEntity<Object> validacaoEdicao = validacoesEdicaoATA(ataDTO, ata, multipartFiles);
        System.out.println(validacaoEdicao);

        if (validacaoEdicao != null) {
            return validacaoEdicao;
        }

        BeanUtils.copyProperties(ataDTO, ata, UtilFunctions.getPropriedadesNulas(ataDTO));
        ata.setIdATA(idATA);
        Usuario analistaTIresponsavel =  usuarioService.findById(idAnalista).get();

        if(multipartFiles != null){
            List<ArquivoPauta> arquivosAta = ata.getPauta().getArquivosPauta();

            for(int i = 0; i < multipartFiles.length; i++){
                arquivosAta.add(new ArquivoPauta(multipartFiles[i], analistaTIresponsavel));
            }

            ata.getPauta().setArquivosPauta(arquivosAta);
        }

        for (DecisaoPropostaATADTO decisaoPropostaATADTO : ataDTO.getPropostasAta()) {
            DecisaoPropostaATA decisaoPropostaPauta = new DecisaoPropostaATA();
            BeanUtils.copyProperties(decisaoPropostaATADTO, decisaoPropostaPauta);
            ata.getPropostasAta().add(decisaoPropostaPauta);
        }

        ATA ataSalva = ataService.save(ata);

        for (DecisaoPropostaATA deicasaoProposta : ataSalva.getPropostasAta()) {
            Demanda demandaDecisao = demandaService.findById(deicasaoProposta.getProposta().getIdProposta()).get();
            Tarefa tarefaStatus;
            StatusDemanda statusEscolhidoComissao = deicasaoProposta.getStatusDemandaComissao();
            boolean continuaProcesso = false;

            if(statusEscolhidoComissao == StatusDemanda.TODO){
                tarefaStatus = Tarefa.FINALIZAR;
            } else if (statusEscolhidoComissao == StatusDemanda.CANCELED){
                tarefaStatus = Tarefa.REPROVARDEMANDA;
            } else {
                tarefaStatus = Tarefa.CRIARPAUTA;
                continuaProcesso = true;
            }

            demandaDecisao.setStatusDemanda(statusEscolhidoComissao);
            demandaService.save(demandaDecisao);

            //finaliza histórico de informar parecer da DG
            historicoWorkflowService.finishHistoricoByDemanda(demandaDecisao, tarefaStatus, analistaTIresponsavel, null, null);

            if(continuaProcesso) {
                //inicia histórico de criar pauta
                historicoWorkflowService.initializeHistoricoByDemanda(new Timestamp(new Date().getTime()), Tarefa.CRIARPAUTA, StatusHistorico.EMANDAMENTO, analistaTIresponsavel, demandaDecisao);
            }
        }

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

    private ResponseEntity<Object> validacoesEdicaoATA(ATAEdicaoDTO ataDTO, ATA ata, MultipartFile multipartFiles[]) {
        if(ataDTO.getFinalReuniao() != null && ataDTO.getInicioReuniao() != null) {
            if (ataDTO.getFinalReuniao().getTime() < ataDTO.getInicioReuniao().getTime()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Horários de reunião inválidos");
            }
        }

        if(ata.getPauta().getPropostasPauta().size() != ataDTO.getPropostasAta().size()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A quantidade de decisões de ata informada é inválida");
        }

        if(!decisaoPropostaATAService.decisoesValidas(ata.getPauta().getPropostasPauta(), ataDTO.getPropostasAta())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Decisões da ata contém número de id de proposta inválido ou número sequencial já registrado/repetido");
        }

        return null;
    }

}
