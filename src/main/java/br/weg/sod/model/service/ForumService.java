package br.weg.sod.model.service;

import br.weg.sod.model.entities.Forum;
import br.weg.sod.repository.ForumRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ForumService {

    private ForumRepository forumRepository;

    public List<Forum> findAll() {
        return forumRepository.findAll();
    }

    public <S extends Forum> S save(S entity) {
        return forumRepository.save(entity);
    }

    public Optional<Forum> findById(Integer integer) {
        return forumRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return forumRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        forumRepository.deleteById(integer);
    }
}
