package br.weg.sade.repository;

import br.weg.sade.model.entity.DecisaoPropostaATA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DecisaoPropostaATARepository extends JpaRepository<DecisaoPropostaATA, Integer> {

    boolean existsByNumeroSequencial(Long numeroSequencial);
}
