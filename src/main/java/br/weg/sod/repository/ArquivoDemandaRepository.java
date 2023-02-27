package br.weg.sod.repository;

import br.weg.sod.model.entities.ArquivoDemanda;
import br.weg.sod.model.entities.Demanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArquivoDemandaRepository extends JpaRepository<ArquivoDemanda, Integer> {

}
