package br.weg.sod.model.service;

import br.weg.sod.model.entities.CentroCusto;
import br.weg.sod.repository.CentroCustoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CentroCustoService {

    private CentroCustoRepository centroCustoRepository;

    public List<CentroCusto> findAll() {
        return centroCustoRepository.findAll();
    }

    public <S extends CentroCusto> S save(S entity) {
        return centroCustoRepository.save(entity);
    }

    public Optional<CentroCusto> findById(Integer integer) {
        return centroCustoRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return centroCustoRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        centroCustoRepository.deleteById(integer);
    }

    public boolean validarCentrosCusto(List<CentroCusto> centroCustoDemanda) {
        for(CentroCusto centroCusto : centroCustoDemanda){
            if(!existsById(centroCusto.getIdCentroCusto())){
                return false;
            }
        }

        return true;
    }
}
