package br.weg.sade.model.service;

import br.weg.sade.model.entities.GerenteNegocio;
import br.weg.sade.model.entities.GerenteTI;
import br.weg.sade.model.entities.Usuario;
import br.weg.sade.repository.UsuarioRepository;
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

    public List<Usuario> findGerentesTI() {
        return findAll().stream().filter(usuario -> usuario instanceof GerenteTI).toList();
    }

    public GerenteNegocio findGerenteByDepartamento(String departamento) {
        List<Usuario> usuarioList = findByDepartamento(departamento);

        for (Usuario usuario : usuarioList) {
            if (usuario instanceof GerenteNegocio) {
                return (GerenteNegocio) usuario;
            }
        }

        return null;
    }

    public GerenteTI findGerenteTIByDepartamento(String departamento) {
        List<Usuario> usuarioList = findByDepartamento(departamento);

        for (Usuario usuario : usuarioList) {
            if (usuario instanceof GerenteTI) {
                return (GerenteTI) usuario;
            }
        }

        return null;
    }

    public boolean existsByNumeroCadastro(Integer numeroCadastro) {
        return usuarioRepository.existsByNumeroCadastro(numeroCadastro);
    }

    public boolean responsaveisValidos(List<Usuario> responsaveisNegocio) {
        for(Usuario usuario : responsaveisNegocio){
            if(!existsById(usuario.getIdUsuario())){
                return false;
            }
        }

        return true;
    }
}
