package br.weg.sod.security;

import br.weg.sod.model.entities.Usuario;
import br.weg.sod.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class JPAService implements UserDetailsService {

    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(username);

        if (usuario.isPresent()) {
            return new UserJPA(usuario.get());
        }

        throw new UsernameNotFoundException("Usuário não encontrado!");
    }

    public UserDetails loadUserByID(Integer idUsuario) {
        Optional<Usuario> pessoa = usuarioRepository.findById(idUsuario);
        if (pessoa.isPresent()) {
            return new UserJPA(pessoa.get());
        }
        throw new UsernameNotFoundException("Usuário não encontrado!");
    }
}
