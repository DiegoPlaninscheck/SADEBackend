package br.weg.sod.util;

import br.weg.sod.dto.DemandaCriacaoDTO;
import br.weg.sod.dto.DemandaEdicaoDTO;
import br.weg.sod.model.entities.Demanda;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.Valid;
import java.sql.Time;

public class DemandaUtil {

    private ObjectMapper objectMapper = new ObjectMapper();

    public Demanda convertJsonToCreationModel(String demandaJSON) {
        DemandaCriacaoDTO demandaCriacaoDTO = convertJsontoCreationDto(demandaJSON);
        return convertCreationDtoToModel(demandaCriacaoDTO);
    }

    private DemandaCriacaoDTO convertJsontoCreationDto(String demandaJSON){
        try {
            return this.objectMapper.readValue(demandaJSON, DemandaCriacaoDTO.class);
        } catch (Exception exception){
            throw new RuntimeException(exception);
        }
    }

    private Demanda convertCreationDtoToModel(@Valid DemandaCriacaoDTO demandaCriacaoDTO){
        return  this.objectMapper.convertValue(demandaCriacaoDTO, Demanda.class);
    }

    public Demanda convertJsonToEditionModel(String demandaJSON) {
        DemandaEdicaoDTO demandaEdicaoDTO = convertJsontoEditionDto(demandaJSON);
        return convertEditionDtoToModel(demandaEdicaoDTO);
    }

    private DemandaEdicaoDTO convertJsontoEditionDto(String demandaJSON){
        try {
            return this.objectMapper.readValue(demandaJSON, DemandaEdicaoDTO.class);
        } catch (Exception exception){
            throw new RuntimeException(exception);
        }
    }

    private Demanda convertEditionDtoToModel(@Valid DemandaEdicaoDTO demandaCriacaoDTO){
        return this.objectMapper.convertValue(demandaCriacaoDTO, Demanda.class);
    }
}
