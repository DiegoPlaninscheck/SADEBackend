package br.weg.sade.repository;

import br.weg.sade.model.entity.ArquivoPauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArquivoPautaRepository  extends JpaRepository<ArquivoPauta, Integer> {
}
