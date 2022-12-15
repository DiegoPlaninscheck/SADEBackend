package br.weg.sod.util;

import br.weg.sod.dto.DemandaCriacaoDTO;
import br.weg.sod.model.entities.Demanda;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.Valid;

public class DemandaUtil {

    private ObjectMapper objectMapper = new ObjectMapper();

    public Demanda convertJsonToModel(String livroJson) {
        DemandaCriacaoDTO livroDTO = convertJsontoDto(livroJson);
        return convertDtoToModel(livroDTO);
    }

    private DemandaCriacaoDTO convertJsontoDto(String livroJson){
        try {
            return this.objectMapper.readValue(livroJson, DemandaCriacaoDTO.class);
        } catch (Exception exception){
            throw new RuntimeException(exception);
        }
    }

    private Demanda convertDtoToModel(@Valid DemandaCriacaoDTO livroDTO){
        return  this.objectMapper.convertValue(livroDTO, Demanda.class);
    }
}
