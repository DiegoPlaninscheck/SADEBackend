package br.weg.sade.repository;

import br.weg.sade.model.entity.DecisaoPropostaPauta;
import br.weg.sade.model.entity.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DecisaoPropostaPautaRepository extends JpaRepository<DecisaoPropostaPauta, Integer> {

    boolean existsByProposta(Proposta proposta);

}
