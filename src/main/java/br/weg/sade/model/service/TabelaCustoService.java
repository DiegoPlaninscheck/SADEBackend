package br.weg.sade.model.service;

import br.weg.sade.model.entities.TabelaCusto;
import br.weg.sade.repository.TabelaCustoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class TabelaCustoService {
    private TabelaCustoRepository tabelaCustoRepository;

    public List<TabelaCusto> findAll() {
        return tabelaCustoRepository.findAll();
    }

    public <S extends TabelaCusto> S save(S entity) {
        return tabelaCustoRepository.save(entity);
    }

    public Optional<TabelaCusto> findById(Integer integer) {
        return tabelaCustoRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return tabelaCustoRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        tabelaCustoRepository.deleteById(integer);
    }
}
