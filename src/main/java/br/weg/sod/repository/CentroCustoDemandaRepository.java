package br.weg.sod.repository;

import br.weg.sod.model.entities.CentroCustoDemanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CentroCustoDemandaRepository extends JpaRepository<CentroCustoDemanda, Integer> {
}
