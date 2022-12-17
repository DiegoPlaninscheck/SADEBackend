package br.weg.sod.repository;

import br.weg.sod.model.entities.DecisaoPropostaPauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DecisaoPropostaPautaRepository extends JpaRepository<DecisaoPropostaPauta, Integer> {

}
