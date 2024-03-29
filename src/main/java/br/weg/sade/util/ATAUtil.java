package br.weg.sade.util;

import br.weg.sade.model.dto.ATACriacaoDTO;
import br.weg.sade.model.dto.ATAEdicaoDTO;
import br.weg.sade.model.entity.ATA;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.Valid;

public class ATAUtil {

    private ObjectMapper objectMapper = new ObjectMapper();

    public ATA convertJsonToModel(String ataJSON) {
        ATAEdicaoDTO ataDTO = convertJsontoDto(ataJSON);
        return convertDtoToModel(ataDTO);
    }

    public ATAEdicaoDTO convertJsontoDto(String ataJSON) {
        try {
            return this.objectMapper.readValue(ataJSON, ATAEdicaoDTO.class);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public ATACriacaoDTO convertJsontoDtoCriacao(String ataJSON) {
        try {
            return this.objectMapper.readValue(ataJSON, ATACriacaoDTO.class);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private ATA convertDtoToModel(@Valid ATAEdicaoDTO ataDTO) {
        return this.objectMapper.convertValue(ataDTO, ATA.class);
    }

}
