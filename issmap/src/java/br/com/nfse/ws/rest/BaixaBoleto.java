/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.ws.rest;

import br.com.nfse.ws.beans.RespNotifica;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Thiago
 */
@Path("/b")
public class BaixaBoleto {
    
    @Path("/{nota}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public RespNotifica recebeNotificacao(@PathParam("nota") String nota) {
        
        RespNotifica resposta = new RespNotifica();
        resposta.setResposta("N");
        
        if(nota.equals("thiago")){
            resposta.setResposta("S");
        }                
        return resposta;
        
    }
    
    
}
