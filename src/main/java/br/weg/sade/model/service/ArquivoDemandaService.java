package br.weg.sade.model.service;

import br.weg.sade.model.entities.ArquivoDemanda;
import br.weg.sade.repository.ArquivoDemandaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ArquivoDemandaService {

    private ArquivoDemandaRepository arquivoDemandaRepository;

    public List<ArquivoDemanda> findAll() {
        return arquivoDemandaRepository.findAll();
    }

    public <S extends ArquivoDemanda> S save(S entity) {
        return arquivoDemandaRepository.save(entity);
    }

    public Optional<ArquivoDemanda> findById(Integer integer) {
        return arquivoDemandaRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return arquivoDemandaRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        arquivoDemandaRepository.deleteById(integer);
    }

}
