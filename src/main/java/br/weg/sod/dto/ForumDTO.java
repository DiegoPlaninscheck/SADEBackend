package br.weg.sod.dto;

import lombok.NonNull;

public class ForumDTO {

    @NonNull
    private Integer idForum;

    @NonNull
    private String nomeForum;

    @NonNull
    private Integer analistaIdUsuario;

    @NonNull
    private Integer forumIdForum;
}
