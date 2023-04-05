package br.weg.sod.controller;

import br.weg.sod.dto.DecisaoPropostaPautaCriacaoDTO;
import br.weg.sod.dto.DecisaoPropostaPautaEdicaoDTO;
import br.weg.sod.dto.PautaCriacaoDTO;
import br.weg.sod.dto.PautaEdicaoDTO;
import br.weg.sod.model.entities.*;
import br.weg.sod.model.entities.enuns.StatusHistorico;
import br.weg.sod.model.entities.enuns.Tarefa;
import br.weg.sod.model.entities.enuns.TipoDocumento;
import br.weg.sod.model.service.*;
import br.weg.sod.util.PautaUtil;
import br.weg.sod.util.UtilFunctions;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/sod/pauta")
public class PautaController {

    private PautaService pautaService;
    private HistoricoWorkflowService historicoWorkflowService;
    private UsuarioService usuarioService;
    private DecisaoPropostaPautaService decisaoPropostaPautaService;
    private PropostaService propostaService;
    private ForumService forumService;

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
    public ResponseEntity<Object> save(
            @RequestBody @Valid PautaCriacaoDTO pautaCriacaoDTO,
            @PathVariable(name = "idAnalista") Integer idAnalista)
            throws IOException {
        if(!validacaoPropostasCriacao(pautaCriacaoDTO.getPropostasPauta())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Uma das propostas informadas já está em uma pauta ou o id informado não existe");
        }

        if(!forumService.existsById(pautaCriacaoDTO.getForum().getIdForum())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("O id do fórum informado não existe");
        }

        Pauta pauta = new Pauta();
        BeanUtils.copyProperties(pautaCriacaoDTO, pauta);

        for (DecisaoPropostaPautaCriacaoDTO decisaoPropostaPautaDTO : pautaCriacaoDTO.getPropostasPauta()) {
            DecisaoPropostaPauta decisaoPropostaPauta = new DecisaoPropostaPauta();
            BeanUtils.copyProperties(decisaoPropostaPautaDTO, decisaoPropostaPauta);
            pauta.getPropostasPauta().add(decisaoPropostaPauta);

            Proposta propostaDaPauta = propostaService.findById(decisaoPropostaPauta.getProposta().getIdProposta()).get();
            propostaDaPauta.setEstaEmPauta(true);
            propostaService.save(propostaDaPauta);
        }

        Pauta pautaSalva = pautaService.save(pauta);

        for(DecisaoPropostaPauta decisaoPropostaPauta : pauta.getPropostasPauta()){
//            encerrar historico criar pauta
            Demanda demandaDecisao = propostaService.findById(decisaoPropostaPauta.getProposta().getIdProposta()).get().getDemanda();
            AnalistaTI analistaResponsavel = (AnalistaTI) usuarioService.findById(idAnalista).get();
            historicoWorkflowService.finishHistoricoByDemanda(demandaDecisao, Tarefa.CRIARPAUTA,analistaResponsavel, null, null );

//            inicio informar parecer da comissao
            historicoWorkflowService.initializeHistoricoByDemanda(
                    new Timestamp(pauta.getDataReuniao().getTime()),
                    Tarefa.INFORMARPARECERFORUM,
                    StatusHistorico.EMAGUARDO,
                    analistaResponsavel,
                    demandaDecisao
            );
        }

        return ResponseEntity.status(HttpStatus.OK).body(pautaSalva);
    }

