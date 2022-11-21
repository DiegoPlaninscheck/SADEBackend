package br.weg.sod.model.entities.enuns;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TipoBeneficio {

    REAL("Real"),
    POTENCIAL("Potencial"),
    QUALITATIVO("Qualitativo");

    private String nome;

}
