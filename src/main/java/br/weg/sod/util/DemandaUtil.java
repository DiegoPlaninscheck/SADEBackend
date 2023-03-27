package br.weg.sod.util;

import br.weg.sod.dto.DemandaCriacaoDTO;
import br.weg.sod.dto.DemandaEdicaoDTO;
import br.weg.sod.model.entities.Demanda;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.Valid;

public class DemandaUtil {

    private ObjectMapper objectMapper = new ObjectMapper();

    public Demanda convertJsonToModel(String demandaJSON, int tipoDTO) {
        Object demandaDTO;

        if (tipoDTO == 1) {
            demandaDTO = convertJsontoDtoCriacao(demandaJSON);
        } else {
            demandaDTO = convertJsontoDtoEdicao(demandaJSON);
        }

        return convertDtoToModel(demandaDTO);
    }

    public DemandaCriacaoDTO convertJsontoDtoCriacao(String demandaJSON) {
        try {
            return this.objectMapper.readValue(demandaJSON, DemandaCriacaoDTO.class);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public DemandaEdicaoDTO convertJsontoDtoEdicao(String demandaJSON) {
        try {
            return this.objectMapper.readValue(demandaJSON, DemandaEdicaoDTO.class);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private Demanda convertDtoToModel(@Valid Object historicoDTO) {
        return this.objectMapper.convertValue(historicoDTO, Demanda.class);
    }
}
