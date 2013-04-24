/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.dao;

import br.com.nfse.to.AguardandoEnvio;
import br.com.nfse.to.NFSE;
import br.com.nfse.generic.GenericDAO;
import br.com.nfse.jsfuntil.HibernateUtil;
import br.com.nfse.to.Ccm;
import br.com.nfse.to.Usuario;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author ThiagoHenrique
 */
public class DAOAguardaEnvio extends GenericDAO {
    
    private Session sessao = HibernateUtil.getSessionFactory().openSession();
    Usuario usuario = new Usuario();
    
    List<AguardandoEnvio> notasEmEspera;
    
    public List<AguardandoEnvio> bustaNotasEmEspera(String cpfCnpj){
        notasEmEspera = new ArrayList<AguardandoEnvio>();
        
        try {
            Criteria criteria = sessao.createCriteria(AguardandoEnvio.class);
            criteria = criteria.createAlias("notaEspera", "n");
            criteria = criteria.add(Restrictions.eq("n.cpfCnpjPrestador", cpfCnpj));
            if (criteria.list().isEmpty()) {
                return notasEmEspera = null;
            } else {
                return notasEmEspera = criteria.list();
            }
        } catch (Exception e) {
            return notasEmEspera = null;
        }
        
    }
    
    public static void main(String args[]){
        
        DAOAguardaEnvio daoAguardaEnvio = new DAOAguardaEnvio();
        List<AguardandoEnvio> lista = daoAguardaEnvio.bustaNotasEmEspera("04114569000139");
        
        for (AguardandoEnvio aguarda : lista) {
            System.out.println(aguarda.getNotaEspera().getCpfCnpjTomador());
        }
        
    }
    
    
}
