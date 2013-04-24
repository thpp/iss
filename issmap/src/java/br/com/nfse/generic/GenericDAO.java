/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.nfse.generic;

import br.com.nfse.dao.DAOCcm;
import br.com.nfse.jsfuntil.HibernateUtil;
import br.com.nfse.to.Ccm;
import java.sql.SQLException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Thiago
 */
public abstract class GenericDAO {

    Transaction transacao = null;
	Session session = null;

        public void salvar(Object obj)throws SQLException{
		session = HibernateUtil.getSessionFactory().openSession();
		transacao = session.beginTransaction();

		try{
			session.saveOrUpdate(obj);
			transacao.commit();
		}catch (Exception e) {
			transacao.rollback(); 
                       // throw new IdadeException("Idade fora da faixa");
                        System.out.println("Erro ao salvar ====>> "+e.getMessage());
		}finally{
			session.close();
		}
                
	}
	public void excluir(Object obj)throws Exception{
		session = HibernateUtil.getSessionFactory().openSession();
		transacao = session.beginTransaction();

		try{
			session.delete(obj);
			transacao.commit();
		}catch (Exception e) {
			transacao.rollback();
                        System.out.println("Erro ao Excluir ====>> "+e.getMessage());
		}finally{
			session.close();
		}
	}
        
        public static void main(String args[]){
            DAOCcm ccmDao = new DAOCcm();
            
            Ccm ccm = new Ccm();
            
            ccm.setNomeRazao("Teste Gravando dao");
        try {
            ccmDao.salvar(ccm);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
            
            
        }
}
