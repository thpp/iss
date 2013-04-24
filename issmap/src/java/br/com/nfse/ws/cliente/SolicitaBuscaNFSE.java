/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.ws.cliente;

import br.com.nfse.jsfuntil.ConfiguracaoSyst;
import br.com.nfse.ws.beans.RespNotaEnvio;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
/**
 *
 * @author ThiagoHenrique
 */
public class SolicitaBuscaNFSE {
    
    public RespNotaEnvio retornaSolicitaBusca(String documento, String codigoVereifica){
        
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource service = client.resource(getBaseURI());
        RespNotaEnvio resposta = new RespNotaEnvio();
        resposta.setResposta(false);
        try{
            resposta = service.path("ws").path("mobiliarios").path("nota").path(documento).path(codigoVereifica).get(RespNotaEnvio.class);            
        }catch(Exception e){
            System.out.println("ERRO Mobiliario gemmap");
        }        
        return resposta;
    }
    
    private static URI getBaseURI() {
        return UriBuilder.fromUri(
                ConfiguracaoSyst.SERVIDOR_WEBSERVICE).build();
                //"http://201.43.6.22:8080/RestGemmap").build();
                
        
    }
    
}
