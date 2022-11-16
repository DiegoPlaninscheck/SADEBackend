package br.weg.sod.repository;

import br.weg.sod.model.entities.ATA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ATARepository extends JpaRepository<ATA, Integer> {
}
