package br.weg.sade.util;

import br.weg.sade.dto.PropostaCriacaoDTO;
import br.weg.sade.dto.PropostaEdicaoDTO;
import br.weg.sade.model.entities.Proposta;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.Valid;

public class PropostaUtil {

    private ObjectMapper objectMapper = new ObjectMapper();

    public Proposta convertJsonToModel(String propostaJSON, int tipoDTO) {
        Object propostaDTO;

        if(tipoDTO == 1){
            propostaDTO = convertJsontoDtoCriacao(propostaJSON);
        } else {
            propostaDTO = convertJsontoDtoEdicao(propostaJSON);
        }

        return convertDtoToModel(propostaDTO);
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
