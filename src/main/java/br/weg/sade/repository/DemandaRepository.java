package br.weg.sade.repository;

import br.weg.sade.model.entities.Demanda;
import br.weg.sade.model.entities.Usuario;
import br.weg.sade.model.entities.enuns.StatusDemanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandaRepository extends JpaRepository<Demanda, Integer> {

    List<Demanda> findDemandasByUsuario(Usuario usuario);

    List<Demanda> findDemandasByRascunho(boolean rascunho);

    List<Demanda> findDemandasByStatusDemanda(StatusDemanda statusDemanda);

    List<Demanda> findDemandaByPertenceUmaProposta(boolean pertenceUmaProposta);
}
