package br.weg.sod.model.service;

import br.weg.sod.model.entities.CentroCustoDemanda;
import br.weg.sod.repository.CentroCustoDemandaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CentroCustoDemandaService {

    private CentroCustoDemandaRepository centroCustoDemandaRepository;

    public List<CentroCustoDemanda> findAll() {
        return centroCustoDemandaRepository.findAll();
    }

    public <S extends CentroCustoDemanda> S save(S entity) {
        return centroCustoDemandaRepository.save(entity);
    }

    public Optional<CentroCustoDemanda> findById(Integer integer) {
        return centroCustoDemandaRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return centroCustoDemandaRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        centroCustoDemandaRepository.deleteById(integer);
    }
}
