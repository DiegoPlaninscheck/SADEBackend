package br.weg.sod.util;

import br.weg.sod.dto.ATAEdicaoDTO;
import br.weg.sod.model.entities.ATA;
import br.weg.sod.model.entities.enuns.TipoDocumento;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.Valid;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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

    public static String[] getPropriedadesNulas (Object fonte) {
        BeanWrapper src = new BeanWrapperImpl(fonte);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet();
        for(PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

}
