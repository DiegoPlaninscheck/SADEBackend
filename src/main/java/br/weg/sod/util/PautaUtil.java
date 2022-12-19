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

    private PautaEdicaoDTO convertJsontoDto(String pautaJSON) {
        try {
            return this.objectMapper.readValue(pautaJSON, PautaEdicaoDTO.class);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private Pauta convertDtoToModel(@Valid PautaEdicaoDTO propostaDTO) {
        return this.objectMapper.convertValue(propostaDTO, Pauta.class);
    }
}
