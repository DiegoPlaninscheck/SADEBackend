package br.weg.sod.repository;

import br.weg.sod.model.entities.CentroCustoPagante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CentroCustoPaganteRepository extends JpaRepository<CentroCustoPagante, Integer> {
}
