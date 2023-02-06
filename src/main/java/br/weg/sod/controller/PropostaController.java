package br.weg.sod.controller;

import br.weg.sod.model.entities.*;
import br.weg.sod.model.entities.enuns.StatusHistorico;
import br.weg.sod.model.entities.enuns.Tarefa;
import br.weg.sod.model.service.*;
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
    private TabelaCustoService tabelaCustoService;
    private LinhaTabelaSevice linhaTabelaSevice;
    private CentroCustoPaganteService centroCustoPaganteService;
    private CentroCustoService centroCustoService;

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
    @PostMapping("/{idAnalista}")
    public ResponseEntity<Object> save(@RequestParam("proposta") @Valid String propostaJSON, @RequestParam(value = "files", required = false) MultipartFile[] multipartFiles, @PathVariable("idAnalista") Integer idAnalista) throws IOException {
        PropostaUtil util = new PropostaUtil();
        Proposta proposta = util.convertJsonToModel(propostaJSON, 1);

        ResponseEntity<Object> validacao = validacoesProposta(proposta, idAnalista);

        if (validacao != null) {
            return validacao;
        }

        Integer valorPayback = 2; //depois fazer a conta com payback e custo totais e os caralho

        proposta.setPayback(valorPayback);
        proposta.setIdProposta(proposta.getDemanda().getIdDemanda());
        AnalistaTI analistaResponsavel = (AnalistaTI) usuarioService.findById(idAnalista).get();

        if (multipartFiles != null) {
            for (MultipartFile multipartFile : multipartFiles) {
                proposta.getDemanda().getArquivosDemanda().add(new ArquivoDemanda(multipartFile, analistaResponsavel));
            }
        }

        Proposta propostaSalva = propostaService.save(proposta);

        //encerra o histórico da criação de proposta
//        historicoWorkflowService.finishHistoricoByProposta(proposta, Tarefa.CRIARPAUTA);

        //inicia o histórico de criar pauta
//        historicoWorkflowService.initializeHistoricoByProposta(new Timestamp(new Date().getTime()),Tarefa.CRIARPAUTA, StatusHistorico.EMANDAMENTO, analistaResponsavel, propostaSalva);

        return ResponseEntity.status(HttpStatus.OK).body(propostaSalva);
    }

    @PutMapping("/{idProposta}/{idAnalista}")
    public ResponseEntity<Object> edit(@RequestParam("proposta") @Valid String propostaJSON, @RequestParam(value = "files", required = false) MultipartFile[] multipartFiles, @PathVariable(name = "idProposta") Integer idProposta, @PathVariable(name = "idAnalista") Integer idAnalista) throws IOException {
        if (!propostaService.existsById(idProposta)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma proposta com o ID informado");
        }

        if (!(usuarioService.findById(idAnalista).get() instanceof AnalistaTI)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ID de usuário informado inválido para essa ação");
        }

        PropostaUtil util = new PropostaUtil();
        Proposta proposta = util.convertJsonToModel(propostaJSON, 2);
        proposta.setIdProposta(idProposta);
        Proposta propostaDB = propostaService.findById(idProposta).get();
        proposta.setDemanda(propostaDB.getDemanda());
        proposta.setPayback(propostaDB.getPayback());

        ResponseEntity<Object> validacaoEdicao = validacoesItensProposta(proposta, idAnalista);

        if (validacaoEdicao != null) {
            return validacaoEdicao;
        }

        if (multipartFiles != null) {
            for (MultipartFile multipartFile : multipartFiles) {
                proposta.getDemanda().getArquivosDemanda().add(new ArquivoDemanda(multipartFile, usuarioService.findById(idAnalista).get()));
            }
        }

        Proposta propostaSalva = propostaService.save(proposta);

//        if (proposta.getEmWorkflow() && proposta.getAprovadoWorkflow() == null) {
//            //inicia o histórico de aprovação em workflow
//            Demanda demandaVinculada = demandaService.findById(proposta.getIdProposta()).get();
//            Usuario solicitanteDemanda = usuarioService.findById(demandaVinculada.getUsuario().getIdUsuario()).get();
//            GerenteNegocio gerenteDoSolicitante = usuarioService.findGerenteByDepartamento(solicitanteDemanda.getDepartamento());
//
//            historicoWorkflowService.initializeHistoricoByProposta(new Timestamp(new Date().getTime()), Tarefa.AVALIARWORKFLOW, StatusHistorico.EMANDAMENTO, gerenteDoSolicitante, proposta);
//        }

        return ResponseEntity.status(HttpStatus.OK).body(propostaSalva);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable(name = "id") Integer idProposta) {
        if (!propostaService.existsById(idProposta)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado nenhuma proposta com o ID informado");
        }
        propostaService.deleteById(idProposta);
        return ResponseEntity.status(HttpStatus.OK).body("Proposta deletada com sucesso!");
    }

    private ResponseEntity<Object> validacoesProposta(Proposta proposta, Integer idAnalista) {
        if (proposta.getPeriodoExecucaoFim().getTime() < proposta.getPeriodoExecucaoInicio().getTime()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("O período de execução inválido");
        }

        if (!(usuarioService.findById(idAnalista).get() instanceof AnalistaTI)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ID de usuário informado inválido para essa ação");
        }

        List<Usuario> responsaveisNegocio = proposta.getResponsaveisNegocio();

        if (responsaveisNegocio != null && responsaveisNegocio.size() != 0) {
            if (!usuarioService.responsaveisValidos(responsaveisNegocio)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID de álgum responsável pela proposta informado inválido");
            }
        }

        List<TabelaCusto> tabelasCustoProposta = proposta.getTabelasCustoProposta();

        if (tabelasCustoProposta != null) {
            if (tabelasCustoProposta.size() != 0) {
                if (!propostaService.tabelasvalidas(tabelasCustoProposta)) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tabelas de custo contém informações inválidas");
                }

                for (TabelaCusto tabelaCusto : tabelasCustoProposta) {
                    List<CentroCustoPagante> centroDeCustos = tabelaCusto.getCentrosCustoPagantes();

                    if (centroDeCustos != null && centroDeCustos.size() != 0) {
                        for (CentroCustoPagante centroCustoPagante : centroDeCustos) {

                            if (!centroCustoService.existsById(centroCustoPagante.getCentroCusto().getIdCentroCusto())) {
                                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID de centro de custo informado inválido");
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    private ResponseEntity<Object> validacoesItensProposta(Proposta proposta, Integer idAnalista) {
        ResponseEntity<Object> validacaoProposta = validacoesProposta(proposta, idAnalista);

        if (validacaoProposta != null) {
            return validacaoProposta;
        }

        List<TabelaCusto> tabelasCustoProposta = proposta.getTabelasCustoProposta();

        if (tabelasCustoProposta != null) {
            if (tabelasCustoProposta.size() != 0) {
                for (TabelaCusto tabelaCusto : proposta.getTabelasCustoProposta()) {
                    if (!tabelaCustoService.existsById(tabelaCusto.getIdTabelaCusto())) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body("ID de tabela de custo inválido");
                    }

                    List<CentroCustoPagante> centroDeCustos = tabelaCusto.getCentrosCustoPagantes();

                    if (centroDeCustos != null && centroDeCustos.size() != 0) {
                        for (CentroCustoPagante centroCustoPagante : centroDeCustos) {
                            if (centroCustoPagante.getIdCentroCustoPagante() == null) {
                                if (centroDeCustos.size() != tabelaCustoService.findById(tabelaCusto.getIdTabelaCusto()).get().getCentrosCustoPagantes().size() + 1) {
                                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Se for adicionar um centro de custo pagante deverá atualizar as porcentagens das despesas de todos relacionados áquela proposta");
                                }
                            }
                        }
                    }
                }
            }
        }

        return null;
    }


}
