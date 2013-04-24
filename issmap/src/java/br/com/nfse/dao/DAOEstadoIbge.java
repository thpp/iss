/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.dao;

import br.com.nfse.jsfuntil.HibernateUtil;
import br.com.nfse.to.IBGEEstado;
import org.hibernate.Session;

/**
 *
 * @author ThiagoHenrique
 */
public class DAOEstadoIbge {

    private Session sessao = HibernateUtil.getSessionFactory().openSession();

    public IBGEEstado buscarProCodigo(Integer codigo) {

        return (IBGEEstado) sessao.get(IBGEEstado.class, codigo);

    }
}
