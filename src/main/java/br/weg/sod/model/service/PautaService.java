package br.weg.sod.model.service;

import br.weg.sod.model.entities.DecisaoPropostaPauta;
import br.weg.sod.model.entities.Pauta;
import br.weg.sod.model.entities.Proposta;
import br.weg.sod.repository.PautaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PautaService {
    private PautaRepository pautaRepository;

    public List<Pauta> findAll() {
        return pautaRepository.findAll();
    }

    public <S extends Pauta> S save(S entity) {
        return pautaRepository.save(entity);
    }

    public Optional<Pauta> findById(Integer integer) {
        return pautaRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return pautaRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        pautaRepository.deleteById(integer);
    }

    public List<Pauta> findByProposta(Proposta proposta){
        List<Pauta> listaPautasContemProposta = new ArrayList<>();

        for(Pauta pauta : findAll()){
            for(DecisaoPropostaPauta decisaoPropostaPauta : pauta.getPropostasPauta()){
                if(decisaoPropostaPauta.getProposta().getIdProposta() == proposta.getIdProposta()){
                    listaPautasContemProposta.add(pauta);
                    break;
                }
            }
        }

        return listaPautasContemProposta;
    }
}
