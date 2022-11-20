package br.weg.sod.model.service;

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
}
