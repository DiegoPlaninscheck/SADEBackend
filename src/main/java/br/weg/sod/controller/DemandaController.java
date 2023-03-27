package br.weg.sod.controller;

import br.weg.sod.dto.DemandaCriacaoDTO;
import br.weg.sod.dto.DemandaEdicaoDTO;
import br.weg.sod.dto.HistoricoWorkflowCriacaoDTO;
import br.weg.sod.model.entities.*;
import br.weg.sod.model.entities.enuns.StatusDemanda;
import br.weg.sod.model.entities.enuns.StatusHistorico;
import br.weg.sod.model.entities.enuns.Tarefa;
import br.weg.sod.model.service.*;
import br.weg.sod.util.DemandaUtil;
import br.weg.sod.util.HistoricoWorkflowUtil;
import br.weg.sod.util.UtilFunctions;
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
        return ResponseEntity.status(HttpStatus.OK).body(demandaService.findById(idDemanda).get());
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Demanda>> findByUsuario(@PathVariable(name = "idUsuario") Integer idUsuario) {
        return ResponseEntity.status(HttpStatus.OK).body(demandaService.findDemandasByUsuario(usuarioService.findById(idUsuario).get()));
    }

    @GetMapping("/rascunho/{isRascunho}")
    public ResponseEntity<List<Demanda>> findRascunho(@PathVariable(name = "isRascunho") boolean isRascunho) {
        return ResponseEntity.status(HttpStatus.OK).body(demandaService.findDemandasByRascunho(isRascunho));
    }

    @GetMapping("/proposta/{pertenceUmaProposta}")
    public ResponseEntity<Object> findByEstaEmPauta(@PathVariable(name = "pertenceUmaProposta") Boolean pertenceUmaProposta) {
        return ResponseEntity.status(HttpStatus.OK).body(demandaService.findDemandaByPertenceUmaProposta(pertenceUmaProposta));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Demanda>> findByStatusDemanda(@PathVariable(name = "status") StatusDemanda statusDemanda) {
        return ResponseEntity.status(HttpStatus.OK).body(demandaService.findDemandasByStatusDemanda(statusDemanda));
    }

    /**
     * fazer processo de atualização de listagem
     *
     * @param indexLista
     * @return
     */
    @GetMapping("/listagem/{index}")
    public ResponseEntity<Object> findByStartingIndex(@PathVariable(name = "index") Integer indexLista) {
//        if (!demandaService.existsById(idDemanda)) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma demanda com o ID informado");
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(demandaService.findById(idDemanda));
        return null;
    }

    @GetMapping("/{id}/arquivos")
    public ResponseEntity<Object> findArquivosDemanda(@PathVariable(name = "id") Integer idDemanda) {
        if (!demandaService.existsById(idDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma demanda com o ID informado");
        }

        return ResponseEntity.status(HttpStatus.OK).body(demandaService.findById(idDemanda).get().getArquivosDemanda());
    }

    @Transactional
    @PostMapping
    public ResponseEntity<Object> save(
            @RequestParam("demanda") @Valid String demandaJSON,
            @RequestParam(value = "files", required = false) MultipartFile[] multipartFiles,
            @RequestParam("pdfVersaoHistorico") MultipartFile versaoPDF)
            throws IOException {
        if (versaoPDF.isEmpty()) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("PDF da versão não informado");
        }

        Demanda demanda = new DemandaUtil().convertJsonToModel(demandaJSON, 1);
        ResponseEntity<Object> demandaValidada = validarDemanda(demanda);

        if (demandaValidada != null) {
            return demandaValidada;
        }

        if (multipartFiles != null) {
            for (MultipartFile multipartFile : multipartFiles) {
                demanda.getArquivosDemanda().add(new ArquivoDemanda(multipartFile, demanda.getUsuario()));
            }
        }

        Demanda demandaSalva = demandaService.save(demanda);

        System.out.println(demandaSalva);

        Timestamp momento = new Timestamp(new Date().getTime());

        HistoricoWorkflow historicoWorkflowCriacao = new HistoricoWorkflow(
                Tarefa.CRIARDEMANDA,
                StatusHistorico.CONCLUIDO,
                new ArquivoHistoricoWorkflow(versaoPDF),
                momento,
                Tarefa.CRIARDEMANDA,
                demandaSalva
        );

        HistoricoWorkflow historicoWorkflowAvaliacao = new HistoricoWorkflow(Tarefa.AVALIARDEMANDA, StatusHistorico.EMAGUARDO, demandaSalva);
        historicoWorkflowService.save(historicoWorkflowCriacao);
        historicoWorkflowService.save(historicoWorkflowAvaliacao);

        return ResponseEntity.status(HttpStatus.OK).body(demandaSalva);
    }

    @PutMapping("/{idDemanda}/{idAnalista}")
    public ResponseEntity<Object> edit(
            @RequestParam("demanda") @Valid String demandaJSON,
            @RequestParam(value = "files", required = false) MultipartFile[] multipartFiles,
            @RequestParam("pdfVersaoHistorico") MultipartFile versaoPDF,
            @PathVariable(name = "idDemanda") Integer idDemanda,
            @PathVariable(name = "idAnalista") Integer idAnalista)
            throws IOException {
        if (!demandaService.existsById(idDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma demanda com o ID informado");
        }

        if (!(usuarioService.findById(idAnalista).get() instanceof AnalistaTI)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ID de usuário informado inválido para essa ação");
        }

        if (versaoPDF.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Arquivo de versionamento da demanda não informado");
        }

        DemandaUtil util = new DemandaUtil();
        DemandaEdicaoDTO demandaDTO = util.convertJsontoDtoEdicao(demandaJSON);
        Demanda demanda = demandaService.findById(idDemanda).get();
        demanda.setIdDemanda(idDemanda);

        BeanUtils.copyProperties(demandaDTO, demanda, UtilFunctions.getPropriedadesNulas(demandaDTO));

        ResponseEntity<Object> demandaValidada = validarEdicaoDemanda(demanda, demandaDTO.getClassificando(), demandaDTO.getAdicionandoInformacoes());

        if (demandaValidada != null) {
            return demandaValidada;
        }

        if (multipartFiles != null) {
            for (MultipartFile multipartFile : multipartFiles) {
                if (multipartFile.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Arquivo não informado");
                }

                demanda.getArquivosDemanda().add(new ArquivoDemanda(multipartFile, demanda.getUsuario()));
            }
        }

        Demanda demandaSalva = demandaService.save(demanda);
        AnalistaTI analistaTI = (AnalistaTI) usuarioService.findById(idAnalista).get();

        if (demandaDTO.getClassificando()) {
            //concluindo histórico da classificacao do analista de TI
            historicoWorkflowService.finishHistoricoByDemanda(demandaSalva, Tarefa.CLASSIFICARDEMANDA, analistaTI, null, versaoPDF);

            //iniciando o histórico de avaliacao do gerente de negócio
            Usuario solicitante = usuarioService.findById(demanda.getUsuario().getIdUsuario()).get();
            GerenteNegocio gerenteNegocio = usuarioService.findGerenteByDepartamento(solicitante.getDepartamento());
            historicoWorkflowService.initializeHistoricoByDemanda(new Timestamp(new Date().getTime()), Tarefa.AVALIARDEMANDA, StatusHistorico.EMANDAMENTO, gerenteNegocio, demandaSalva);
        } else if (demandaDTO.getAdicionandoInformacoes()) {
            //conclui o histórico de adicionar informações
            historicoWorkflowService.finishHistoricoByDemanda(demandaSalva, Tarefa.ADICIONARINFORMACOESDEMANDA, analistaTI, null, versaoPDF);

            //inicia o histórico de criar proposta
            historicoWorkflowService.initializeHistoricoByDemanda(new Timestamp(new Date().getTime()), Tarefa.CRIARPROPOSTA, StatusHistorico.EMANDAMENTO, analistaTI, demandaSalva);
        } else {
            HistoricoWorkflow ultimoHistoricoConcluido = historicoWorkflowService.findLastHistoricoCompletedByDemanda(demandaSalva);
            ultimoHistoricoConcluido.setArquivoHistoricoWorkflow(new ArquivoHistoricoWorkflow(versaoPDF));
            historicoWorkflowService.save(ultimoHistoricoConcluido);
        }

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

        if (beneficiosDemanda != null && beneficiosDemanda.size() != 0) {
            try {
                beneficioService.checarBeneficios(beneficiosDemanda);
            } catch (RuntimeException exception) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Um dos benefícios passados está com inconformidades em seus dados");
            }
        }

        if (!centroCustoService.validarCentrosCusto(demanda.getCentroCustoDemanda())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Um dos centros de custo é inválido");
        }

        if (!usuarioService.existsById(demanda.getUsuario().getIdUsuario())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID de usuário inválido");
        }

        return null;
    }

    private ResponseEntity<Object> validarEdicaoDemanda(Demanda demanda, Boolean classificando, Boolean adicionandoInformacoes) {
        ResponseEntity<Object> demandaValidada = validarDemanda(demanda);

        if (demandaValidada != null) {
            return demandaValidada;
        }

        if (classificando) {
            return validarClassificando(demanda);
        }

        if (adicionandoInformacoes) {
            return validarAdicionandoInformacoes(demanda);
        }

        return null;
    }

    private ResponseEntity<Object> validarClassificando(Demanda demanda) {
        if (demanda.getTamanho() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tamanho da demanda não informado");
        }

        if (demanda.getBUSolicitante() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("BU solicitante não informada");
        }

        if (demanda.getBUsBeneficiadas() == null) {
            if (demanda.getBUsBeneficiadas().size() == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("BUs beneficiadas não foi informado");
            }
        }

        if (demanda.getSecaoTIResponsavel() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sessão de TI responsável não informada");
        }

        return null;
    }

    private ResponseEntity<Object> validarAdicionandoInformacoes(Demanda demanda) {
        if (demanda.getStatusDemanda() != StatusDemanda.ASSESMENT && demanda.getStatusDemanda() != StatusDemanda.BUSINESSCASE) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Status informado inválido");
        }

        if (demanda.getPrazoElaboracao() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Prazo de elaboração da demanda não informado");
        }

        if (demanda.getCodigoPPM() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Código PPM não informado");
        }

        if (demanda.getLinkJira() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Link para o Jira não informado");
        }

        return null;
    }
}
