package br.weg.sod.model.service;

import br.weg.sod.model.entities.Demanda;
import br.weg.sod.model.entities.HistoricoWorkflow;
import br.weg.sod.model.entities.Proposta;
import br.weg.sod.model.entities.enuns.Tarefa;
import br.weg.sod.repository.HistoricoWorkflowRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class HistoricoWorkflowService {

    private HistoricoWorkflowRepository historicoWorkflowRepository;

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

    public List<HistoricoWorkflow> findByProposta(Proposta proposta){
        return findByDemanda(proposta.getDemanda());
    }

    public HistoricoWorkflow findLastHistoricoByProposta(Proposta proposta){
        List<HistoricoWorkflow> listHistorico = findByProposta(proposta);
        return listHistorico.get(listHistorico.size() - 1);
    }

    public boolean foiAvaliado(Proposta proposta) {
        List<HistoricoWorkflow> listHistorico = findByProposta(proposta);
        for(HistoricoWorkflow historicoWorkflow : listHistorico){
            if(historicoWorkflow.getTarefa() == Tarefa.AVALIARWORKFLOW){
                return true;
            }
        }

        return false;
    }
}
