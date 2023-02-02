package br.weg.sod.util;

import br.weg.sod.dto.PropostaCriacaoDTO;
import br.weg.sod.dto.PropostaEdicaoDTO;
import br.weg.sod.model.entities.Proposta;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.Valid;

public class PropostaUtil {

    private ObjectMapper objectMapper = new ObjectMapper();

    public Proposta convertJsonToModel(String propostaJSON, int tipoDTO) {
        Object propostaEdicaoDTO;

        if(tipoDTO == 1){
            propostaEdicaoDTO = convertJsontoDtoCriacao(propostaJSON);
        } else {
            propostaEdicaoDTO = convertJsontoDtoEdicao(propostaJSON);
        }

        return convertDtoToModel(propostaEdicaoDTO);
    }

    private PropostaCriacaoDTO convertJsontoDtoCriacao(String propostaJSON) {
        try {
            return this.objectMapper.readValue(propostaJSON, PropostaCriacaoDTO.class);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private PropostaEdicaoDTO convertJsontoDtoEdicao(String propostaJSON) {
        try {
            return this.objectMapper.readValue(propostaJSON, PropostaEdicaoDTO.class);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private Proposta convertDtoToModel(@Valid Object propostaDTO) {
        return this.objectMapper.convertValue(propostaDTO, Proposta.class);
    }
}
