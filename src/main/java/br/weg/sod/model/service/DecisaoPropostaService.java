package br.weg.sod.model.service;

import br.weg.sod.model.entities.ATA;
import br.weg.sod.model.entities.DecisaoProposta;
import br.weg.sod.repository.DecisaoPropostaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class DecisaoPropostaService {

    private DecisaoPropostaRepository decisaoPropostaRepository;

    public List<DecisaoProposta> findAll() {
        return decisaoPropostaRepository.findAll();
    }

    public <S extends DecisaoProposta> S save(S entity) {
        return decisaoPropostaRepository.save(entity);
    }

    public Optional<DecisaoProposta> findById(Integer integer) {
        return decisaoPropostaRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return decisaoPropostaRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        decisaoPropostaRepository.deleteById(integer);
    }

    public List<DecisaoProposta> findByATA(ATA ATA) {
        return decisaoPropostaRepository.findByATA(ATA);
    }
}
