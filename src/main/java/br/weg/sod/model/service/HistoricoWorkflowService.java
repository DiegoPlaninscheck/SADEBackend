package br.weg.sod.model.service;

import br.weg.sod.dto.HistoricoWorkflowCriacaoDTO;
import br.weg.sod.dto.HistoricoWorkflowEdicaoDTO;
import br.weg.sod.model.entities.*;
import br.weg.sod.model.entities.enuns.StatusHistorico;
import br.weg.sod.model.entities.enuns.Tarefa;
import br.weg.sod.repository.HistoricoWorkflowRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class HistoricoWorkflowService {

    private HistoricoWorkflowRepository historicoWorkflowRepository;

    //funções básicas
    public List<HistoricoWorkflow> findAll() {
        return historicoWorkflowRepository.findAll();
    }

    public <S extends HistoricoWorkflow> S save(S entity) {
        return historicoWorkflowRepository.save(entity);
    }

    public Optional<HistoricoWorkflow> findById(Integer integer) {
        return historicoWorkflowRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return historicoWorkflowRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        historicoWorkflowRepository.deleteById(integer);
    }

    public List<HistoricoWorkflow> findByDemanda(Demanda demanda) {
        return historicoWorkflowRepository.findByDemanda(demanda);
    }

    public HistoricoWorkflow findLastHistoricoByDemanda(Demanda demanda) {
        List<HistoricoWorkflow> listHistorico = findByDemanda(demanda);
        return listHistorico.get(listHistorico.size() - 1);
    }

    public HistoricoWorkflow findLastHistoricoCompletedByDemanda(Demanda demanda) {
        List<HistoricoWorkflow> listHistorico = findByDemanda(demanda);
        for (int i = listHistorico.size() - 1; i >= 0; i--) {
            if (listHistorico.get(i).getStatus() == StatusHistorico.CONCLUIDO) {
                return listHistorico.get(i);
            }
        }

        return null;
    }

    public AnalistaTI findLastHistoricoAnalistaByDemanda(Demanda demanda) {
        List<HistoricoWorkflow> listHistorico = findByDemanda(demanda);
        for (int i = listHistorico.size() - 1; i >= 0; i--) {
            if (listHistorico.get(i).getUsuario() instanceof AnalistaTI) {
                return (AnalistaTI) listHistorico.get(i).getUsuario();
            }
        }

        return null;
    }


    //validações
    public ResponseEntity<Object> validaCriacaoHistorico(HistoricoWorkflowCriacaoDTO historicoWorkflowDTO, Usuario usuarioProximoHistorico) {
        Tarefa tarefaNova = historicoWorkflowDTO.getTarefa();
        ResponseEntity<Object> historicoValido = null;

        if (tarefaNova == Tarefa.REENVIARDEMANDA) {
            historicoValido = validarHistoricoDevolucao(historicoWorkflowDTO.getAcaoFeitaHistoricoAnterior(), historicoWorkflowDTO.getMotivoDevolucaoAnterior());
        }

        if (tarefaNova == Tarefa.CLASSIFICARDEMANDA) {
            historicoValido = validarHistoricoAprovado(historicoWorkflowDTO.getAcaoFeitaHistoricoAnterior());
        }

        if (tarefaNova == Tarefa.ADICIONARINFORMACOESDEMANDA) {
            historicoValido = validarHistoricoAprovado(historicoWorkflowDTO.getAcaoFeitaHistoricoAnterior());
        }

        if (tarefaNova == Tarefa.AVALIARWORKFLOW) {
            historicoValido = validarHistoricoWorkflowAprovado(historicoWorkflowDTO.getAcaoFeitaHistoricoAnterior(), usuarioProximoHistorico);
        }

        if (tarefaNova == Tarefa.CRIARPAUTA) {
            historicoValido = validarHistoricoWorkflowReprovado(historicoWorkflowDTO.getAcaoFeitaHistoricoAnterior(), historicoWorkflowDTO.getMotivoDevolucaoAnterior(), usuarioProximoHistorico);
        }

        return historicoValido;
    }

    public ResponseEntity<Object> validaEdicaoHistorico(HistoricoWorkflowEdicaoDTO historicoWorkflowDTO, HistoricoWorkflow historicoWorkflowBD) {
        Tarefa tarefaFeita = historicoWorkflowDTO.getAcaoFeita();
        ResponseEntity<Object> historicoValido = null;

        if (tarefaFeita == Tarefa.REPROVARDEMANDA) {
            historicoValido = validarHistoricoReprovado(historicoWorkflowBD.getUsuario(), historicoWorkflowDTO.getMotivoDevolucao());
        }

        return historicoValido;
    }

    private ResponseEntity<Object> validarHistoricoDevolucao(Tarefa acaoAnterior, String motivoDevolucaoAnterior) {
        if (acaoAnterior != Tarefa.DEVOLVERDEMANDA) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Ação de histórico anterior inválida");
        }

        if (motivoDevolucaoAnterior == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Status da ação anterior necessita de um motivo de devolução");
        }

        return null;
    }

    private ResponseEntity<Object> validarHistoricoReprovado(Usuario usuario, String motivoDevolucao) {
        if (usuario instanceof GerenteNegocio) {
            if (motivoDevolucao == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Essa ação necessita de um motivo de devolução");
            }
        }

        return null;
    }

    private ResponseEntity<Object> validarHistoricoAprovado(Tarefa acaoFeitaHistoricoAnterior) {
        if (acaoFeitaHistoricoAnterior != Tarefa.APROVARDEMANDA) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Status da ação anterior inválido para ação do próximo histórico");
        }

        return null;
    }

    private ResponseEntity<Object> validarHistoricoWorkflowAprovado(Tarefa acaoFeitaHistoricoAnterior, Usuario usuario) {
        if (acaoFeitaHistoricoAnterior != Tarefa.APROVARWORKFLOW) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Status da ação anterior inválido para ação do próximo histórico");
        }

        if (!(usuario instanceof GerenteTI)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Usuário responsável não pode ser encarregado dessa ação");
        }

        return null;
    }

    private ResponseEntity<Object> validarHistoricoWorkflowReprovado(Tarefa acaoFeitaHistoricoAnterior, String motivoDevolucaoAnterior, Usuario usuario) {
        if (acaoFeitaHistoricoAnterior != Tarefa.REPROVARWORKFLOW) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Status da ação anterior inválido para ação do próximo histórico");
        }

        if (motivoDevolucaoAnterior == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Essa ação necessita de um motivo de devolução");
        }

        if (!(usuario instanceof AnalistaTI)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Usuário responsável não pode ser encarregado dessa ação");
        }

        return null;
    }


    //processos
    public void finishHistoricoByDemanda(Demanda demanda, Tarefa acaoFeita, Usuario usuarioResponsavel, String motivoDevolucao, MultipartFile versaoPDF) throws IOException {
        HistoricoWorkflow historicoWorkflowVelho = findLastHistoricoByDemanda(demanda);

        historicoWorkflowVelho.setConclusaoTarefa(new Timestamp(new Date().getTime()));
        historicoWorkflowVelho.setStatus(StatusHistorico.CONCLUIDO);
        historicoWorkflowVelho.setAcaoFeita(acaoFeita);
        historicoWorkflowVelho.setUsuario(usuarioResponsavel);
        historicoWorkflowVelho.setMotivoDevolucao(motivoDevolucao);
        if (versaoPDF != null) {
            historicoWorkflowVelho.setArquivoHistoricoWorkflow(new ArquivoHistoricoWorkflow(versaoPDF));
        }

        save(historicoWorkflowVelho);
    }

    public void initializeHistoricoByDemanda(Timestamp recebimento, Tarefa tarefa, StatusHistorico statusHistorico, Usuario usuario, Demanda demanda) {
        Timestamp prazo = new Timestamp(recebimento.getTime() + 86400000 * 5);

        save(new HistoricoWorkflow(recebimento, prazo, tarefa, statusHistorico, usuario, demanda));
    }

