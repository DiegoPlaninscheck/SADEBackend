package br.weg.sod.util;

import br.weg.sod.dto.ATAEdicaoDTO;
import br.weg.sod.dto.PautaEdicaoDTO;
import br.weg.sod.model.entities.Pauta;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.Valid;
import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

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
