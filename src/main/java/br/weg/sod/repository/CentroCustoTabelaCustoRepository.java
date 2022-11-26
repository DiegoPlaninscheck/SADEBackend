package br.weg.sod.repository;

import br.weg.sod.model.entities.CentroCustoTabelaCusto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CentroCustoTabelaCustoRepository extends JpaRepository<CentroCustoTabelaCusto, Integer> {
}
