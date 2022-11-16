package br.weg.sod.repository;

import br.weg.sod.model.entities.DecisaoProposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DecisaoPropostaRepository extends JpaRepository<DecisaoProposta, Integer> {
}
