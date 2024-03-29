package br.weg.sade.service;

import br.weg.sade.model.entity.BU;
import br.weg.sade.repository.BURepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class BUService {

    private BURepository buRepository;

    public List<BU> findAll() {
        return buRepository.findAll();
    }

    public <S extends BU> S save(S entity) {
        return buRepository.save(entity);
    }

    public Optional<BU> findById(Integer integer) {
        return buRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return buRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        buRepository.deleteById(integer);
    }
}
