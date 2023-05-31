package br.weg.sade.model.dto;

import br.weg.sade.model.entity.Notificacao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
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

    @JsonIgnore
    private byte[] foto;

    @NotBlank
    private String setor;

    @NotBlank
    private String cargo;

    private List<Notificacao> notificacoesUsuario;

}
