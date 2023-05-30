//package br.weg.sod.util;
//
//import br.weg.sod.model.entities.Demanda;
//import br.weg.sod.model.entities.Notificacao;
//import br.weg.sod.model.entities.Usuario;
//import br.weg.sod.model.entities.enuns.AcaoNotificacao;
//import br.weg.sod.model.entities.enuns.TipoNotificacao;
//import br.weg.sod.model.service.DemandaService;
//import br.weg.sod.model.service.NotificacaoService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import javax.transaction.Transactional;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class VerificacoesUtil {
//
//    @Autowired
//    private DemandaService demandaService;
//
//    @Autowired
//    private NotificacaoService notificacaoService;
//
//    @Autowired
//    private SimpMessagingTemplate simpMessagingTemplate;
//
//    // 86400s = 1 dia
//    @Scheduled(fixedDelay = 60000)
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
//                Notificacao notificacao = new Notificacao();
//                notificacao.setAcao(AcaoNotificacao.RASCUNHO);
//                notificacao.setDescricaoNotificacao("Há rascunho(s) para ser(em) concluido(s)");
//                notificacao.setTituloNotificacao("Rascunho");
//                notificacao.setTipoNotificacao(TipoNotificacao.DEMANDA);
//                notificacao.setLinkNotificacao("http://localhost:8081/continuedemand");
//
//                List<Usuario> usuarios = new ArrayList<>();
//
//                for(Demanda demanda : demandas){
//                    usuarios.add(demanda.getUsuario());
//
//                    notificacao.setUsuariosNotificacao(usuarios);
//
//                    notificacao.setIdComponenteLink(demanda.getIdDemanda());
//
//                    notificacao = notificacaoService.save(notificacao);
//
//                    simpMessagingTemplate.convertAndSend("/notificacao/demanda/" + demanda.getIdDemanda(), notificacao);
//                }
//                System.out.println("FOI");
//            } else {
//                System.out.println("SEM RASCUNHOS DEMANDA");
//            }
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//}
