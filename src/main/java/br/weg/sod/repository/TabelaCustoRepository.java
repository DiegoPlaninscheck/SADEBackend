package br.weg.sod.repository;

import br.weg.sod.model.entities.TabelaCusto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TabelaCustoRepository extends JpaRepository<TabelaCusto, Integer> {
}
