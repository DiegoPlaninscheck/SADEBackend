package br.weg.sade.repository;

import br.weg.sade.model.entity.TabelaCusto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TabelaCustoRepository extends JpaRepository<TabelaCusto, Integer> {
}
