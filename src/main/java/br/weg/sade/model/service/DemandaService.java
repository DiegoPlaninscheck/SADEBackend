package br.weg.sade.model.service;

import br.weg.sade.model.entities.Demanda;
import br.weg.sade.model.entities.Usuario;
import br.weg.sade.model.entities.enuns.StatusDemanda;
import br.weg.sade.repository.DemandaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class DemandaService {

    private DemandaRepository demandaRepository;

    public List<Demanda> findAll() {
        return demandaRepository.findAll();
    }

    public <S extends Demanda> S save(S entity) {
        return demandaRepository.save(entity);
    }

    public Optional<Demanda> findById(Integer integer) {
        return demandaRepository.findById(integer);
    }

    public List<Demanda> findDemandaByPertenceUmaProposta(boolean pertenceUmaProposta) {
        return demandaRepository.findDemandaByPertenceUmaProposta(pertenceUmaProposta);
    }

    public List<Demanda> findDemandasByUsuario(Usuario usuario) {
        return demandaRepository.findDemandasByUsuario(usuario);
    }

    public List<Demanda> findDemandasByRascunho(boolean rascunho) {
        return demandaRepository.findDemandasByRascunho(rascunho);
    }

    public List<Demanda> findDemandasByStatusDemanda(StatusDemanda statusDemanda) {
        return demandaRepository.findDemandasByStatusDemanda(statusDemanda);
    }

    public boolean existsById(Integer integer) {
        return demandaRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        demandaRepository.deleteById(integer);
    }

}
