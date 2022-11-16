package br.weg.sod.model.service;

import br.weg.sod.model.entities.Chat;
import br.weg.sod.repository.ChatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ChatService {

    private ChatRepository chatRepository;

    public List<Chat> findAll() {
        return chatRepository.findAll();
    }

    public <S extends Chat> S save(S entity) {
        return chatRepository.save(entity);
    }

    public Optional<Chat> findById(Integer integer) {
        return chatRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return chatRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        chatRepository.deleteById(integer);
    }
}
