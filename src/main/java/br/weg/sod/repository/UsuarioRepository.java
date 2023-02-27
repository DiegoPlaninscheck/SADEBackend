package br.weg.sod.repository;

import br.weg.sod.model.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    List<Usuario> findByDepartamento(String departamento);

    boolean existsByNumeroCadastro(Integer numeroCadastro);
}
