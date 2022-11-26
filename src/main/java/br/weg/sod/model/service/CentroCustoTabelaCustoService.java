package br.weg.sod.model.service;

import br.weg.sod.model.entities.CentroCustoTabelaCusto;
import br.weg.sod.repository.CentroCustoTabelaCustoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CentroCustoTabelaCustoService {

    private CentroCustoTabelaCustoRepository centroCustoTabelaCustoRepository;

    public List<CentroCustoTabelaCusto> findAll() {
        return centroCustoTabelaCustoRepository.findAll();
    }

    public <S extends CentroCustoTabelaCusto> S save(S entity) {
        return centroCustoTabelaCustoRepository.save(entity);
    }

    public Optional<CentroCustoTabelaCusto> findById(Integer integer) {
        return centroCustoTabelaCustoRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return centroCustoTabelaCustoRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        centroCustoTabelaCustoRepository.deleteById(integer);
    }
}
