/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.dao;

import br.com.nfse.generic.GenericDAO;
import br.com.nfse.jsfuntil.ConfiguracaoSyst;
import br.com.nfse.jsfuntil.HibernateUtil;
import br.com.nfse.to.BoletoIssMap;
import br.com.nfse.to.Ccm;
import com.sun.jersey.core.util.ThrowHelper;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.crypto.Data;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Thiago
 */
public class DAOBoleto extends GenericDAO {
    
    private Session session = null;
    private Transaction transacao = null;
    private List<BoletoIssMap> listaBoletos;
    
    public BoletoIssMap salvarBoletoRetorno(BoletoIssMap obj) throws SQLException {
        session = HibernateUtil.getSessionFactory().openSession();
        transacao = session.beginTransaction();
        BoletoIssMap BoletoRetorno = null;
        try {
            session.saveOrUpdate(obj);
            transacao.commit();
            BoletoRetorno = obj;
        } catch (Exception e) {
            transacao.rollback();
            System.out.println("Erro ao salvar ====>> " + e.getMessage());
        } finally {
            session.close();
        }
        return BoletoRetorno;
    }
    
     public void salvarMerge(BoletoIssMap obj) throws SQLException {
        session = HibernateUtil.getSessionFactory().openSession();
        transacao = session.beginTransaction();

        try {
            session.merge(obj);
            transacao.commit();
        } catch (Exception e) {
            transacao.rollback();
            System.out.println("Erro ao salvar ====>> " + e.getMessage());
        } finally {
            session.close();
        }

    }
  
    public List<BoletoIssMap> retornaBoletos(Date dataInicio, Date dataFim){
        listaBoletos = new ArrayList<BoletoIssMap>();
        session = HibernateUtil.getSessionFactory().openSession();

        Criteria criteria = session.createCriteria(BoletoIssMap.class);
        criteria = criteria.add(Restrictions.ge("dataInicioCompetencia", dataInicio));
        criteria = criteria.add(Restrictions.le("dataFimCompetencia", dataFim));


        if (criteria.list().isEmpty()) {
            //System.out.println("Ususario null");
            //listaBoletos = null;
        } else {
            //System.out.println("AXOUUUU!!!!!");
            listaBoletos = (List<BoletoIssMap>) criteria.list();
        }
        return listaBoletos;
        
    }
    
     public List<BoletoIssMap> retornaBoletosRF(Date dataInicio, Date dataFim){
        listaBoletos = new ArrayList<BoletoIssMap>();
        session = HibernateUtil.getSessionFactory().openSession();

        Criteria criteria = session.createCriteria(BoletoIssMap.class);
        criteria = criteria.add(Restrictions.ge("dataInicioCompetencia", dataInicio));
        criteria = criteria.add(Restrictions.le("dataFimCompetencia", dataFim));
        criteria = criteria.add(Restrictions.eq("ficha", ConfiguracaoSyst.FICHA_RETIDO_FONTE));


        if (criteria.list().isEmpty()) {
            //System.out.println("Ususario null");
            //listaBoletos = null;
        } else {
            //System.out.println("AXOUUUU!!!!!");
            listaBoletos = (List<BoletoIssMap>) criteria.list();
        }
        return listaBoletos;
        
    }
    
    public List<BoletoIssMap> retornaBoletosPorDocumento(String documento){
        listaBoletos = new ArrayList<BoletoIssMap>();
        session = HibernateUtil.getSessionFactory().openSession();

        Criteria criteria = session.createCriteria(BoletoIssMap.class);
        criteria = criteria.add(Restrictions.like("numeroDocumento", documento));        
        

        if (criteria.list().isEmpty()) {
            //System.out.println("Ususario null");
            //listaBoletos = null;
        } else {
            //System.out.println("AXOUUUU!!!!!");
            listaBoletos = (List<BoletoIssMap>) criteria.list();
        }
        return listaBoletos;
    }
    
    public List<BoletoIssMap> retornaBoletosEmAbertoVariavel(Ccm sacado){
        listaBoletos = new ArrayList<BoletoIssMap>();
        session = HibernateUtil.getSessionFactory().openSession();

        Criteria criteria = session.createCriteria(BoletoIssMap.class);
        criteria = criteria.add(Restrictions.eq("sacado", sacado));
        criteria = criteria.add(Restrictions.eq("baixa", false));
        criteria = criteria.add(Restrictions.eq("ficha", ConfiguracaoSyst.FICHA_VARIAVEL));
        criteria = criteria.addOrder(Order.desc("dataDocumento"));

        if (criteria.list().isEmpty()) {
          //  System.out.println("Ususario null");
            //listaBoletos = new ArrayList<BoletoIssMap>();
        } else {
           // System.out.println("AXOUUUU!!!!!");
            listaBoletos = (List<BoletoIssMap>) criteria.list();
        }
        return listaBoletos;
        
    }
    
    public List<BoletoIssMap> retornaBoletosEmAbertoRetidoFonte(Ccm sacado){
        listaBoletos = new ArrayList<BoletoIssMap>();
        session = HibernateUtil.getSessionFactory().openSession();

        Criteria criteria = session.createCriteria(BoletoIssMap.class);
        criteria = criteria.add(Restrictions.eq("sacado", sacado));
        criteria = criteria.add(Restrictions.eq("baixa", false));
        criteria = criteria.add(Restrictions.eq("ficha", ConfiguracaoSyst.FICHA_RETIDO_FONTE));
        criteria = criteria.addOrder(Order.desc("dataDocumento"));

        if (criteria.list().isEmpty()) {
          //  System.out.println("Ususario null");
            //listaBoletos = new ArrayList<BoletoIssMap>();
        } else {
           // System.out.println("AXOUUUU!!!!!");
            listaBoletos = (List<BoletoIssMap>) criteria.list();
        }
        return listaBoletos;
        
    }
    
}
