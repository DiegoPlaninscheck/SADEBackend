package br.weg.sade.repository;

import br.weg.sade.model.entities.CentroCustoPagante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CentroCustoPaganteRepository extends JpaRepository<CentroCustoPagante, Integer> {
}
