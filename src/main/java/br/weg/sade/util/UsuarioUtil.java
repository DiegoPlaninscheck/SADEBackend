package br.weg.sade.util;

import br.weg.sade.model.dto.UsuarioDTO;
import br.weg.sade.model.entity.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.Valid;

public class UsuarioUtil {

    private ObjectMapper objectMapper = new ObjectMapper();


    public Usuario convertJsonToModel(String usuarioJSON) {
        UsuarioDTO usuarioDTO = convertJsonToDTO(usuarioJSON);
        return convertDtoToModel(usuarioDTO);
    }

    public Usuario convertJsonToModel(String usuarioJSON, Integer tipoUsuario) {
        UsuarioDTO usuarioDTO = convertJsonToDTO(usuarioJSON);

        System.out.println("Converteu json pra dto");
        System.out.println(usuarioDTO);

        return convertDtoToModel(usuarioDTO, tipoUsuario);
    }

    private UsuarioDTO convertJsonToDTO(String usuarioJSON) {
        try {
            return this.objectMapper.readValue(usuarioJSON, UsuarioDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Usuario convertDtoToModel(@Valid UsuarioDTO usuarioDTO) {
        return this.objectMapper.convertValue(usuarioDTO, Usuario.class);
    }

    private Usuario convertDtoToModel(@Valid UsuarioDTO usuarioDTO, Integer tipoUsuario) {
        Usuario usuario = null;

        switch (tipoUsuario) {
            case 1 -> {
                usuario = this.objectMapper.convertValue(usuarioDTO, Solicitante.class);
            }
            case 2 -> {
                usuario = this.objectMapper.convertValue(usuarioDTO, AnalistaTI.class);
            }
            case 3 -> {
                usuario = this.objectMapper.convertValue(usuarioDTO, GerenteNegocio.class);
            }
            case 4 -> {
                usuario = this.objectMapper.convertValue(usuarioDTO, GerenteTI.class);
            }
        }
        usuario.setSenha(usuarioDTO.getSenha());
        return usuario;
    }

}
