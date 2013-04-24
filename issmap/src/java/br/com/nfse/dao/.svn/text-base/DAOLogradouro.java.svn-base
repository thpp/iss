/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.dao;

import br.com.nfse.generic.GenericDAO;
import br.com.nfse.jsfuntil.HibernateUtil;
import br.com.nfse.to.Bairro;
import br.com.nfse.to.Logradouro;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Thiago Henrique
 */
public class DAOLogradouro extends GenericDAO {

    Transaction transacao = null;
    Session session = null;
    Logradouro log = null;

    public Logradouro buscaPorNomeCep(String cep) throws Exception {

        session = HibernateUtil.getSessionFactory().openSession();

        try {
            Criteria criteria = session.createCriteria(Logradouro.class);
            criteria.add(Restrictions.eq("cep", cep));

            List<Logradouro> list = criteria.list();

            log = list.get(0);

            if (new DAOBairro().buscarBairroPorCidade(log.getBairro().getCidade()).size() == 1) {
                System.out.println("Entrou no metodo somente um bairro");
                log.setNome("");
                Bairro bairro = log.getBairro();
                bairro.setNome("");
                log.setBairro(bairro);
            }
        } catch (Exception e) {
        } 

        return log;
    }
}
