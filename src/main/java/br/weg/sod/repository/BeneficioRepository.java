package br.weg.sod.repository;

import br.weg.sod.model.entities.Beneficio;
import br.weg.sod.model.entities.Demanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeneficioRepository extends JpaRepository<Beneficio, Integer> {

    public List<Beneficio> findByDemanda(Demanda demanda);
}
