package br.weg.sod.security;


import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UsuarioDTO {

    @NotBlank
    private String email;

    @NotBlank
    private String senha;
}
