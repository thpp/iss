/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.ws.rest;

import br.com.nfse.dao.DAOEstados;
import br.com.nfse.to.Estado;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author ThiagoHenrique
 */
@Path("/estados/json")
public class EstadosResourcesJs {
    
    @GET
    @Produces( MediaType.APPLICATION_JSON)
    public List<Estado> getEstados(){
        
        DAOEstados daoEstados = new DAOEstados();
        return daoEstados.listaEstados();        
    }
    
}
