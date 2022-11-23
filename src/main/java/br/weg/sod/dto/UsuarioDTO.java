package br.weg.sod.dto;

import br.weg.sod.model.entities.Notificacao;
import lombok.NonNull;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class UsuarioDTO {

    @Digits(integer = 6, fraction = 0)
    private Integer numeroCadastro;

    @NotBlank
    private String nomeUsuario;

    @NotBlank
    private String departamento;

    @Email
    private String email;

    @NotBlank
    private String senha;

    @NonNull
    private byte[] foto;

    @NotBlank
    private String setor;

    @NotBlank
    private String cargo;

    private List<Notificacao> notificacoesUsuario;

}
