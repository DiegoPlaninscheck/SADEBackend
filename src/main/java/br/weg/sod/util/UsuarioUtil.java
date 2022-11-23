package br.weg.sod.util;

import br.weg.sod.dto.UsuarioDTO;
import br.weg.sod.model.entities.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.Valid;

public class UsuarioUtil {

    private ObjectMapper objectMapper = new ObjectMapper();

    public Usuario convertJsonToModel(String usuarioJSON) {
        UsuarioDTO usuarioDTO = convertJsonToDTO(usuarioJSON);
        return convertDtoToModel(usuarioDTO);
    }

    private UsuarioDTO convertJsonToDTO(String usuarioJSON) {
        try {
            return this.objectMapper.readValue(usuarioJSON, UsuarioDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Usuario convertDtoToModel(@Valid UsuarioDTO pecaDTO) {
        return this.objectMapper.convertValue(pecaDTO, Usuario.class);
    }

}
