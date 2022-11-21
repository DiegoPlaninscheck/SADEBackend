package br.weg.sod.model.entities.enuns;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TipoForum {

    CVDP("Comissão de Vendas e Desenvolvimento de Produtos"),
    CPGCI("Comissão de Processos de Gestão de Cadeia Integrada"),
    CPGPR("Comissão de Processos de Gerenciamento de Projetos de Fornecimento"),
    CTI("Comitê de TI"),
    GPN("Comitê de Gestão de Projetos de Negócio");

    String nome;
}
