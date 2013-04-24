/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.ws.cliente;

import br.com.nfse.jsfuntil.ConfiguracaoSyst;
import br.com.nfse.ws.beans.Mobiliario;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class BuscaMobiliarioGemmap {        

    public Mobiliario retornaMobiliario(String documento) throws Exception{
        
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource service = client.resource(getBaseURI());
        
        Mobiliario mobiliario = new Mobiliario();
        try{
            //System.out.println("Buscando Mobiliario GEMMAP!!!...");
            mobiliario = service.path("ws").path("mobiliarios").path(documento).get(Mobiliario.class);
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println("ERRO Mobiliario gemmap");
            throw new Exception();
        }        
        return mobiliario;
    }
    
    private static URI getBaseURI() {
        return UriBuilder.fromUri(
                ConfiguracaoSyst.SERVIDOR_WEBSERVICE).build();
                //"http://200.205.9.172:8080/RestGemmap").build();                        
    }
    
    public static void main(String[] args) {
        
        BuscaMobiliarioGemmap teste = new BuscaMobiliarioGemmap();
        
        //Mobiliario mobiliario = teste.retornaMobiliario("60701190061740");
        
      //  System.out.println(mobiliario.getCnpjCpf());
    }    
}