package br.weg.sade.model.entities.enuns;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SecaoTI {

    STD("Sistemas de Tecnologias Digitais"),
    AGD("Arquitetura e Governança de Dados"),
    SEG("Segurança"),
    SGI("Suporte"),
    TIN("Tecnologias"),
    AAS("Atendimento"),
    PTI("Projetos de TI"),
    SCO("Sistemas Corporativos"),
    SIM("Sistemas de Manufatura"),
    SIE("Sistemas de Engenharia"),
    SVE("Sistemas de Vendas e ECommerce");

    String nome;
}
