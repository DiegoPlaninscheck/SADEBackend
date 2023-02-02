package br.weg.sod.model.service;

import br.weg.sod.dto.DecisaoPropostaATADTO;
import br.weg.sod.model.entities.DecisaoPropostaATA;
import br.weg.sod.model.entities.DecisaoPropostaPauta;
import br.weg.sod.repository.DecisaoPropostaATARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DecisaoPropostaATAService {

    @Autowired
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

    public boolean existsByNumeroSequencial(Long numeroSequencial) {
        return decisaoPropostaATARepository.existsByNumeroSequencial(numeroSequencial);
    }

    public boolean decisoesValidas(List<DecisaoPropostaPauta> propostasPauta, List<DecisaoPropostaATADTO> propostasAta) {

        //vê se o número sequencial informado já existe ou está repetido na lista
        for(DecisaoPropostaATADTO decisaoATADTO : propostasAta){
            if(existsByNumeroSequencial(decisaoATADTO.getNumeroSequencial())){
                return false;
            }

            for(DecisaoPropostaATADTO decisaoATADTOconferir : propostasAta){
                if(decisaoATADTOconferir.getNumeroSequencial() == decisaoATADTO.getNumeroSequencial() && decisaoATADTOconferir.getProposta().equals(decisaoATADTO.getProposta())){
                    return false;
                }
            }
        }

        //vê se as propostas informadas são as mesmas apreciadas pela pauta conectada a ata
        for(DecisaoPropostaATADTO decisaoATADTO : propostasAta){
            boolean existe = false;

            for(DecisaoPropostaPauta decisaoPauta : propostasPauta){
                if(decisaoPauta.getProposta().getIdProposta() == decisaoATADTO.getProposta().getIdProposta()){
                    existe = true;
                }
            }

            if(!existe){
                return false;
            }
        }

        return true;
    }

}
