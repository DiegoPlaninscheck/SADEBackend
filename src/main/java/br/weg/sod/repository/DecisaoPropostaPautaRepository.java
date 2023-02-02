package br.weg.sod.repository;

import br.weg.sod.model.entities.DecisaoPropostaPauta;
import br.weg.sod.model.entities.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DecisaoPropostaPautaRepository extends JpaRepository<DecisaoPropostaPauta, Integer> {

    boolean existsByProposta(Proposta proposta);

    List<DecisaoPropostaPauta> findByProposta(Proposta proposta);
}
