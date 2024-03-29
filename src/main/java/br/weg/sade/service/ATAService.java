package br.weg.sade.service;

import br.weg.sade.model.entity.ATA;
import br.weg.sade.repository.ATARepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ATAService {

    private ATARepository ataRepository;

    public List<ATA> findAll() {
        return ataRepository.findAll();
    }

    public <S extends ATA> S save(S entity) {
        return ataRepository.save(entity);
    }

    public Optional<ATA> findById(Integer integer) {
        return ataRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return ataRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        ataRepository.deleteById(integer);
    }

    public boolean existsByNumeroDG(Long numeroDG) {
        return ataRepository.existsByNumeroDG(numeroDG);
    }
}
