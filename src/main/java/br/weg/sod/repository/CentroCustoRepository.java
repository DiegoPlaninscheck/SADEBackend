package br.weg.sod.repository;

import br.weg.sod.model.entities.CentroCusto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CentroCustoRepository extends JpaRepository<CentroCusto, Integer> {
}
