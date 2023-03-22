package br.weg.sod.repository;

import br.weg.sod.model.entities.Demanda;
import br.weg.sod.model.entities.HistoricoWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoWorkflowRepository extends JpaRepository<HistoricoWorkflow, Integer> {

    List<HistoricoWorkflow> findHistoricoWorkflowByDemanda(Demanda demanda);
}
