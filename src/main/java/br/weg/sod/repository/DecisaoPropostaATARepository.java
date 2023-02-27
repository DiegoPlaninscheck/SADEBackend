package br.weg.sod.repository;

import br.weg.sod.model.entities.DecisaoPropostaATA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DecisaoPropostaATARepository extends JpaRepository<DecisaoPropostaATA, Integer> {

    boolean existsByNumeroSequencial(Long numeroSequencial);
}
