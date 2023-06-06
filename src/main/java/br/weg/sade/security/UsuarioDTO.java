package br.weg.sade.security;


import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UsuarioDTO {

    @NotBlank
    private String email;

    @NotBlank
    private String senha;

    private String stringUsuario;
}
