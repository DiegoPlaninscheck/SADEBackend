package br.weg.sade.controller;

import br.weg.sade.model.dto.DemandaEdicaoDTO;
import br.weg.sade.model.entity.*;
import br.weg.sade.model.enums.*;
import br.weg.sade.service.*;
import br.weg.sade.util.DemandaUtil;
import br.weg.sade.util.PDFUtil;
import br.weg.sade.util.UtilFunctions;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static br.weg.sade.util.UtilFunctions.transformStringArray;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/sade/demanda")
public class DemandaController {

    private DemandaService demandaService;
    private HistoricoWorkflowService historicoWorkflowService;
    private UsuarioService usuarioService;
    private BeneficioService beneficioService;
    private CentroCustoService centroCustoService;

    private SimpMessagingTemplate simpMessagingTemplate;

    private NotificacaoService notificacaoService;

    @GetMapping
    public ResponseEntity<List<Demanda>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(demandaService.findAll());
    }

    @GetMapping("/rascunho/{rascunho}")
    public ResponseEntity<List<Demanda>> findAllByRascunho(@PathVariable("rascunho") Boolean rascunho) {
        return ResponseEntity.status(HttpStatus.OK).body(demandaService.findDemandasByRascunho(rascunho));
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

    @GetMapping("/usuario/{idUsuario}/rascunho")
    public ResponseEntity<List<Demanda>> findRascunho(@PathVariable(name = "idUsuario") Integer idUsuario) {
        List<Demanda> demandasQueSaoRascunho = demandaService.findDemandasByRascunho(true), demandasDoUsuario = new ArrayList<>();

        for (Demanda demanda : demandasQueSaoRascunho) {
            if (demanda.getUsuario().getIdUsuario() == idUsuario) {
                demandasDoUsuario.add(demanda);
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(demandasDoUsuario);
    }

    @GetMapping("/proposta/{pertenceUmaProposta}")
    public ResponseEntity<Object> findByEstaEmPauta(@PathVariable(name = "pertenceUmaProposta") Boolean pertenceUmaProposta) {
        List<Demanda> demandasSemProposta = demandaService.findDemandaByPertenceUmaProposta(pertenceUmaProposta), demandasAptas = new ArrayList<>();

        for (Demanda demandaSemProposta : demandasSemProposta) {
            if (demandaSemProposta.getLinkJira() != null) {
                demandasAptas.add(demandaSemProposta);
            }
        }


        return ResponseEntity.status(HttpStatus.OK).body(demandasAptas);
    }

    @GetMapping("/devolvidas/usuario/{idUsuario}")
    public ResponseEntity<Object> findByDevolvida(@PathVariable("idUsuario") Integer idUsuario) {
        if (!usuarioService.existsById(idUsuario)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Não foi encontrado nenhum usuario com o ID informado");
        }

        Usuario usuarioAtual = usuarioService.findById(idUsuario).get();
        List<Demanda> listaDemandasUsuario = new ArrayList<>(), listaDemandasDevolvidas = new ArrayList<>(), listaDemandas = new ArrayList<>();

        for (Demanda demanda : demandaService.findDemandasByUsuario(usuarioAtual)) {
            HistoricoWorkflow historicoWorkflowDemanda = historicoWorkflowService.findLastHistoricoByDemanda(demanda);

            if (historicoWorkflowDemanda == null) {
                continue;
            }

            if (historicoWorkflowDemanda.getTarefa().equals(Tarefa.REENVIARDEMANDA)) {
                demanda.setDevolvida(true);
                listaDemandasDevolvidas.add(demanda);
            } else {
                listaDemandasUsuario.add(demanda);
            }
        }

        listaDemandas.addAll(listaDemandasDevolvidas);
        listaDemandas.addAll(listaDemandasUsuario);

        return ResponseEntity.ok().body(listaDemandas);
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
    @PostMapping("/{forcarCriacao}")
    public ResponseEntity<Object> save(
            @RequestParam("demanda") @Valid String demandaJSON,
            @RequestParam(value = "files", required = false) MultipartFile[] multipartFiles,
            @PathVariable("forcarCriacao") Boolean forcarCriacao
    ) throws IOException {
        Demanda demanda = new DemandaUtil().convertJsonToModel(demandaJSON, 1);
        ResponseEntity<Object> demandaValidada = validarDemanda(demanda);

        if (demandaValidada != null) {
            return demandaValidada;
        }

        demanda.setScore(gerarScore(demanda));

        if (!forcarCriacao) {
            try {
                ArrayList<Demanda> listaDemandasSimilares = checarSimilaridade(demanda);

                if (listaDemandasSimilares.size() != 0) {
                    return ResponseEntity.ok().body(listaDemandasSimilares);
                }
            } catch (Exception exception) {
                System.out.println(exception);
            }
        }

        if (multipartFiles != null) {
            for (MultipartFile multipartFile : multipartFiles) {
                demanda.getArquivosDemanda().add(new ArquivoDemanda(multipartFile, demanda.getUsuario()));
            }
        }

        Demanda demandaSalva = demandaService.save(demanda);
        ArquivoHistoricoWorkflow arquivoHistoricoWorkflow = null;

        try {
            PDFUtil pdfUtil = new PDFUtil();
            arquivoHistoricoWorkflow = pdfUtil.criarPDFDemanda(demandaSalva, "criacao");
        } catch (Exception e) {
            System.out.println(e);
        }

        Timestamp momento = new Timestamp(new Date().getTime());
        HistoricoWorkflow historicoWorkflowCriacao = new HistoricoWorkflow(
                Tarefa.CRIARDEMANDA,
                StatusHistorico.CONCLUIDO,
                arquivoHistoricoWorkflow,
                momento,
                Tarefa.CRIARDEMANDA,
                demandaSalva
        );

        HistoricoWorkflow historicoWorkflowAvaliacao = new HistoricoWorkflow(Tarefa.AVALIARDEMANDA, StatusHistorico.EMAGUARDO, demandaSalva);
        historicoWorkflowService.save(historicoWorkflowCriacao);
        historicoWorkflowService.save(historicoWorkflowAvaliacao);

        return ResponseEntity.status(HttpStatus.OK).body(demandaSalva);
    }

    @Transactional
    @PostMapping("/rascunho")
    public ResponseEntity<Object> saveRascunho(@RequestParam("demanda") @Valid String demandaJSON) {
        Demanda demanda = new DemandaUtil().convertJsonToModel(demandaJSON, 1);
        Demanda demandaSalva = demandaService.save(demanda);

        return ResponseEntity.status(HttpStatus.OK).body(demandaSalva);
    }

    @PutMapping("/{idDemanda}/{idAnalista}")
    public ResponseEntity<Object> edit(
            @RequestParam("demanda") @Valid String demandaJSON,
            @RequestParam(value = "files", required = false) MultipartFile[] multipartFiles,
//            @RequestParam("pdfVersaoHistorico") MultipartFile versaoPDF,
            @PathVariable(name = "idDemanda") Integer idDemanda,
            @PathVariable(name = "idAnalista") Integer idAnalista)
            throws IOException {

        if (!demandaService.existsById(idDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma demanda com o ID informado");
        }

        Usuario usuario = usuarioService.findById(idAnalista).get();
        DemandaUtil util = new DemandaUtil();
        DemandaEdicaoDTO demandaDTO = util.convertJsontoDtoEdicao(demandaJSON);
        Demanda demanda = demandaService.findById(idDemanda).get();
        demanda.setIdDemanda(idDemanda);

        BeanUtils.copyProperties(demandaDTO, demanda, UtilFunctions.getPropriedadesNulas(demandaDTO));

        ResponseEntity<Object> demandaValidada = validarEdicaoDemanda(demanda, demandaDTO.getClassificando(), demandaDTO.getAdicionandoInformacoes());

        if (demandaValidada != null) {
            return demandaValidada;
        }

        demanda.setScore(gerarScore(demanda));

        if (multipartFiles != null) {
            for (MultipartFile multipartFile : multipartFiles) {
                if (multipartFile.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Arquivo não informado");
                }

                demanda.getArquivosDemanda().add(new ArquivoDemanda(multipartFile, demanda.getUsuario()));
            }
        }

        Demanda demandaSalva = demandaService.save(demanda);
        Usuario analistaTI = usuarioService.findById(idAnalista).get();

        PDFUtil pdfUtil = new PDFUtil();
        ArquivoHistoricoWorkflow arquivoHistoricoWorkflow = null;

        if (demandaDTO.getClassificando()) {
            arquivoHistoricoWorkflow = pdfUtil.criarPDFDemanda(demandaSalva, "classificacao");

            //concluindo histórico da classificacao do analista de TI
            historicoWorkflowService.finishHistoricoByDemanda(demandaSalva, Tarefa.CLASSIFICARDEMANDA, analistaTI, null, arquivoHistoricoWorkflow);

            //iniciando o histórico de avaliacao do gerente de negócio
            Usuario solicitante = usuarioService.findById(demanda.getUsuario().getIdUsuario()).get();
            GerenteNegocio gerenteNegocio = usuarioService.findGerenteByDepartamento(solicitante.getDepartamento());
            historicoWorkflowService.initializeHistoricoByDemanda(new Timestamp(new Date().getTime()), Tarefa.AVALIARDEMANDA, StatusHistorico.EMANDAMENTO, gerenteNegocio, demandaSalva);

            // Notificacao para o solicitante
            Notificacao notificacaoSolicitante = new Notificacao();
            notificacaoSolicitante.setAcao(AcaoNotificacao.DEMANDAAPROVADA);
            notificacaoSolicitante.setDescricaoNotificacao("Demanda aprovado pelo analista de TI");
            notificacaoSolicitante.setTituloNotificacao("Demanda Aprovada");
            notificacaoSolicitante.setTipoNotificacao(TipoNotificacao.DEMANDA);
            notificacaoSolicitante.setLinkNotificacao("http://localhost:8081/notifications/demand");
            notificacaoSolicitante.setIdComponenteLink(demandaSalva.getIdDemanda());

            List<Usuario> usuariosSolicitante = new ArrayList<>();

            usuariosSolicitante.add(demandaSalva.getUsuario());

            notificacaoSolicitante.setUsuariosNotificacao(usuariosSolicitante);

            notificacaoSolicitante = notificacaoService.save(notificacaoSolicitante);

            simpMessagingTemplate.convertAndSend("/notificacao/demanda/" + idDemanda, notificacaoSolicitante);

            // Notificação para o Gerente de Negocio
            Notificacao notificacaoGerenteNegocio = new Notificacao();
            notificacaoGerenteNegocio.setAcao(AcaoNotificacao.AVALIARDEMANDA);
            notificacaoGerenteNegocio.setDescricaoNotificacao("Há uma nova demanda para ser avaliada");
            notificacaoGerenteNegocio.setTituloNotificacao("Avaliar Demanda");
            notificacaoGerenteNegocio.setTipoNotificacao(TipoNotificacao.DEMANDA);
            notificacaoGerenteNegocio.setLinkNotificacao("http://localhost:8081/notifications/demand");
            notificacaoGerenteNegocio.setIdComponenteLink(demandaSalva.getIdDemanda());

            List<Usuario> usuariosGerenteNegocio = new ArrayList<>();

            usuariosGerenteNegocio.add(gerenteNegocio);

            notificacaoGerenteNegocio.setUsuariosNotificacao(usuariosGerenteNegocio);

            notificacaoGerenteNegocio = notificacaoService.save(notificacaoGerenteNegocio);

            simpMessagingTemplate.convertAndSend("/notificacao/demanda/" + idDemanda, notificacaoGerenteNegocio);

        } else if (demandaDTO.getAdicionandoInformacoes()) {
            arquivoHistoricoWorkflow = pdfUtil.criarPDFDemanda(demandaSalva, "adicionando");

            //conclui o histórico de adicionar informações
            historicoWorkflowService.finishHistoricoByDemanda(demandaSalva, Tarefa.ADICIONARINFORMACOESDEMANDA, analistaTI, null, arquivoHistoricoWorkflow);

            //inicia o histórico de criar proposta
            historicoWorkflowService.initializeHistoricoByDemanda(new Timestamp(new Date().getTime()), Tarefa.CRIARPROPOSTA, StatusHistorico.EMANDAMENTO, analistaTI, demandaSalva);

            Notificacao notificacao = new Notificacao();
            notificacao.setAcao(AcaoNotificacao.ADICAOINFORMACOESDEMANDA);
            notificacao.setDescricaoNotificacao("Foram adicionadas informações a sua demanda");
            notificacao.setTituloNotificacao("Adição de informações a demanda");
            notificacao.setTipoNotificacao(TipoNotificacao.DEMANDA);
            notificacao.setLinkNotificacao("http://localhost:8081/notifications/demand");
            notificacao.setIdComponenteLink(demandaSalva.getIdDemanda());

            List<Usuario> usuarios = new ArrayList<>();

            usuarios.add(demandaSalva.getUsuario());

            notificacao.setUsuariosNotificacao(usuarios);

            notificacao = notificacaoService.save(notificacao);

            simpMessagingTemplate.convertAndSend("/notificacao/demanda/" + idDemanda, notificacao);

        } else if (demandaDTO.getCriandoDemandaPorRascunho()) {
            HistoricoWorkflow historicoWorkflowCriacao = new HistoricoWorkflow(
                    Tarefa.CRIARDEMANDA,
                    StatusHistorico.CONCLUIDO,
                    arquivoHistoricoWorkflow,
                    new Timestamp(new Date().getTime()),
                    Tarefa.CRIARDEMANDA,
                    demandaSalva
            );

            HistoricoWorkflow historicoWorkflowAvaliacao = new HistoricoWorkflow(Tarefa.AVALIARDEMANDA, StatusHistorico.EMAGUARDO, demandaSalva);
            historicoWorkflowService.save(historicoWorkflowCriacao);
            historicoWorkflowService.save(historicoWorkflowAvaliacao);
        } else if (demandaDTO.getEditandoDemanda()) {
            Usuario analistaResponsavel = historicoWorkflowService.findLastHistoricoCompletedByDemanda(demandaSalva).getUsuario();

            historicoWorkflowService.finishHistoricoByDemanda(demandaSalva, Tarefa.REENVIARDEMANDA, usuario, null, null);
            historicoWorkflowService.initializeHistoricoByDemanda(new Timestamp(new Date().getTime()), Tarefa.AVALIARDEMANDA, StatusHistorico.EMANDAMENTO, analistaResponsavel, demandaSalva);
        } else {
            HistoricoWorkflow ultimoHistoricoConcluido = historicoWorkflowService.findLastHistoricoCompletedByDemanda(demandaSalva);
            ultimoHistoricoConcluido.setArquivoHistoricoWorkflow(null);
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
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Tamanho da demanda não informado");
        }

        if (demanda.getBUSolicitante() == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("BU solicitante não informada");
        }

        if (demanda.getBUsBeneficiadas() == null) {
            if (demanda.getBUsBeneficiadas().size() == 0) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("BUs beneficiadas não foi informado");
            }
        }

        if (demanda.getSecaoTIResponsavel() == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Sessão de TI responsável não informada");
        }

        return null;
    }

    private ResponseEntity<Object> validarAdicionandoInformacoes(Demanda demanda) {
        if (demanda.getStatusDemanda() != StatusDemanda.ASSESSMENT && demanda.getStatusDemanda() != StatusDemanda.BUSINESSCASE) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Status informado inválido");
        }

        if (demanda.getPrazoElaboracao() == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Prazo de elaboração da demanda não informado");
        }

        if (demanda.getCodigoPPM() == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Código PPM não informado");
        }

        if (demanda.getLinkJira() == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Link para o Jira não informado");
        }

        return null;
    }


    private Double gerarScore(Demanda demanda) {
        double score, valorBeneficiosReais = 0.0, valorBeneficiosPotenciais = 0.0;
        int indiceTamanho = 1000000000, indicePrioridade;
        long diasNaFila = 0;

        if(demanda.getTamanho() != null){
            indiceTamanho = switch (demanda.getTamanho().getNome()) {
                case "Muito Pequeno" -> 40;
                case "Pequeno" -> 300;
                case "Médio" -> 1000;
                case "Grande" -> 3000;
                case "Muito Grande" -> 5000;
                default -> 1000000000;
            };

            List<HistoricoWorkflow> historicosDemanda = historicoWorkflowService.findHistoricoWorkflowByDemanda(demanda);

            if(historicosDemanda.size() > 0){
                LocalDateTime recebimento = LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(historicosDemanda.get(0).getConclusaoTarefa().getTime()),
                                ZoneId.systemDefault())
                        .truncatedTo(ChronoUnit.DAYS);

                LocalDateTime hoje = LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(new Date().getTime()),
                                ZoneId.systemDefault())
                        .truncatedTo(ChronoUnit.DAYS);

                diasNaFila = Duration.between(recebimento, hoje).toDays();
            }
        }

        indicePrioridade = switch (demanda.getUsuario().getClass().getSimpleName()) {
            case "GerenteNegocio" -> 2;
            case "AnalistaTI" -> 4;
            case "GerenteTI" -> 16;
            default -> 1;
        };

        for(Beneficio beneficio : demanda.getBeneficiosDemanda()){
            switch (beneficio.getTipoBeneficio().getNome()) {
                case "Real" -> valorBeneficiosReais += valorTransformado(beneficio);
                case "Potencial" -> valorBeneficiosPotenciais += valorTransformado(beneficio);
            }
        }

        score = (((2 * valorBeneficiosReais) + (1 * valorBeneficiosPotenciais) + diasNaFila) / indiceTamanho) * indicePrioridade;

        return score;
    }

    private double valorTransformado(Beneficio beneficio) {
        double cotacao = 1;

        switch (beneficio.getMoeda().getNome()){
            case "Dolar" -> cotacao = 4.9;
            case "Euro" -> cotacao = 5.39;
        }

        return beneficio.getValor() * cotacao;
    }


    public ArrayList<Demanda> checarSimilaridade(@RequestBody @Valid Demanda demanda) throws IOException {
        URL url = new URL("http://localhost:5000/checar");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        String demandaJson = new Gson().toJson(demanda), resposta = "";

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = demandaJson.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;

            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            resposta = response.toString();
        } catch (Exception e) {
            System.out.println(e);
        }

        ArrayList<Integer> listaIdDemandas = transformStringArray(resposta);
        ArrayList<Demanda> listaDemandas = new ArrayList<>();

        for (Integer idDemanda : listaIdDemandas) {
            listaDemandas.add(demandaService.findById(idDemanda).get());
        }

        return listaDemandas;
    }
}
