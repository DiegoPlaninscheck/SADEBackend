package br.weg.sod.model.service;

import br.weg.sod.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UsuarioService {

    private UsuarioRepository usuarioRepository;

    public List<UsuarioRepository> findAll() {
        return usuarioRepository.findAll();
    }

    public <S extends UsuarioRepository> S save(S entity) {
        return usuarioRepository.save(entity);
    }

    public Optional<UsuarioRepository> findById(Integer integer) {
        return usuarioRepository.findById(integer);
    }

    public boolean existsById(Integer integer) {
        return usuarioRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        usuarioRepository.deleteById(integer);
    }
}
