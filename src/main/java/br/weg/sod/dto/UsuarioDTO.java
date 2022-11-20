package br.weg.sod.dto;

import lombok.NonNull;

public class UsuarioDTO {

    @NonNull
    private Integer idUsuario;

    @NonNull
    private Integer numeroCadastro;

    @NonNull
    private String nome;

    @NonNull
    private String departamento;

    @NonNull
    private String email;

    @NonNull
    private String senha;

    @NonNull
    private byte[] foto;

    @NonNull
    private String setor;

    @NonNull
    private String cargo;

    @NonNull
    private Integer responsaveisNegocioIdUsuario;

    @NonNull
    private Integer notificacoesUsuarioIdUsuario;

    @NonNull
    private Integer usuariosForumIdUsuario;
}
