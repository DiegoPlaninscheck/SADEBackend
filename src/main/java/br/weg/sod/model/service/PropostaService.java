package br.weg.sod.model.service;

import br.weg.sod.model.entities.CentroCustoPagante;
import br.weg.sod.model.entities.LinhaTabela;
import br.weg.sod.model.entities.Proposta;
import br.weg.sod.model.entities.TabelaCusto;
import br.weg.sod.repository.PropostaRepository;
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

    public boolean existsById(Integer integer) {
        return propostaRepository.existsById(integer);
    }

    public void deleteById(Integer integer) {
        propostaRepository.deleteById(integer);
    }

    public List<Proposta> getPropostasAprovadasWorkflow(){
        List<Proposta> propostas = findAll(), propostasParaAprovar = new ArrayList<>();

        for(Proposta proposta : propostas){
            if(proposta.getAprovadoWorkflow() && !proposta.getAvaliadoWorkflow()){


                propostasParaAprovar.add(proposta);
            }
        }

        return propostasParaAprovar;
    }

    public boolean propostasExistem(List<Proposta> propostasPauta){
        for(Proposta proposta : propostasPauta){
            if(!existsById(proposta.getIdProposta())){
                return false;
            }
        }

        return true;
    }

    public boolean tabelasvalidas(List<TabelaCusto> tabelasCustoProposta) {
        for(TabelaCusto tabela : tabelasCustoProposta){
            Double valorTotal = 0.0, porcentagemTotal = 0.0;
            Integer quantidadeTotal = 0;

            for(LinhaTabela linhaTabela : tabela.getLinhasTabela()){
                valorTotal += linhaTabela.getValorQuantidade() * linhaTabela.getQuantidade();
                quantidadeTotal += linhaTabela.getQuantidade();
            }

            if(quantidadeTotal != tabela.getQuantidadeTotal() || !valorTotal.equals(tabela.getValorTotal())){
                return false;
            }

            for(CentroCustoPagante centroCustoPagante : tabela.getCentrosCustoPagantes()){
                porcentagemTotal += centroCustoPagante.getPorcentagemDespesa();
            }

            if(porcentagemTotal != 1){
                return false;
            }

        }

        return true;
    }
}
