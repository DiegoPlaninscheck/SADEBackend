package br.weg.sade.service;

import br.weg.sade.model.entity.CentroCustoPagante;
import br.weg.sade.model.entity.LinhaTabela;
import br.weg.sade.model.entity.Proposta;
import br.weg.sade.model.entity.TabelaCusto;
import br.weg.sade.repository.PropostaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PropostaService {

    private PropostaRepository propostaRepository;

    public List<Proposta> findAll() {
        return propostaRepository.findAll();
    }

    public <S extends Proposta> S save(S entity) {
        return propostaRepository.save(entity);
    }

    public Optional<Proposta> findById(Integer integer) {
        return propostaRepository.findById(integer);
    }

    public List<Proposta> findByEstaEmPauta(boolean estaEmPauta) {
        return propostaRepository.findByEstaEmPauta(estaEmPauta);
    }

    public boolean existsById(Integer integer) {
        return propostaRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        propostaRepository.deleteById(integer);
    }

    public List<Proposta> getPropostasAprovadasWorkflow() {
        List<Proposta> propostas = findAll(), propostasParaAprovar = new ArrayList<>();

        for (Proposta proposta : propostas) {
            if (proposta.getAprovadoWorkflow() && proposta.getAvaliadoWorkflow()) {
                propostasParaAprovar.add(proposta);

                proposta.setAprovadoWorkflow(true);
                save(proposta);
            }
        }

        return propostasParaAprovar;
    }

    public boolean propostasExistem(List<Proposta> propostasPauta) {
        for (Proposta proposta : propostasPauta) {
            if (!existsById(proposta.getIdProposta())) {
                return false;
            }
        }

        return true;
    }

    public boolean tabelasvalidas(List<TabelaCusto> tabelasCustoProposta) {
        for (TabelaCusto tabela : tabelasCustoProposta) {
            Double valorTotal = 0.0, porcentagemTotal = 0.0;
            Integer quantidadeTotal = 0;

            for (LinhaTabela linhaTabela : tabela.getLinhasTabela()) {
                valorTotal += linhaTabela.getValorQuantidade() * linhaTabela.getQuantidade();
                quantidadeTotal += linhaTabela.getQuantidade();
            }

            if (quantidadeTotal != tabela.getQuantidadeTotal() || !valorTotal.equals(tabela.getValorTotal())) {
                return false;

            }

            for (CentroCustoPagante centroCustoPagante : tabela.getCentrosCustoPagantes()) {
                System.out.println(centroCustoPagante.getPorcentagemDespesa());
                porcentagemTotal += centroCustoPagante.getPorcentagemDespesa();
            }

            System.out.println(porcentagemTotal);

            if (porcentagemTotal != 1) {
                return false;
            }

        }

        return true;
    }
}