//
//    public List<HistoricoWorkflow> findByProposta(Proposta proposta) {
//        return findByDemanda(proposta.getDemanda());
//    }
//
//    public HistoricoWorkflow findLastHistoricoByProposta(Proposta proposta) {
//        List<HistoricoWorkflow> listHistorico = findByProposta(proposta);
//        return listHistorico.get(listHistorico.size() - 1);
//    }
//
//

//
//    public void finishHistoricoByDemanda(Demanda demanda, Tarefa acaoFeita,Usuario usuario) {
//        HistoricoWorkflow historicoWorkflowVelho = findLastHistoricoByDemanda(demanda);
//
//        historicoWorkflowVelho.setConclusaoTarefa(new Timestamp(new Date().getTime()));
//        historicoWorkflowVelho.setStatus(StatusHistorico.CONCLUIDO);
//        historicoWorkflowVelho.setAcaoFeita(acaoFeita);
//        historicoWorkflowVelho.setUsuario(usuario);
//        //dar um jeito no pdf
//
//        save(historicoWorkflowVelho);
//    }
//
//    public void initializeHistoricoByProposta(Timestamp recebimento, Tarefa tarefa, StatusHistorico statusHistorico, Usuario usuario, Proposta proposta) {
//        initializeHistoricoByDemanda(recebimento, tarefa, statusHistorico, usuario, proposta.getDemanda());
//    }
//
//    public void finishHistoricoByProposta(Proposta proposta, Tarefa acaoFeita){
//        finishHistoricoByDemanda(proposta.getDemanda(), acaoFeita, (ArquivoHistoricoWorkflow) null);
//    }

}
