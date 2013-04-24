/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.ws.cliente;

import br.com.nfse.jsfuntil.ConfiguracaoSyst;
import br.com.nfse.ws.beans.DadosBoletoWS;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.ws.rs.core.UriBuilder;

/**
 *
 * @author Thiago
 */
public class BuscaDadosBoletoGemmap {

    public DadosBoletoWS retornaMobiliario(String ano, String codigoCedente, String ficha, String valorDocumento, String numeroMes, String nroMobiliario, String dataInicio, String dataFim, String cpfCnpj, String tipoNota, String codigoQuandoUnica) {

        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource service = client.resource(getBaseURI());
        
        DadosBoletoWS nossoNumero = new DadosBoletoWS();
        
        try {

            DateFormat dataf = new SimpleDateFormat("yyyy");
            Date data = Calendar.getInstance(Locale.getDefault()).getTime();
            String anoStr = dataf.format(data);

            nossoNumero = service.path("ws").path("nossonumero").path(anoStr).path(codigoCedente).path(ficha).path(valorDocumento).path(numeroMes).path(nroMobiliario).path(dataInicio).path(dataFim).path(cpfCnpj).path(tipoNota).path(codigoQuandoUnica).get(DadosBoletoWS.class);
            //nossoNumero = service.path("ws").path("nossonumero").path(anoStr).path("17545006693").path("4").get(DadosBoletoWS.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("ERRO Mobiliario gemmap");
        }
        return nossoNumero;
    }

    private static URI getBaseURI() {
        return UriBuilder.fromUri(
                ConfiguracaoSyst.SERVIDOR_WEBSERVICE).build();
        //"http://201.43.6.22:8080/RestGemmap").build();                        
    }
}
