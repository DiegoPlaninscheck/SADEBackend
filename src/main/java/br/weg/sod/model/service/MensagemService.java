package br.weg.sod.model.service;

import br.weg.sod.model.entities.Mensagem;
import br.weg.sod.repository.MensagemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class MensagemService {

    private MensagemRepository mensagemRepository;

    public List<Mensagem> findAll() {
        return mensagemRepository.findAll();
    }

    public <S extends Mensagem> S save(S entity) {
        return mensagemRepository.save(entity);
    }

    public Optional<Mensagem> findById(Integer integer) {
        return mensagemRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return mensagemRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        mensagemRepository.deleteById(integer);
    }
}
