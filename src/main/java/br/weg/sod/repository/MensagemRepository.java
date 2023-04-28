package br.weg.sod.repository;

import br.weg.sod.model.entities.Chat;
import br.weg.sod.model.entities.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensagemRepository extends JpaRepository<Mensagem, Integer> {

    List<Mensagem> findMensagemsByChat(Chat chat);
}
