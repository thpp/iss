/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.dao;

import br.com.nfse.generic.GenericDAO;
import br.com.nfse.jsfuntil.HibernateUtil;
import br.com.nfse.to.IBGECidade;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author ThiagoHenrique
 */
public class DAOCidadeIBGE extends GenericDAO {
    
    Transaction transacao = null;
    Session session = null;
    
    public List<IBGECidade> buscarCidadePorNome(String nome){
        
        session = HibernateUtil.getSessionFactory().openSession();

        List<IBGECidade> listaDeCidades = new ArrayList<IBGECidade>();
        
        try {
            Criteria criteria = session.createCriteria(IBGECidade.class);
            criteria = criteria.add(Restrictions.like("nome", "%"+nome+"%").ignoreCase());

            listaDeCidades = criteria.list();
            return listaDeCidades;
        }catch(Exception e){
            return listaDeCidades = null;
        }
    }
    
    public static void main(String args[]){
        List<IBGECidade>listaCidades = new ArrayList<IBGECidade>();
        DAOCidadeIBGE dao = new DAOCidadeIBGE();
        
        listaCidades = dao.buscarCidadePorNome("ourinhos");
        
        for (IBGECidade iBGECidade : listaCidades) {
            System.out.println(iBGECidade.getNome());
        }
        
       
    }
    
}
