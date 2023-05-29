package br.weg.sod.util;

import br.weg.sod.model.entities.Demanda;
import br.weg.sod.model.service.DemandaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class VerificacoesUtil {

    @Autowired
    private DemandaService demandaService;

    // 86400s = 1 dia
    @Scheduled(fixedDelay = 10000)
    public void verificarDemandaRascunho(){
        List<Demanda> demandas = new ArrayList<>();
        for(Demanda demanda : demandaService.findAll()){
            if(demanda.isRascunho()){
                demandas.add(demanda);
            }
        }

        if(demandas.size() > 0){
            System.out.println("TEM RASCUNHO DE DEMANDAS");
            System.out.println(demandas);
        } else {
            System.out.println("SEM RASCUNHOS DEMANDA");
        }
    }

}
