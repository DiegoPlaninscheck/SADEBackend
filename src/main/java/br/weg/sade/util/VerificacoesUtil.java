package br.weg.sade.util;

import br.weg.sade.model.entities.Demanda;
import br.weg.sade.model.entities.Notificacao;
import br.weg.sade.model.entities.Usuario;
import br.weg.sade.model.entities.enuns.AcaoNotificacao;
import br.weg.sade.model.entities.enuns.TipoNotificacao;
import br.weg.sade.model.service.DemandaService;
import br.weg.sade.model.service.NotificacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class VerificacoesUtil {

    @Autowired
    private DemandaService demandaService;

    @Autowired
    private NotificacaoService notificacaoService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    // 86400s = 1 dia
    @Scheduled(fixedDelay = 86400000)
    @Async
    @Transactional
    public void verificarDemandaRascunho(){
        try {
            List<Demanda> demandas = new ArrayList<>();
            for(Demanda demanda : demandaService.findAll()){
                if(demanda.isRascunho()){
                    demandas.add(demanda);
                }
            }

            if(demandas.size() > 0){
                for(Demanda demanda : demandas){
                    Notificacao notificacao = new Notificacao();
                    notificacao.setAcao(AcaoNotificacao.RASCUNHO);
                    notificacao.setDescricaoNotificacao("Há rascunho(s) para ser(em) concluido(s)");
                    notificacao.setTituloNotificacao("Rascunho");
                    notificacao.setTipoNotificacao(TipoNotificacao.DEMANDA);
                    notificacao.setLinkNotificacao("http://localhost:8081/continuedemand/?");

                    List<Usuario> usuarios = new ArrayList<>();

                    usuarios.add(demanda.getUsuario());

                    notificacao.setUsuariosNotificacao(usuarios);

                    notificacao.setIdComponenteLink(demanda.getIdDemanda());

                    Notificacao notificacaoSalva = notificacaoService.save(notificacao);

                    simpMessagingTemplate.convertAndSend("/notificacao/demanda/" + demanda.getIdDemanda(), notificacao);
                }
                System.out.println("FOI");
            } else {
                System.out.println("SEM RASCUNHOS DEMANDA");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
