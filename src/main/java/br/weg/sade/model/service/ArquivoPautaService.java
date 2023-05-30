package br.weg.sade.model.service;

import br.weg.sade.model.entities.ArquivoPauta;
import br.weg.sade.repository.ArquivoPautaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArquivoPautaService {

    private ArquivoPautaRepository arquivoPautaRepository;

    public List<ArquivoPauta> findAll() {
        return arquivoPautaRepository.findAll();
    }

    public <S extends ArquivoPauta> S save(S entity) {
        return arquivoPautaRepository.save(entity);
    }

    public Optional<ArquivoPauta> findById(Integer integer) {
        return arquivoPautaRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return arquivoPautaRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        arquivoPautaRepository.deleteById(integer);
    }
}
