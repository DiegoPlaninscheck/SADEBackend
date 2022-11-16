package br.weg.sod.repository;

import br.weg.sod.model.entities.BU;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BURepository extends JpaRepository<BU, Integer> {
}
