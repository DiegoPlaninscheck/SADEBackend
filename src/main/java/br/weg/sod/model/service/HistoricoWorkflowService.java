package br.weg.sod.model.service;

import br.weg.sod.model.entities.HistoricoWorkflow;
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
}
