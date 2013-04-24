/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.dao;

import br.com.nfse.generic.GenericDAO;
import br.com.nfse.jsfuntil.HibernateUtil;
import br.com.nfse.to.Estado;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;

/**
 *
 * @author ThiagoHenrique
 */
public class DAOEstados extends GenericDAO {

    private Session sessao = HibernateUtil.getSessionFactory().openSession();

    public List<Estado> listaEstados() {

        List<Estado> listaEstados = new ArrayList<Estado>();

        try {
            Criteria criteria = sessao.createCriteria(Estado.class);
            return listaEstados = criteria.list();
        } catch (Exception e) {
            return listaEstados = null;
        }
    }
    
    public Estado buscarProCodigo(Integer codigo) {

        return (Estado) sessao.get(Estado.class, codigo);

    }
}
