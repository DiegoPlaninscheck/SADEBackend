package br.weg.sod.util;

import br.weg.sod.dto.DemandaCriacaoDTO;
import br.weg.sod.dto.DemandaEdicaoDTO;
import br.weg.sod.dto.HistoricoWorkflowCriacaoDTO;
import br.weg.sod.dto.HistoricoWorkflowEdicaoDTO;
import br.weg.sod.model.entities.Demanda;
import br.weg.sod.model.entities.HistoricoWorkflow;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.Valid;
import java.sql.Time;

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

    ////////////////

//    public Demanda convertJsonToCreationModel(String demandaJSON) {
//        DemandaCriacaoDTO demandaCriacaoDTO = convertJsontoCreationDto(demandaJSON);
//        return convertCreationDtoToModel(demandaCriacaoDTO);
//    }
//
//    private DemandaCriacaoDTO convertJsontoCreationDto(String demandaJSON){
//        try {
//            return this.objectMapper.readValue(demandaJSON, DemandaCriacaoDTO.class);
//        } catch (Exception exception){
//            throw new RuntimeException(exception);
//        }
//    }
//
//    private Demanda convertCreationDtoToModel(@Valid DemandaCriacaoDTO demandaCriacaoDTO){
//        return  this.objectMapper.convertValue(demandaCriacaoDTO, Demanda.class);
//    }
//
//    public Demanda convertJsonToEditionModel(String demandaJSON) {
//        DemandaEdicaoDTO demandaEdicaoDTO = convertJsontoEditionDto(demandaJSON);
//        return convertEditionDtoToModel(demandaEdicaoDTO);
//    }
//
//    private DemandaEdicaoDTO convertJsontoEditionDto(String demandaJSON){
//        try {
//            return this.objectMapper.readValue(demandaJSON, DemandaEdicaoDTO.class);
//        } catch (Exception exception){
//            throw new RuntimeException(exception);
//        }
//    }
//
//    private Demanda convertEditionDtoToModel(@Valid DemandaEdicaoDTO demandaCriacaoDTO){
//        return this.objectMapper.convertValue(demandaCriacaoDTO, Demanda.class);
//    }
}
