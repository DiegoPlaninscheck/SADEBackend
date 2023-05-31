package br.weg.sade.repository;

import br.weg.sade.model.entity.BU;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BURepository extends JpaRepository<BU, Integer> {
}
