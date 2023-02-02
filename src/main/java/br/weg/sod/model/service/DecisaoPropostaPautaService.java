package br.weg.sod.model.service;

import br.weg.sod.model.entities.ATA;
import br.weg.sod.model.entities.DecisaoPropostaPauta;
import br.weg.sod.model.entities.Proposta;
import br.weg.sod.repository.DecisaoPropostaPautaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class DecisaoPropostaPautaService {

    private DecisaoPropostaPautaRepository decisaoPropostaPautaRepository;

    public List<DecisaoPropostaPauta> findAll() {
        return decisaoPropostaPautaRepository.findAll();
    }

    public <S extends DecisaoPropostaPauta> S save(S entity) {
        return decisaoPropostaPautaRepository.save(entity);
    }

    public Optional<DecisaoPropostaPauta> findById(Integer integer) {
        return decisaoPropostaPautaRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return decisaoPropostaPautaRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        decisaoPropostaPautaRepository.deleteById(integer);
    }

    public boolean existsByProposta(Proposta proposta) {
        return decisaoPropostaPautaRepository.existsByProposta(proposta);
    }

    public List<DecisaoPropostaPauta> findByProposta(Proposta proposta) {
        return decisaoPropostaPautaRepository.findByProposta(proposta);
    }
}
