package br.weg.sod.repository;

import br.weg.sod.model.entities.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, Integer> {

    List<Pauta> findPautasByPertenceUmaATA(boolean pertenceUmaATA);
}
