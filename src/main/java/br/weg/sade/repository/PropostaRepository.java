package br.weg.sade.repository;

import br.weg.sade.model.entity.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropostaRepository extends JpaRepository<Proposta, Integer> {

    List<Proposta> findByEstaEmPauta(boolean estaEmPauta);
}
