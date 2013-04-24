/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.ws.rest;

import br.com.nfse.dao.DAOEstados;
import br.com.nfse.to.Estado;
import br.com.nfse.to.TesteTotal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author ThiagoHenrique
 */
@Path("/estados/xml")
public class EstadosResourcesXml {
    
    // busca e lista objetos
    @Path("/estado")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<Estado> getEstados() {
        DAOEstados daoEstados = new DAOEstados();
        
        return daoEstados.listaEstados();        
    }
    
    @Path("/total")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<TesteTotal> getTotal() {
        
        List<TesteTotal> listaTesteTotal = new ArrayList<TesteTotal>();
        TesteTotal t = new TesteTotal();
        
        t.setTotal(10);
        
        listaTesteTotal.add(t);
        
        return listaTesteTotal;
    }
    
    // inclui objetos
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.TEXT_PLAIN)
    public String recebeEstado(Estado estado) {
        System.out.println(estado.getNome());
        return "Maravilha Sucesso";
    }
    
    // Atualiza objetos
    @Path("{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    @Produces("text/plain")
    public String atualizaEstado(Estado novoEstado, @PathParam("id") int id) {
        DAOEstados daoEstados = new DAOEstados();
        Estado estado = daoEstados.buscarProCodigo(id);
        try {
            daoEstados.salvar(estado);
             return estado.getNome() + " atualizada.";
        } catch (SQLException ex) {
            Logger.getLogger(EstadosResourcesXml.class.getName()).log(Level.SEVERE, null, ex);
             return estado.getNome() + " atualizada.";
        }
       
    }
    
    // remove
    @Path("{id}")
    @DELETE
    @Produces("text/plain")
    public String removeBanda(@PathParam("id") Integer id) {
        DAOEstados daoEstados = new DAOEstados();
        Estado estado = new Estado();
        try {
            daoEstados.excluir(estado);
            return "Estado Excluido.";
        } catch (Exception ex) {
            Logger.getLogger(EstadosResourcesXml.class.getName()).log(Level.SEVERE, null, ex);
            return "Banda removida.";
        }
        
    }
}