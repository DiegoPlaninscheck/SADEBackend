package br.weg.sod.util;

import br.weg.sod.dto.PautaEdicaoDTO;
import br.weg.sod.model.entities.Pauta;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.Valid;

public class PautaUtil {

    private ObjectMapper objectMapper = new ObjectMapper();

    public Pauta convertJsonToModel(String pautaJSON) {
        PautaEdicaoDTO pautaDTO = convertJsontoDto(pautaJSON);
        return convertDtoToModel(pautaDTO);
    }

    public PautaEdicaoDTO convertJsontoDto(String pautaJSON) {
        try {
            return this.objectMapper.readValue(pautaJSON, PautaEdicaoDTO.class);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private Pauta convertDtoToModel(@Valid PautaEdicaoDTO pautaDTO) {
        return this.objectMapper.convertValue(pautaDTO, Pauta.class);
    }
}
