package br.weg.sade.repository;

import br.weg.sade.model.entity.Forum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForumRepository extends JpaRepository<Forum, Integer> {
}
