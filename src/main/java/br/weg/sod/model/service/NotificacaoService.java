package br.weg.sod.model.service;

import br.weg.sod.model.entities.Notificacao;
import br.weg.sod.repository.NotificacaoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class NotificacaoService {
    private NotificacaoRepository notificacaoRepository;

    public List<Notificacao> findAll() {
        return notificacaoRepository.findAll();
    }

    public <S extends Notificacao> S save(S entity) {
        return notificacaoRepository.save(entity);
    }

    public Optional<Notificacao> findById(Integer integer) {
        return notificacaoRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return notificacaoRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        notificacaoRepository.deleteById(integer);
    }
}
