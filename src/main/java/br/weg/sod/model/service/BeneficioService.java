package br.weg.sod.model.service;

import br.weg.sod.model.entities.Beneficio;
import br.weg.sod.model.entities.Demanda;
import br.weg.sod.repository.BeneficioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class BeneficioService {

    private BeneficioRepository beneficioRepository;

    public List<Beneficio> findAll() {
        return beneficioRepository.findAll();
    }

    public <S extends Beneficio> S save(S entity) {
        return beneficioRepository.save(entity);
    }

    public Optional<Beneficio> findById(Integer integer) {
        return beneficioRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return beneficioRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        beneficioRepository.deleteById(integer);
    }

    public List<Beneficio> findByDemanda(Demanda demanda) {
        return beneficioRepository.findByDemanda(demanda);
    }
}
