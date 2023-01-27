package br.weg.sod.util;

import br.weg.sod.dto.ATAEdicaoDTO;
import br.weg.sod.model.entities.ATA;
import br.weg.sod.model.entities.enuns.TipoDocumento;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.Valid;
import java.util.ArrayList;

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

    private ATA convertDtoToModel(@Valid ATAEdicaoDTO ataDTO) {
        return this.objectMapper.convertValue(ataDTO, ATA.class);
    }

}
