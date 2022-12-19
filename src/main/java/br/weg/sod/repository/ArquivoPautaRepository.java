package br.weg.sod.repository;

import br.weg.sod.model.entities.ArquivoPauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArquivoPautaRepository  extends JpaRepository<ArquivoPauta, Integer> {
}
