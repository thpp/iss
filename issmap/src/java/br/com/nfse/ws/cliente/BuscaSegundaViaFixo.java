/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.ws.cliente;

import br.com.nfse.jsfuntil.ConfiguracaoSyst;
import br.com.nfse.ws.beans.SegundaViaFixo;
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
public class BuscaSegundaViaFixo {
    
    public SegundaViaFixo retornaBoletoFixo(Integer mobNro, Integer exAno, Integer nroFicha, Integer cgarcNro, String codcedente) throws Exception {

        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource service = client.resource(getBaseURI());

        SegundaViaFixo segundaVia = new SegundaViaFixo();

        try {

            segundaVia = service.path("ws").path("bfsv").path(mobNro.toString()).path(exAno.toString()).path(nroFicha.toString()).path(cgarcNro.toString()).path(codcedente).get(SegundaViaFixo.class);
            //nossoNumero = service.path("ws").path("nossonumero").path(anoStr).path("17545006693").path("4").get(DadosBoletoWS.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("ERRO Mobiliario gemmap");
        }
        return segundaVia;
    }

    private static URI getBaseURI() {
        return UriBuilder.fromUri(
                ConfiguracaoSyst.SERVIDOR_WEBSERVICE).build();
        //"http://201.43.6.22:8080/RestGemmap").build();                        
    }
    
    
    public static void main(String args[]) {

        SegundaViaFixo segundaVia = new SegundaViaFixo();
        
        try{
            segundaVia = new BuscaSegundaViaFixo().retornaBoletoFixo(197, 2012, 4, 4716503, "1754500669");
            System.out.println(segundaVia.getCodigoBarras());
            System.out.println(segundaVia.getDataVencimento());
            System.out.println(segundaVia.getNossoNumero());
            System.out.println(segundaVia.getNumeroDocumento());
            System.out.println(segundaVia.getValorIncendio());
            System.out.println(segundaVia.getValorIss());
            System.out.println(segundaVia.getValorTaxaExpediente());
            System.out.println(segundaVia.getValorTaxaLicenca());
        }catch(Exception ex){
            System.out.println("Erro ao Buscar!!.. "+ ex.getMessage());
        }
    }
    
    
}
