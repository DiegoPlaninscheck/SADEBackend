package br.weg.sade.repository;

import br.weg.sade.model.entities.ATA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ATARepository extends JpaRepository<ATA, Integer> {

    boolean existsByNumeroDG(Long numeroDG);
}
