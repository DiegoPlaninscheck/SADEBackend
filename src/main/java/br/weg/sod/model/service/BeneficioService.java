package br.weg.sod.model.service;

import br.weg.sod.model.entities.Beneficio;
import br.weg.sod.model.entities.Demanda;
import br.weg.sod.model.entities.enuns.Moeda;
import br.weg.sod.model.entities.enuns.TipoBeneficio;
import br.weg.sod.repository.BeneficioRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class BeneficioService {

    private BeneficioRepository beneficioRepository;

    public List<Beneficio> findAll() {
        return beneficioRepository.findAll();
    }

    public <S extends Beneficio> S save(S entity) {
        return beneficioRepository.save(entity);
    }

    public Optional<Beneficio> findById(Integer integer) {
        return beneficioRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return beneficioRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        beneficioRepository.deleteById(integer);
    }

    /**
     * Verifica se um benefício é válido de acrodo com o seu tipo. Se retornar:
     * 0: Tudo certo
     * 1: Um benefício QUALITATIVO está com atributos qe não deveria
     * 2: Um benefício REAL ou POTENCIAL está faltando atributos
     *
     * @param beneficio
     * @return
     */
    public void beneficioValido(Beneficio beneficio){
        TipoBeneficio tipoBeneficio = beneficio.getTipoBeneficio();
        Moeda moedaBeneficio = beneficio.getMoeda();
        Double valor = beneficio.getValor();

        if(tipoBeneficio == TipoBeneficio.QUALITATIVO && (moedaBeneficio != null || valor != null)) {
            throw new RuntimeException("Benefícios do tipo QUALITATIVO não aceitam valores de 'moeda' e 'valor'");
        } else if((tipoBeneficio == TipoBeneficio.POTENCIAL|| tipoBeneficio == TipoBeneficio.REAL) && (moedaBeneficio == null || valor == null)){
            throw new RuntimeException("Benefícios do tipo POTENCIAL ou REAL necessitam de ter os campos 'moeda' e 'valor'");
        }
    }

    public void checarBeneficios(List<Beneficio> beneficiosDemanda){
        for(Beneficio beneficio : beneficiosDemanda){
            beneficioValido(beneficio);
        }
    }

}
