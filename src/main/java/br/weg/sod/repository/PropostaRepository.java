package br.weg.sod.repository;

import br.weg.sod.model.entities.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropostaRepository extends JpaRepository<Proposta, Integer> {

    List<Proposta> findByEstaEmPauta(boolean estaEmPauta);
}
