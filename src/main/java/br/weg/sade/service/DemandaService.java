package br.weg.sade.service;

import br.weg.sade.model.entity.Demanda;
import br.weg.sade.model.entity.Usuario;
import br.weg.sade.model.enums.StatusDemanda;
import br.weg.sade.repository.DemandaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<Demanda> findByDepartamento(String departamento) {
        List<Demanda> demandasPossiveis = demandaRepository.findDemandasByRascunho(false), demandasDepartamento = new ArrayList<>();

        for(Demanda demanda : demandasPossiveis){
            if(demanda.getUsuario().getDepartamento().equals(departamento)){
                demandasDepartamento.add(demanda);
            }
        }

        return demandasDepartamento;
    }
}
