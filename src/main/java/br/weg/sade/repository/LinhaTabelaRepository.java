package br.weg.sade.repository;

import br.weg.sade.model.entities.LinhaTabela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinhaTabelaRepository extends JpaRepository<LinhaTabela, Integer> {
}
