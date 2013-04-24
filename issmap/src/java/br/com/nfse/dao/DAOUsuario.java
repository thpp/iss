/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.dao;

import br.com.nfse.generic.GenericDAO;
import br.com.nfse.jsfuntil.HibernateUtil;
import br.com.nfse.to.Ccm;
import br.com.nfse.to.Usuario;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Thiago Henrique
 */
public class DAOUsuario extends GenericDAO {

    private Session sessao = HibernateUtil.getSessionFactory().openSession();
    Usuario usuario = new Usuario();

    public Usuario buscarProCodigo(Integer codigo) {

        return (Usuario) sessao.get(Usuario.class, codigo);

    }

    public List<Usuario> listaUsuarios(Ccm ccm) {

        List<Usuario> listaUsuarios = new ArrayList<Usuario>();

        try {
            Criteria criteria = sessao.createCriteria(Usuario.class);
            criteria = criteria.createAlias("ccm", "c");
            criteria = criteria.add(Restrictions.eq("c.id", ccm.getId()));
            criteria = criteria.add(Restrictions.eq("tipoUsuario", 2));
            if (criteria.list().isEmpty()) {
                return listaUsuarios = null;
            } else {
                return listaUsuarios = criteria.list();
            }
        } catch (Exception e) {
            return listaUsuarios = null;
        }
    }
    
     public Usuario buscaUsuarioPorCpf(String cnpj) {

        try {
            Criteria criteria = sessao.createCriteria(Usuario.class);            
            criteria = criteria.add(Restrictions.eq("login", cnpj));
            criteria = criteria.add(Restrictions.eq("tipoUsuario", 2));

            if (criteria.list().isEmpty()) {
                System.out.println("Ususario null");
                this.usuario = null;
            } else {
                this.usuario = (Usuario) criteria.list().get(0);
                //System.out.println("usuario na lista Critaria");
            }
        } catch (Exception e) {
        }
        return this.usuario;

    }

    public Usuario buscaPorCnpj(String cnpj) {

        try {
            Criteria criteria = sessao.createCriteria(Usuario.class);            
            criteria = criteria.add(Restrictions.eq("login", cnpj));

            if (criteria.list().isEmpty()) {
                System.out.println("Ususario null");
                this.usuario = null;
            } else {
                this.usuario = (Usuario) criteria.list().get(0);
                //System.out.println("usuario na lista Critaria");
            }
        } catch (Exception e) {
        }
        return this.usuario;

    }
}
