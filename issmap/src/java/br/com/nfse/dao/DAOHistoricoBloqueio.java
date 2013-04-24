/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.dao;

import br.com.nfse.generic.GenericDAO;
import br.com.nfse.jsfuntil.HibernateUtil;
import br.com.nfse.to.Ccm;
import br.com.nfse.to.HistoricoBloqueio;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import sun.net.www.http.Hurryable;

/**
 *
 * @author ThiagoHenrique
 */
public class DAOHistoricoBloqueio extends GenericDAO {

    private Session session = null;
    private Transaction transacao = null;

    public HistoricoBloqueio buscaRegistroHistoricoAberto(Ccm ccm) throws Exception {

        List<HistoricoBloqueio> listaHistorico = new LinkedList<HistoricoBloqueio>();
        session = HibernateUtil.getSessionFactory().openSession();
        
        Criteria criteria = session.createCriteria(HistoricoBloqueio.class);
        criteria = criteria.createAlias("ccm", "c");
        criteria = criteria.add(Restrictions.eq("c.id", ccm.getId()));
        criteria = criteria.add(Restrictions.isNull("dataLiberacao"));
        
        return (HistoricoBloqueio) criteria.list().get(0);
        
    }
    
    public static void main(String args[]){
        Ccm ccm = new Ccm();
        ccm.setId(4);
        
        DAOHistoricoBloqueio daoHistorico = new DAOHistoricoBloqueio();
        try {
            HistoricoBloqueio historicoBloqueio = daoHistorico.buscaRegistroHistoricoAberto(ccm);
            

                System.out.println(historicoBloqueio.getDatabloqueio());
                System.out.println(historicoBloqueio.getMotivo());
                System.out.println(historicoBloqueio.getDataLiberacao());
            
            
        } catch (Exception ex) {
            System.out.println("Erro ao buscar hitbloqueio");
        }
        
        
        
    }

}
