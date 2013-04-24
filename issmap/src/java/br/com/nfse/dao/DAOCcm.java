/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.dao;

import br.com.nfse.generic.GenericDAO;
import br.com.nfse.jsfuntil.HibernateUtil;
import br.com.nfse.to.Ccm;
import java.sql.SQLException;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Thiago Henrique
 */
public class DAOCcm extends GenericDAO {

    private Session session = null;
    private Transaction transacao = null;
    Ccm ccm = null;
    

    public Ccm salvarComRetorno(Ccm obj) throws SQLException {
        session = HibernateUtil.getSessionFactory().openSession();
        transacao = session.beginTransaction();
        Ccm ccmRetorno = null;
        try {
            session.saveOrUpdate(obj);
            transacao.commit();
            ccmRetorno = obj;
        } catch (Exception e) {
            transacao.rollback();
            System.out.println("Erro ao salvar ====>> " + e.getMessage());
        } finally {
            session.close();
        }
        return ccmRetorno;
    }

    public Ccm buscaCadastroPorCnpjCpf(String cnpjCpf) throws Exception {

        session = HibernateUtil.getSessionFactory().openSession();

        Criteria criteria = session.createCriteria(Ccm.class);
        criteria = criteria.add(Restrictions.eq("cnpjCpf", cnpjCpf));

        if (criteria.list().isEmpty()) {
            System.out.println("Ususario null");
            this.ccm = null;
        } else {
            this.ccm = (Ccm) criteria.list().get(0);
        }
        // colocar restrição de que ele é realmente um contador, talvez telo crea... 
        return ccm;
    }
    
    public Ccm buscaContadorPorCnpjCpf(String cnpjCpf) throws Exception {

        session = HibernateUtil.getSessionFactory().openSession();

        Criteria criteria = session.createCriteria(Ccm.class);
        criteria = criteria.add(Restrictions.eq("cnpjCpf", cnpjCpf));
        criteria = criteria.add(Restrictions.eq("contadorProfissional", true));

        if (criteria.list().isEmpty()) {
            System.out.println("Ususario null");
            this.ccm = null;
        } else {
            this.ccm = (Ccm) criteria.list().get(0);
        }
        // colocar restrição de que ele é realmente um contador, talvez telo crea... 
        return ccm;
    }

    public Ccm buscaPorCnpjCpf(String cnpjCpf) {

        session = HibernateUtil.getSessionFactory().openSession();

        Ccm tomador = null;

        try {
            Criteria criteria = session.createCriteria(Ccm.class);
            criteria = criteria.add(Restrictions.eq("cnpjCpf", cnpjCpf));

            if (criteria.list().isEmpty()) {
                System.out.println("Ususario null");
                tomador = null;
            } else {
                tomador = (Ccm) criteria.list().get(0);
                //System.out.println(tomador.getCnpjCpf());
            }
        } catch (Exception e) {
        }


        return tomador;
    }
    
    
    

    public Boolean verificaNotificacao(Integer codigo) {

        Ccm ccm = (Ccm) session.get(Ccm.class, codigo);        
        return ccm.getNotificacaoGemmap();
        
    }

    public void salvarMerge(Ccm obj) throws SQLException {
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

    public static void main(String args[]) {

        DAOCcm daoCcm = new DAOCcm();



    }
}
