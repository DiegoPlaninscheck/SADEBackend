package br.weg.sade.repository;

import br.weg.sade.model.entity.Demanda;
import br.weg.sade.model.entity.HistoricoWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoWorkflowRepository extends JpaRepository<HistoricoWorkflow, Integer> {

    List<HistoricoWorkflow> findHistoricoWorkflowByDemanda(Demanda demanda);
}
