package br.weg.sod.model.service;

import br.weg.sod.model.entities.LinhaTabela;
import br.weg.sod.repository.LinhaTabelaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class LinhaTabelaSevice {

    private LinhaTabelaRepository linhaTabelaRepository;

    public List<LinhaTabela> findAll() {
        return linhaTabelaRepository.findAll();
    }

    public <S extends LinhaTabela> S save(S entity) {
        return linhaTabelaRepository.save(entity);
    }

    public Optional<LinhaTabela> findById(Integer integer) {
        return linhaTabelaRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return linhaTabelaRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        linhaTabelaRepository.deleteById(integer);
    }
}