    @PutMapping("/{idPauta}/{idAnalista}")
    public ResponseEntity<Object> edit(@RequestParam("pauta") @Valid String pautaJSON, @RequestParam(value = "ata", required = false) MultipartFile multipartFile, @PathVariable(name = "idPauta") Integer idPauta, @PathVariable(name = "idAnalista") Integer idAnalista) throws IOException {
        if (!pautaService.existsById(idPauta)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma pauta com o ID informado");
        }

        PautaUtil util = new PautaUtil();
        Pauta pauta = pautaService.findById(idPauta).get();
        PautaEdicaoDTO pautaDTO = util.convertJsontoDto(pautaJSON);

        if (!dataFutura(pautaDTO.getDataReuniao())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Data de reunião informada inválida");
        }

        if(!validacaoPropostasEdicao(pautaDTO.getPropostasPauta(), pauta)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Uma das propostas informadas já está em uma pauta ou o id informado não existe");
        }

        BeanUtils.copyProperties(pautaDTO, pauta, UtilFunctions.getPropriedadesNulas(pautaDTO));
        pauta.setIdPauta(idPauta);
        AnalistaTI analistaTIresponsavel = (AnalistaTI) usuarioService.findById(idAnalista).get();

        if (multipartFile != null) {
            if(multipartFile.isEmpty()){
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("PDF a ata da reunião não informado");
            }
            List<ArquivoPauta> arquivosPauta = new ArrayList<>();

            arquivosPauta.add(new ArquivoPauta(multipartFile, TipoDocumento.ATAREUNIAO ,analistaTIresponsavel));

            pauta.setArquivosPauta(arquivosPauta);
        }

        List<DecisaoPropostaPauta> decisoesPauta = new ArrayList<>();

        for(DecisaoPropostaPautaEdicaoDTO decisaoDTO : pautaDTO.getPropostasPauta()){
            DecisaoPropostaPauta decisaoPropostaPautaNova = decisaoPropostaPautaService.findById(decisaoDTO.getIdDecisaoPropostaPauta()).get();
            Proposta propostaDaDecisao = decisaoPropostaPautaNova.getProposta();

            BeanUtils.copyProperties(decisaoDTO, decisaoPropostaPautaNova);

            if(decisaoPropostaPautaNova.getProposta() == null){
                decisaoPropostaPautaNova.setProposta(propostaDaDecisao);
            }

            decisoesPauta.add(decisaoPropostaPautaNova);
        }

        List<DecisaoPropostaPauta> propostasAprovadasWorkflow = decisaoPropostaPautaService.createDecisaoPropostaWorkflow(propostaService.getPropostasAprovadasWorkflow());

        List<DecisaoPropostaPauta> todasDecisoesAprovadas = Stream.concat(propostasAprovadasWorkflow.stream(), decisoesPauta.stream()).toList();

        pauta.setPropostasPauta(todasDecisoesAprovadas);

        Pauta pautaSalva = pautaService.save(pauta);

        for(DecisaoPropostaPauta decisaoPropostaPauta : pautaSalva.getPropostasPauta()){
//            encerrar historico de informar o parecer
            Demanda demandaDecisao = propostaService.findById(decisaoPropostaPauta.getProposta().getIdProposta()).get().getDemanda();
            historicoWorkflowService.finishHistoricoByDemanda(demandaDecisao, Tarefa.INFORMARPARECERFORUM,analistaTIresponsavel, null, null );

            historicoWorkflowService.initializeHistoricoByDemanda(new Timestamp(pautaDTO.getDataReuniaoATA().getTime()), Tarefa.INFORMARPARECERDG, StatusHistorico.EMAGUARDO, analistaTIresponsavel, demandaDecisao);
        }

        return ResponseEntity.status(HttpStatus.OK).body(pauta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idPauta) {
        if (!pautaService.existsById(idPauta)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma pauta com o ID informado");
        }
        pautaService.deleteById(idPauta);
        return ResponseEntity.status(HttpStatus.OK).body("Pauta deletada com sucesso!");
    }

    private boolean validacaoPropostasCriacao(List<DecisaoPropostaPautaCriacaoDTO> propostasPauta) {
        List<Proposta> propostasDaDecisao = new ArrayList<>();

        for(DecisaoPropostaPautaCriacaoDTO decisaoPropostaDTO : propostasPauta){
            propostasDaDecisao.add(decisaoPropostaDTO.getProposta());
        }

        //ver se as propostas estão em algum processo de aprovação em aberto
        if(!propostaService.propostasExistem(propostasDaDecisao)){
            return false;
        }

        //ver se as propostas existem
        if(!propostasLivres(propostasDaDecisao)){
            return false;
        }

        return true;
    }

    private boolean validacaoPropostasEdicao(List<DecisaoPropostaPautaEdicaoDTO> propostasPauta, Pauta pautaBancoDados) {
        List<Proposta> propostasDaDecisao = new ArrayList<>();
        List<DecisaoPropostaPauta> decisoesPauta =  pautaBancoDados.getPropostasPauta();

        for(DecisaoPropostaPautaEdicaoDTO decisaoPropostaDTO : propostasPauta){
            if(decisaoPropostaDTO.getProposta() != null){
                propostasDaDecisao.add(decisaoPropostaDTO.getProposta());
            }

            //ver se o id de decisaoPropostaPauta informado já tá vinculado àquela pauta
            boolean existe = false;

            for(DecisaoPropostaPauta decisaoPauta : decisoesPauta){
                if(decisaoPauta.getIdDecisaoPropostaPauta() == decisaoPropostaDTO.getIdDecisaoPropostaPauta()){
                    existe = true;
                    break;
                }
            }

            if(!existe){
                return false;
            }
        }

        //ver se existem propostas para serem avaliadas
        if(propostasDaDecisao.size() != 0) {

            //ver se as propostas existem
            if (!propostaService.propostasExistem(propostasDaDecisao)) {
                return false;
            }

            //ver se as propostas já não estão em outra pauta
            if(!propostasLivres(propostasDaDecisao)){
                return false;
            }
        }

        return true;
    }

    private boolean propostasLivres(List<Proposta> propostasPauta){
        for(Proposta proposta : propostasPauta){
            if(decisaoPropostaPautaService.existsByProposta(proposta)){
                for(Pauta pautaContemProposta : pautaService.findByProposta(proposta)){
                    if(!pautaService.pautaFinalizada(pautaContemProposta)){
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private boolean dataFutura(Date dataReuniao) {
        return dataReuniao.getTime() > new Date().getTime();
    }
}
