package br.weg.sod.model.service;

import br.weg.sod.model.entities.CentroCustoPagante;
import br.weg.sod.repository.CentroCustoPaganteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CentroCustoPaganteService {

    private CentroCustoPaganteRepository centroCustoPaganteRepository;

    public List<CentroCustoPagante> findAll() {
        return centroCustoPaganteRepository.findAll();
    }

    public <S extends CentroCustoPagante> S save(S entity) {
        return centroCustoPaganteRepository.save(entity);
    }

    public Optional<CentroCustoPagante> findById(Integer integer) {
        return centroCustoPaganteRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return centroCustoPaganteRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        centroCustoPaganteRepository.deleteById(integer);
    }
}
