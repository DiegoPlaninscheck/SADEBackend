package br.weg.sod.repository;

import br.weg.sod.model.entities.HistoricoWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoWorkflowRepository extends JpaRepository<HistoricoWorkflow, Integer> {
}
