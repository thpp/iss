/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.ws.cliente;

/**
 *
 * @author Thiago
 */


import br.com.nfse.jsfuntil.ConfiguracaoSyst;
import br.com.nfse.ws.beans.Notificacao;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class BuscaNotificacoesGemmap {
    
    
    public Notificacao retornaNotificacoes(String nroGemmap, String nro){
        
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource service = client.resource(getBaseURI());
        
        Notificacao notificacoes = new Notificacao();
        try{
            notificacoes = service.path("ws").path("notifica").path(nroGemmap).path(nro).get(Notificacao.class);
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println("ERRO Mobiliario gemmap");
        }        
        return notificacoes;
    }
    
    private static URI getBaseURI() {
        return UriBuilder.fromUri(
                ConfiguracaoSyst.SERVIDOR_WEBSERVICE).build();
                //"http://201.43.6.22:8080/RestGemmap").build();                        
    }
    
    public static void main(String args[]){
        
//        BuscaNotificacoesGemmap buscagemmap = new BuscaNotificacoesGemmap();
//        
//        List<Notificacao> lista = buscagemmap.retornaNotificacoes("43");
//        
//        for (Notificacao notificaWs : lista) {
//            System.out.println(notificaWs.getNotificacao());
//        }
        
    }
    
}
