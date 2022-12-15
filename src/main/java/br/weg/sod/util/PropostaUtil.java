package br.weg.sod.util;

import br.weg.sod.dto.PropostaDTO;
import br.weg.sod.model.entities.Proposta;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.Valid;

public class PropostaUtil {

    private ObjectMapper objectMapper = new ObjectMapper();

    public Proposta convertJsonToModel(String propostaJSON) {
        PropostaDTO propostaDTO = convertJsontoDto(propostaJSON);
        return convertDtoToModel(propostaDTO);
    }

    private PropostaDTO convertJsontoDto(String propostaJSON) {
        try {
            return this.objectMapper.readValue(propostaJSON, PropostaDTO.class);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private Proposta convertDtoToModel(@Valid PropostaDTO propostaDTO) {
        return this.objectMapper.convertValue(propostaDTO, Proposta.class);
    }
}
