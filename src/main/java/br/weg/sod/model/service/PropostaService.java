package br.weg.sod.model.service;

import br.weg.sod.model.entities.Proposta;
import br.weg.sod.repository.PropostaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PropostaService {

    private PropostaRepository propostaRepository;

    public List<Proposta> findAll() {
        return propostaRepository.findAll();
    }

    public <S extends Proposta> S save(S entity) {
        return propostaRepository.save(entity);
    }

    public Optional<Proposta> findById(Integer integer) {
        return propostaRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return propostaRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        propostaRepository.deleteById(integer);
    }
}
