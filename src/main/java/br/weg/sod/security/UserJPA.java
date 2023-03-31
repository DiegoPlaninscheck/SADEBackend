package br.weg.sod.security;

import br.weg.sod.model.entities.Usuario;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class UserJPA implements UserDetails {

    private Usuario usuario;

    private Collection<GrantedAuthority> authorities;

    private boolean accountNonExpired = true;

    private boolean accountNonLocked = true;

    private boolean credentialsNonExpired = true;

    private boolean enabled = true;

    public UserJPA(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String getPassword() {
        return usuario.getSenha();
    }

    @Override
    public String getUsername() {
        return usuario.getEmail();
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities(){
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();

        grantedAuthorityList.add(new SimpleGrantedAuthority(this.getUsername().getClass().getSimpleName()));

        return grantedAuthorityList;
    }
}
