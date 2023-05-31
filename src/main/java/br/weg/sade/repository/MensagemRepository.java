package br.weg.sade.repository;

import br.weg.sade.model.entity.Chat;
import br.weg.sade.model.entity.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensagemRepository extends JpaRepository<Mensagem, Integer> {

    List<Mensagem> findMensagemsByChat(Chat chat);
}
