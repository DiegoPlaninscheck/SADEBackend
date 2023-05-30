package br.weg.sade.repository;

import br.weg.sade.model.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    List<Usuario> findByDepartamento(String departamento);

    Optional<Usuario> findByEmail(String email);

    boolean existsByNumeroCadastro(Integer numeroCadastro);
}
