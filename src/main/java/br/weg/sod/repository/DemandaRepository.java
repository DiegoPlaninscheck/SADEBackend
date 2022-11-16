package br.weg.sod.repository;

import br.weg.sod.model.entities.Demanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandaRepository extends JpaRepository<Demanda, Integer> {
}
