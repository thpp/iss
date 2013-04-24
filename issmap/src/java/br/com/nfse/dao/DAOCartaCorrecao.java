/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.dao;

import br.com.nfse.to.CartaCorrecao;
import br.com.nfse.to.NFSE;


import br.com.nfse.generic.GenericDAO;
import br.com.nfse.jsfuntil.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Thiago
 */
public class DAOCartaCorrecao extends GenericDAO {
    
    private Session sessao = HibernateUtil.getSessionFactory().openSession();
    
    public CartaCorrecao buscaCartaPorNota(NFSE nota)throws Exception{        
        
        try {
            Criteria criteria = sessao.createCriteria(CartaCorrecao.class);
            criteria = criteria.createAlias("nota", "n");
            criteria = criteria.add(Restrictions.eq("n.idBanco", nota.getIdBanco()));
            
            if (criteria.list().isEmpty()) {
                return null;
            } else {
                return (CartaCorrecao) criteria.list().get(0);
            }
        } catch (Exception e) {
            return null;
        }
        
    }
    
    
}
