/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.dao;

import br.com.nfse.generic.GenericDAO;
import br.com.nfse.jsfuntil.HibernateUtil;
import br.com.nfse.to.Servico;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author ThiagoHenrique
 */
public class DAOServico extends GenericDAO {

    Transaction transacao = null;
    Session session = null;
    Servico servico = null;

    public List<Servico> buscaServicos(String servicos) {


        session = HibernateUtil.getSessionFactory().openSession();

        List<Servico> listaServicos = new ArrayList<Servico>();
        String restricoes[] = servicos.split("\\;");

        for (int i = 0; i < restricoes.length; i++) {
            try {
                if (!restricoes[i].equals("")) {
                    //System.out.println(restricoes[i]);
                    
                    String[] restriMovimento = restricoes[i].split("\\,");
                    
                    Criteria criteria = session.createCriteria(Servico.class);
                    criteria = criteria.add(Restrictions.like("codigoItem", "%" + restriMovimento[0].trim() + "%"));
                    if (criteria.list().isEmpty()) {
                        System.out.println("Ususario null");
                        this.servico = null;
                    } else {
                        this.servico = (Servico) criteria.list().get(0);
                        this.servico.setItemTabela(this.servico.getCodigoItem()+" - "+this.servico.getItemTabela() );
                        
                        if(restriMovimento[1].equals("F")){
                            this.servico.setTipoMovimento("VARIAVEL");
                        }else if(restriMovimento[1].equals("I")){
                            this.servico.setTipoMovimento("ISENTO");
                        }else if(restriMovimento[1].equals("M")){
                            this.servico.setTipoMovimento("PARCELADA");                            
                        }
                                                
                        listaServicos.add(this.servico);
                    }
                } else {
                    System.out.println("SERVIÇO VAZIO!!!!!!!!!");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        session.close();
        return listaServicos;
    }

    public static void main(String args[]) {

        DAOServico daoServico = new DAOServico();

        List<Servico> listaServ = daoServico.buscaServicos("01.01,F;01.02,I;");

        for (Servico servico : listaServ) {
            System.out.println("Servico selecionado>>>>>> " + servico.getDescricao());
            System.out.println("Servico selecionado descricao >>>>>> " + servico.getTipoMovimento());
        }

    }
}
