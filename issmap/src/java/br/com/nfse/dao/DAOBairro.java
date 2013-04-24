/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.dao;

import br.com.nfse.jsfuntil.HibernateUtil;
import br.com.nfse.to.Bairro;
import br.com.nfse.to.Cidade;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Thiago Henrique
 */
public class DAOBairro {

    Transaction transacao = null;
    Session session = null;

    public List<Bairro> buscarBairroPorCidade(Cidade cidade) throws Exception {
        session = HibernateUtil.getSessionFactory().openSession();

        List<Bairro> listaDeBairros = null;

        try {
            Criteria criteria = session.createCriteria(Bairro.class);
            criteria = criteria.createAlias("cidade", "c");
            criteria = criteria.add(Restrictions.eq("c.codigo", cidade.getCodigo()));

            listaDeBairros = criteria.list();

            return listaDeBairros;

        } catch (Exception e) {
            return listaDeBairros;

        }finally{
            session.close();
        }
    }
}
