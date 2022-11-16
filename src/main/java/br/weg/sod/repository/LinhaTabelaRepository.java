package br.weg.sod.repository;

import br.weg.sod.model.entities.LinhaTabela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinhaTabelaRepository extends JpaRepository<LinhaTabela, Integer> {
}
