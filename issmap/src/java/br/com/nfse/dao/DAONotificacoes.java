/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.dao;

import br.com.nfse.generic.GenericDAO;
import br.com.nfse.jsfuntil.ConfiguracaoSyst;
import br.com.nfse.to.Notificacoes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.com.nfse.jsfuntil.HibernateUtil;
import br.com.nfse.to.Ccm;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author ThiagoHenrique
 */
public class DAONotificacoes extends GenericDAO {

    Transaction transacao = null;
    Session session = null;

    public String salvarComRetorno(Object obj) throws Exception {
        session = HibernateUtil.getSessionFactory().openSession();
        transacao = session.beginTransaction();
        String retorno;
        try {
            session.saveOrUpdate(obj);
            transacao.commit();
            retorno = "S";
        } catch (Exception e) {
            transacao.rollback();
            // throw new IdadeException("Idade fora da faixa");
            System.out.println("Erro ao salvar ====>> " + e.getMessage());
            retorno = "N";
        } finally {
            session.close();
        }
        return retorno;
    }

    public List<Notificacoes> listaNotificacoes(Ccm ccm) {
        List<Notificacoes> listaNotificacoes = new ArrayList<Notificacoes>();

        session = HibernateUtil.getSessionFactory().openSession();

        try {
            Criteria criteria = session.createCriteria(Notificacoes.class);
            criteria = criteria.add(Restrictions.eq("destinatario", ccm));
            criteria = criteria.addOrder(Order.desc("id"));
            if (criteria.list().isEmpty()) {
                System.out.println("Usuario null");
            } else {
                listaNotificacoes = criteria.list();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        session.close();
        return listaNotificacoes;
    }

    public static void main(String args[]) {

        DAONotificacoes dao = new DAONotificacoes();

        Notificacoes notifica = new Notificacoes();
        try {
            notifica.setDestinatario(new DAOCcm().buscaCadastroPorCnpjCpf("04114569000139"));
            DateFormat dataf = new SimpleDateFormat("dd/MM/yyyy");
            Date data = Calendar.getInstance(Locale.getDefault()).getTime();
            String dataStr = dataf.format(data);

            Date dateGrava = new Date(dataf.parse(dataStr).getTime());
            notifica.setDataEnvio(dateGrava);
            notifica.setRemetente(ConfiguracaoSyst.NOME_CIDADE);
            notifica.setNotificacao("Vai sabrinaaa");
            dao.salvar(notifica);
            System.out.println("Gravadooooooo!!!!");


        } catch (Exception ex) {
            Logger.getLogger(DAONotificacoes.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
