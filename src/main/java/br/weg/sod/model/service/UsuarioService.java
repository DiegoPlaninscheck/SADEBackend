package br.weg.sod.model.service;

import br.weg.sod.model.entities.GerenteNegocio;
import br.weg.sod.model.entities.GerenteTI;
import br.weg.sod.model.entities.Usuario;
import br.weg.sod.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UsuarioService {

    private UsuarioRepository usuarioRepository;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public <S extends Usuario> S save(S entity) {
        return usuarioRepository.save(entity);
    }

    public Optional<Usuario> findById(Integer integer) {
        return usuarioRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return usuarioRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        usuarioRepository.deleteById(integer);
    }

    public List<Usuario> findByDepartamento(String departamento) {
        return usuarioRepository.findByDepartamento(departamento);
    }

    public GerenteNegocio findGerenteByDepartamento(String departamento) {
        List<Usuario> usuarioList = findByDepartamento(departamento);

        System.out.println(usuarioList);

        for (Usuario usuario : usuarioList) {
            if (usuario instanceof GerenteNegocio) {
                System.out.println("achou");
                return (GerenteNegocio) usuario;
            }
        }

        return null;
    }

    public boolean existsByNumeroCadastro(Integer numeroCadastro) {
        return usuarioRepository.existsByNumeroCadastro(numeroCadastro);
    }
}
