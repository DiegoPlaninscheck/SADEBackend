package br.weg.sade.model.service;

import br.weg.sade.model.entities.*;
import br.weg.sade.model.entities.enuns.StatusDemanda;
import br.weg.sade.repository.DecisaoPropostaPautaRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class DecisaoPropostaPautaService {

    @Autowired
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

    public List<DecisaoPropostaPauta> createDecisaoPropostaWorkflow(List<Proposta> propostasAprovadasWorkflow) {
        List<DecisaoPropostaPauta> decisoesPauta = new ArrayList<>();

        for(Proposta proposta : propostasAprovadasWorkflow){
            DecisaoPropostaPauta decisaoPropostaPauta = new DecisaoPropostaPauta(StatusDemanda.TODO, false, "", proposta);
            decisoesPauta.add(decisaoPropostaPauta);
        }

        return decisoesPauta;
    }

}
