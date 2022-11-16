package br.weg.sod.repository;

import br.weg.sod.model.entities.ArquivoDemanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArquivoDemandaRepository extends JpaRepository<ArquivoDemanda, Integer> {
}
