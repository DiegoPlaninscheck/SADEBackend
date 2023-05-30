package br.weg.sade.repository;

import br.weg.sade.model.entities.ArquivoDemanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArquivoDemandaRepository extends JpaRepository<ArquivoDemanda, Integer> {

}
