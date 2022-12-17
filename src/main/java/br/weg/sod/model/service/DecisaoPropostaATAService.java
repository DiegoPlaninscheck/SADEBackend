package br.weg.sod.model.service;

import br.weg.sod.model.entities.DecisaoPropostaATA;
import br.weg.sod.repository.DecisaoPropostaATARepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DecisaoPropostaATAService {

    private DecisaoPropostaATARepository decisaoPropostaATARepository;

    public List<DecisaoPropostaATA> findAll() {
        return decisaoPropostaATARepository.findAll();
    }

    public <S extends DecisaoPropostaATA> S save(S entity) {
        return decisaoPropostaATARepository.save(entity);
    }

    public Optional<DecisaoPropostaATA> findById(Integer integer) {
        return decisaoPropostaATARepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return decisaoPropostaATARepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        decisaoPropostaATARepository.deleteById(integer);
    }
}
