package br.weg.sade.model.service;

import br.weg.sade.model.entities.Chat;
import br.weg.sade.model.entities.Mensagem;
import br.weg.sade.repository.MensagemRepository;
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

    public List<Mensagem> findMensagemsByChat(Chat chat) {
        return mensagemRepository.findMensagemsByChat(chat);
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
