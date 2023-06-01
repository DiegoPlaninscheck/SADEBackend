package br.weg.sade.util;

import br.weg.sade.model.entity.*;
import br.weg.sade.model.enuns.AcaoNotificacao;
import br.weg.sade.model.enuns.Tarefa;
import br.weg.sade.model.enuns.TipoNotificacao;
import br.weg.sade.service.DemandaService;
import br.weg.sade.service.HistoricoWorkflowService;
import br.weg.sade.service.NotificacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class VerificacoesUtil {

    @Autowired
    private DemandaService demandaService;

    @Autowired
    private NotificacaoService notificacaoService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private HistoricoWorkflowService historicoWorkflowService;

    // 86400s = 1 dia
//    @Scheduled(fixedDelay = 86400000)
//    @Async
//    @Transactional
//    public void verificarDemandaRascunho(){
//        try {
//            List<Demanda> demandas = new ArrayList<>();
//            for(Demanda demanda : demandaService.findAll()){
//                if(demanda.isRascunho()){
//                    demandas.add(demanda);
//                }
//            }
//
//            if(demandas.size() > 0){
//                for(Demanda demanda : demandas){
//                    Notificacao notificacao = new Notificacao();
//                    notificacao.setAcao(AcaoNotificacao.RASCUNHO);
//                    notificacao.setDescricaoNotificacao("Há rascunho(s) para ser(em) concluido(s)");
//                    notificacao.setTituloNotificacao("Rascunho");
//                    notificacao.setTipoNotificacao(TipoNotificacao.DEMANDA);
//                    notificacao.setLinkNotificacao("http://localhost:8081/continuedemand/?");
//
//                    List<Usuario> usuarios = new ArrayList<>();
//
//                    usuarios.add(demanda.getUsuario());
//
//                    notificacao.setUsuariosNotificacao(usuarios);
//
//                    notificacao.setIdComponenteLink(demanda.getIdDemanda());
//
//                    Notificacao notificacaoSalva = notificacaoService.save(notificacao);
//
//                    simpMessagingTemplate.convertAndSend("/notificacao/demanda/" + demanda.getIdDemanda(), notificacao);
//                }
//            } else {
//                System.out.println("SEM RASCUNHOS DEMANDA");
//            }
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

    @Scheduled(fixedDelay = 86400000)
    @Async
    @Transactional
    public void verificarDemandaPrazoElaboracaoProposta() {
        try {
            List<Demanda> demandas = new ArrayList<>();
            for (Demanda demanda : demandaService.findAll()) {
                if (demanda.getPrazoElaboracao() != null) {
                    if (new Date().getTime() > demanda.getPrazoElaboracao().getTime()) {
                        demandas.add(demanda);
                    }
                }
            }

//            for (Demanda demanda : demandas) {
//                System.out.println(demanda);
//            }

            List<HistoricoWorkflow> historicoWorkflows = new ArrayList<>();
            for (Demanda demanda : demandas) {
                historicoWorkflows.add(historicoWorkflowService.findLastHistoricoByDemanda(demanda));
            }

            for (HistoricoWorkflow historicoWorkflow : historicoWorkflows) {
                if (historicoWorkflow.getUsuario() instanceof AnalistaTI && historicoWorkflow.getTarefa() == Tarefa.CRIARPROPOSTA) {
                    System.out.println(historicoWorkflow.getUsuario());
                    Notificacao notificacao = new Notificacao();
                    notificacao.setAcao(AcaoNotificacao.PRAZOELABORACAOPROPOSTA);
                    notificacao.setDescricaoNotificacao("O seu prazo está finalizado, verifique imediatamente a sua demanda!");
                    notificacao.setTituloNotificacao("Prazo elaboração da demanda");
                    notificacao.setTipoNotificacao(TipoNotificacao.DEMANDA);
                    notificacao.setLinkNotificacao("http://localhost:8081/home/demand");
                    notificacao.setIdComponenteLink(historicoWorkflow.getDemanda().getIdDemanda());

                    List<Usuario> usuarios = new ArrayList<>();

                    usuarios.add(historicoWorkflow.getUsuario());

                    notificacao.setUsuariosNotificacao(usuarios);

                    Notificacao notificacaoSalva = notificacaoService.save(notificacao);

                    simpMessagingTemplate.convertAndSend("/notificacao/demanda/" + historicoWorkflow.getDemanda().getIdDemanda(), notificacaoSalva);
                }
            }
            System.out.println("FOI");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
