/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.dao;

import br.com.nfse.generic.GenericDAO;
import br.com.nfse.jsfuntil.HibernateUtil;
import br.com.nfse.to.GeneroAtividade;
import org.hibernate.Session;

/**
 *
 * @author ThiagoHenrique
 */
public class DAOGeneroAtividade extends GenericDAO {
    
    private Session sessao = HibernateUtil.getSessionFactory().openSession();
    
    public GeneroAtividade buscarProCodigo(Integer codigo) {

        return (GeneroAtividade) sessao.get(GeneroAtividade.class, codigo);

    }
    
    
    
}
