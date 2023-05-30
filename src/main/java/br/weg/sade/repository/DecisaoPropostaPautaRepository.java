package br.weg.sade.repository;

import br.weg.sade.model.entities.DecisaoPropostaPauta;
import br.weg.sade.model.entities.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DecisaoPropostaPautaRepository extends JpaRepository<DecisaoPropostaPauta, Integer> {

    boolean existsByProposta(Proposta proposta);

}
