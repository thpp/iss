/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.ws.cliente;

import br.com.nfse.jsfuntil.ConfiguracaoSyst;
import br.com.nfse.ws.beans.BoletoSemBaixa;
import br.com.nfse.ws.beans.ListaBoletoSemBaixa;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import java.net.URI;
import javax.ws.rs.core.UriBuilder;

/**
 *
 * @author Thiago
 */
public class BuscaBoletosBaixados {

    public ListaBoletoSemBaixa retornaBoletosBaixados(String exAno, String mobNro) {

        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource service = client.resource(getBaseURI());

        ListaBoletoSemBaixa listaBoletos = new ListaBoletoSemBaixa();

        try {

            listaBoletos = service.path("ws").path("b").path(exAno).path(mobNro).get(ListaBoletoSemBaixa.class);
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

        BuscaBoletosBaixados buscaBoleto = new BuscaBoletosBaixados();

        ListaBoletoSemBaixa listaBoleto = buscaBoleto.retornaBoletosBaixados("2012", "43");

        if (listaBoleto.getBoletosSemBaixa() != null) {
            for (BoletoSemBaixa boletoSemBaixa : listaBoleto.getBoletosSemBaixa()) {
                System.out.println(boletoSemBaixa.getCgarqNro());
            }
        }else{
            System.out.print("Lista Vazia!!!!");
        }            
    }
}
