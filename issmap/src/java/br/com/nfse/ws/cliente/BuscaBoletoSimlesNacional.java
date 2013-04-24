/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.ws.cliente;

import br.com.nfse.jsfuntil.ConfiguracaoSyst;
import br.com.nfse.ws.beans.BoletosSF;
import br.com.nfse.ws.beans.ListaBoletoSF;

/**
 *
 * @author Thiago
 */

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import java.net.URI;
import javax.ws.rs.core.UriBuilder;


public class BuscaBoletoSimlesNacional {
    
    
     public ListaBoletoSF retornaBoletoSimplesNacional(String exAno, String mobNro) {

        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource service = client.resource(getBaseURI());
        ListaBoletoSF listaBoletos = new ListaBoletoSF();

        try {

            listaBoletos = service.path("ws").path("bs").path(exAno).path(mobNro).get(ListaBoletoSF.class);
            //nossoNumero = service.path("ws").path("nossonumero").path(anoStr).path("17545006693").path("4").get(DadosBoletoWS.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("ERRO Mobiliario gemmap");
        }
        return listaBoletos;
    }

    private static URI getBaseURI() {
        return UriBuilder.fromUri(
                ConfiguracaoSyst.SERVIDOR_WEBSERVICE).build();
        //"http://201.43.6.22:8080/RestGemmap").build();                        
    }
    
    
    public static void main(String args[]) {

        BuscaBoletoSimlesNacional buscaBoleto = new BuscaBoletoSimlesNacional();

        ListaBoletoSF listaBoleto = buscaBoleto.retornaBoletoSimplesNacional("2012", "43");

        if (listaBoleto.getListaBoleto() != null) {
            for (BoletosSF boletoSemBaixa : listaBoleto.getListaBoleto()) {
                System.out.println(boletoSemBaixa.getNumeroDocumento());
            }
        }else{
            System.out.print("Lista Vazia!!!!");
        }            
    }
    
}
