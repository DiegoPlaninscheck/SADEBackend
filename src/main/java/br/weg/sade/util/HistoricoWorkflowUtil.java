package br.weg.sade.util;

import br.weg.sade.model.dto.HistoricoWorkflowCriacaoDTO;
import br.weg.sade.model.dto.HistoricoWorkflowEdicaoDTO;
import br.weg.sade.model.entity.HistoricoWorkflow;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.Valid;

public class HistoricoWorkflowUtil {

    private ObjectMapper objectMapper = new ObjectMapper();

    public HistoricoWorkflow convertJsonToModel(String historicoJSON, int tipoDTO) {
        Object historicoDTO;

        if (tipoDTO == 1) {
            historicoDTO = convertJsontoDtoCriacao(historicoJSON);
        } else {
            historicoDTO = convertJsontoDtoEdicao(historicoJSON);
        }

        return convertDtoToModel(historicoDTO);
    }

    public HistoricoWorkflowCriacaoDTO convertJsontoDtoCriacao(String historicoJSON) {
        try {
            return this.objectMapper.readValue(historicoJSON, HistoricoWorkflowCriacaoDTO.class);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public HistoricoWorkflowEdicaoDTO convertJsontoDtoEdicao(String historicoJSON) {
        try {
            return this.objectMapper.readValue(historicoJSON, HistoricoWorkflowEdicaoDTO.class);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private HistoricoWorkflow convertDtoToModel(@Valid Object historicoDTO) {
        return this.objectMapper.convertValue(historicoDTO, HistoricoWorkflow.class);
    }
}

