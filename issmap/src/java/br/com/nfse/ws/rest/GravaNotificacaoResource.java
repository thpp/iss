/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.ws.rest;

import br.com.nfse.dao.DAOCcm;
import br.com.nfse.dao.DAONotificacoes;
import br.com.nfse.to.Ccm;
import br.com.nfse.to.Notificacoes;
import br.com.nfse.ws.beans.Notificacao;
import br.com.nfse.ws.beans.RespNotifica;
import br.com.nfse.ws.cliente.BuscaNotificacoesGemmap;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author ThiagoHenrique
 */
@Path("/notifica")
public class GravaNotificacaoResource {

    @Path("/{cpfCnpj}/{nro}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public RespNotifica recebeNotificacao(@PathParam("cpfCnpj") String cnpjCpf, @PathParam("nro") String nro) {
       
        
        RespNotifica resposta = new RespNotifica();
        BuscaNotificacoesGemmap buscaNotificacao = new BuscaNotificacoesGemmap();
        Ccm ccm = new Ccm();
        DAOCcm daoCcm = new DAOCcm();
        DAONotificacoes daoNotifica = new DAONotificacoes();
        resposta.setResposta("N");
        Notificacao notificaWs = new Notificacao();

        try {
            ccm = daoCcm.buscaPorCnpjCpf(cnpjCpf);
            notificaWs = buscaNotificacao.retornaNotificacoes(ccm.getCodISSGemmap().toString(), nro);
            Notificacoes notificacao = new Notificacoes();
            
            DateFormat dataf = new SimpleDateFormat("dd/MM/yyyy");

            notificacao.setDataEnvio(new Date(dataf.parse(notificaWs.getDataEnvio()).getTime()));
            notificacao.setDestinatario(ccm);
            notificacao.setHora(notificaWs.getHora());
            notificacao.setNotificacao(notificaWs.getNotificacao());
            notificacao.setRemetente(notificaWs.getRemetente());
            resposta.setResposta(daoNotifica.salvarComRetorno(notificacao));

        } catch (ParseException ex) {
            resposta.setResposta("N");
        } catch (Exception ex) {
            resposta.setResposta("N");
        }
        System.out.println("RESPOSTA ENVIADA >>>>>>> "+resposta.getResposta());
        return resposta;
    }
}
