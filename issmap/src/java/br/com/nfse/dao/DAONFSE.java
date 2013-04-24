/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.dao;

import br.com.nfse.generic.GenericDAO;
import br.com.nfse.jsfuntil.HibernateUtil;
import br.com.nfse.to.NFSE;
import br.com.nfse.to.VisaoGeral;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author ThiagoHenrique
 */
public class DAONFSE extends GenericDAO {

    private Session session = null;
    private Transaction transacao = null;
    private NFSE nfse = new NFSE();

    public NFSE buscaNotaTomador(String cpfCnpjTomador, String codigoVerifica, String cpfCnpjPrestador) throws Exception {

        session = HibernateUtil.getSessionFactory().openSession();

        Criteria criteria = session.createCriteria(NFSE.class);
        criteria = criteria.add(Restrictions.eq("cpfCnpjTomador", cpfCnpjTomador));
        criteria = criteria.add(Restrictions.eq("cogigoVerifica", codigoVerifica));
        criteria = criteria.add(Restrictions.eq("cpfCnpjPrestador", cpfCnpjPrestador));


        if (criteria.list().isEmpty()) {
            System.out.println("Ususario null");
            this.nfse = null;
        } else {
            System.out.println("AXOUUUU!!!!!");
            this.nfse = (NFSE) criteria.list().get(0);
        }

        return nfse;
    }

    public NFSE buscaNota(String cpfCnpjPrestador, String codigoVerifica) throws Exception {

        session = HibernateUtil.getSessionFactory().openSession();

        Criteria criteria = session.createCriteria(NFSE.class);
        criteria = criteria.add(Restrictions.eq("cpfCnpjPrestador", cpfCnpjPrestador));
        criteria = criteria.add(Restrictions.eq("cogigoVerifica", codigoVerifica));


        if (criteria.list().isEmpty()) {
            System.out.println("Ususario null");
            this.nfse = null;
        } else {
            System.out.println("AXOUUUU!!!!!");
            this.nfse = (NFSE) criteria.list().get(0);
        }

        return nfse;
    }

    public List<NFSE> buscaNotasAbertasPrestador(String cpfCnpjPrestador) throws Exception {

        List<NFSE> listaNotas = new ArrayList<NFSE>();

        session = HibernateUtil.getSessionFactory().openSession();

        Criteria criteria = session.createCriteria(NFSE.class);
        criteria = criteria.add(Restrictions.eq("cpfCnpjPrestador", cpfCnpjPrestador));
        criteria = criteria.add(Restrictions.isNull("boletoPagamento"));
        criteria = criteria.addOrder(Order.desc("dataEmissao"));

        if (criteria.list().isEmpty()) {
            System.out.println("Ususario null");
        } else {
            System.out.println("AXOUUUU!!!!!");
            listaNotas = criteria.list();
        }

        return listaNotas;
    }
    
        public List<NFSE> buscaNotasAbertasRetidoFonte(String cpfCnpjTomador) throws Exception {

        List<NFSE> listaNotas = new ArrayList<NFSE>();

        session = HibernateUtil.getSessionFactory().openSession();

        Criteria criteria = session.createCriteria(NFSE.class);
        criteria = criteria.add(Restrictions.eq("cpfCnpjTomador", cpfCnpjTomador));
        criteria = criteria.add(Restrictions.isNull("boletoPagamento"));
        criteria = criteria.add(Restrictions.eq("notaTerceiro", true));
        criteria = criteria.addOrder(Order.desc("dataEmissao"));
        
        listaNotas = criteria.list();
        
        if (listaNotas.isEmpty()) {
            System.out.println("Ususario null");
        } else {
            System.out.println("AXOUUUU!!!!!");
            
        }

        return listaNotas;
    }
        
        public NFSE buscaNotaRetidoFonte(String codigo) throws Exception {

        NFSE nota = new NFSE();

        session = HibernateUtil.getSessionFactory().openSession();

        Criteria criteria = session.createCriteria(NFSE.class);
        criteria = criteria.add(Restrictions.eq("idBanco", Integer.parseInt(codigo)));
        criteria = criteria.add(Restrictions.isNull("boletoPagamento"));
        criteria = criteria.add(Restrictions.eq("notaTerceiro", true));
        
        nota = (NFSE) criteria.list().get(0);
        
        if (nota == null) {
            System.out.println("Ususario null");
        } else {
            System.out.println("AXOUUUU!!!!!");
            
        }

        return nota;
    }
        
        public List<NFSE> buscaNotasAbertasRetidoFonte(String cpfCnpjTomador, Date dataIniRF, Date dataFimRF) throws Exception {

        List<NFSE> listaNotas = new ArrayList<NFSE>();

        session = HibernateUtil.getSessionFactory().openSession();

        Criteria criteria = session.createCriteria(NFSE.class);
        criteria = criteria.add(Restrictions.eq("cpfCnpjTomador", cpfCnpjTomador));
        criteria = criteria.add(Restrictions.isNull("boletoPagamento"));
        criteria = criteria.add(Restrictions.eq("notaTerceiro", true));
        criteria.add(Restrictions.between("dataEmissao", dataIniRF, dataFimRF));
        criteria = criteria.addOrder(Order.desc("dataEmissao"));
        
        listaNotas = criteria.list();
        
        if (listaNotas.isEmpty()) {
            System.out.println("Ususario null");
        } else {
            System.out.println("AXOUUUU!!!!!");
            
        }

        return listaNotas;
    }
        
         public List<NFSE> buscaNotasRetidoFonte(String cpfCnpjTomador, Date dataIniRF, Date dataFimRF) throws Exception {

        List<NFSE> listaNotas = new ArrayList<NFSE>();

        session = HibernateUtil.getSessionFactory().openSession();

        Criteria criteria = session.createCriteria(NFSE.class);
        criteria = criteria.add(Restrictions.eq("cpfCnpjTomador", cpfCnpjTomador));
        //criteria = criteria.add(Restrictions.isNull("boletoPagamento"));
        criteria = criteria.add(Restrictions.eq("notaTerceiro", true));
        criteria.add(Restrictions.between("dataEmissao", dataIniRF, dataFimRF));
        criteria = criteria.addOrder(Order.desc("dataEmissao"));
        
        listaNotas = criteria.list();
        
        if (listaNotas.isEmpty()) {
            System.out.println("Ususario null");
        } else {
            System.out.println("AXOUUUU!!!!!");
            
        }

        return listaNotas;
    }        

    public Long numeroUltimaNota(String cpfCnpjPrestador) {
        
        session = HibernateUtil.getSessionFactory().openSession();
        int retorno = 0;        
        Criteria criteria = session.createCriteria(NFSE.class);
        criteria = criteria.add(Restrictions.eq("cpfCnpjPrestador", cpfCnpjPrestador));
        criteria = criteria.add(Restrictions.eq("notaTerceiro", false));
        Long maxNumero = (Long) criteria.setProjection(Projections.max("numeroNota")).uniqueResult();
        
        if(maxNumero == null){
            return 0L;
        }else{
           return maxNumero; 
        }
    }

    public VisaoGeral buscaQuantidadeNotasHoje(String cpfCnpjPrestador) throws Exception {

        DateFormat dataf = new SimpleDateFormat("dd/MM/yyyy");
        Date data = Calendar.getInstance(Locale.getDefault()).getTime();
        String dataStr = dataf.format(data);

        Date dateGrava = new Date(dataf.parse(dataStr).getTime());

        session = HibernateUtil.getSessionFactory().openSession();


        Double valorNotas = 0.0;
        Double valorIssNotas = 0.0;
        Integer quantidadeNota = 0;

        Criteria criteriaQuantidadeNotas = session.createCriteria(NFSE.class);
        criteriaQuantidadeNotas = criteriaQuantidadeNotas.add(Restrictions.like("cpfCnpjPrestador", cpfCnpjPrestador));
        criteriaQuantidadeNotas.add(Restrictions.eq("dataEmissao", dateGrava));
        criteriaQuantidadeNotas.setProjection(Projections.rowCount());


        Criteria criteriaValorNotas = session.createCriteria(NFSE.class);
        criteriaValorNotas = criteriaValorNotas.add(Restrictions.eq("cpfCnpjPrestador", cpfCnpjPrestador));
        criteriaValorNotas.add(Restrictions.eq("dataEmissao", dateGrava));
        criteriaValorNotas.setProjection(Projections.sum("valorBaseCalculo"));

        try {
            quantidadeNota = ((Integer) criteriaQuantidadeNotas.list().get(0)).intValue();
            valorNotas = ((Double) criteriaValorNotas.list().get(0)).doubleValue();
        } catch (NullPointerException e) {
            VisaoGeral visao = new VisaoGeral();

            visao.setIssDevido(valorIssNotas.toString());
            visao.setQuantidade(quantidadeNota.toString());
            visao.setValorTotalNota(valorNotas.toString());

            return visao;
        }


//        Criteria criteriaValorIssNotas = session.createCriteria(NFSE.class);
//        criteriaValorIssNotas = criteriaValorIssNotas.add(Restrictions.eq("cpfCnpjPrestador", cpfCnpjPrestador));
//        criteriaValorIssNotas.add(Restrictions.eq("dataEmissao", dateGrava));
//        criteriaValorIssNotas.setProjection(Projections.sum("valorIss"));
//        
//        valorIssNotas = ((Double) criteriaValorIssNotas.list().get(0)).doubleValue();

        VisaoGeral visao = new VisaoGeral();

        visao.setIssDevido(valorIssNotas.toString());
        visao.setQuantidade(quantidadeNota.toString());
        visao.setValorTotalNota(valorNotas.toString());

        return visao;

    }

    public VisaoGeral buscaQuantidadeNotasDiaAnterior(String cpfCnpjPrestador) throws Exception {

        Date hoje = new Date();
        int dias_a_avancar = -1; // se quiser diminuir, basta por -1  
        Date diaAnterior = new Date(hoje.getTime() + ((1000 * 24 * 60 * 60) * dias_a_avancar));


        session = HibernateUtil.getSessionFactory().openSession();
        Double valorNotas = 0.0;
        Double valorIssNotas = 0.0;
        Integer quantidadeNota = 0;

        Criteria criteriaQuantidadeNotas = session.createCriteria(NFSE.class);
        criteriaQuantidadeNotas = criteriaQuantidadeNotas.add(Restrictions.like("cpfCnpjPrestador", cpfCnpjPrestador));
        criteriaQuantidadeNotas.add(Restrictions.eq("dataEmissao", diaAnterior));
        criteriaQuantidadeNotas.setProjection(Projections.rowCount());

        Criteria criteriaValorNotas = session.createCriteria(NFSE.class);
        criteriaValorNotas = criteriaValorNotas.add(Restrictions.eq("cpfCnpjPrestador", cpfCnpjPrestador));
        criteriaValorNotas.add(Restrictions.eq("dataEmissao", diaAnterior));
        criteriaValorNotas.setProjection(Projections.sum("valorBaseCalculo"));

        try {
            quantidadeNota = ((Integer) criteriaQuantidadeNotas.list().get(0)).intValue();
            valorNotas = ((Double) criteriaValorNotas.list().get(0)).doubleValue();
        } catch (NullPointerException e) {
            VisaoGeral visao = new VisaoGeral();

            visao.setIssDevido(valorIssNotas.toString());
            visao.setQuantidade(quantidadeNota.toString());
            visao.setValorTotalNota(valorNotas.toString());

            return visao;
        }


//        Criteria criteriaValorIssNotas = session.createCriteria(NFSE.class);
//        criteriaValorIssNotas = criteriaValorIssNotas.add(Restrictions.eq("cpfCnpjPrestador", cpfCnpjPrestador));
//        criteriaValorIssNotas.add(Restrictions.eq("dataEmissao", dateGrava));
//        criteriaValorIssNotas.setProjection(Projections.sum("valorIss"));
//        
//        valorIssNotas = ((Double) criteriaValorIssNotas.list().get(0)).doubleValue();

        VisaoGeral visao = new VisaoGeral();

        visao.setIssDevido(valorIssNotas.toString());
        visao.setQuantidade(quantidadeNota.toString());
        visao.setValorTotalNota(valorNotas.toString());

        return visao;

    }

    public VisaoGeral buscaQuantidadeNotasUltSeteDias(String cpfCnpjPrestador) throws Exception {

        DateFormat dataf = new SimpleDateFormat("dd/MM/yyyy");
        Date data = Calendar.getInstance(Locale.getDefault()).getTime();
        String dataStr = dataf.format(data);

        Date dateFim = new Date(dataf.parse(dataStr).getTime());

        Date hoje = new Date();
        int dias_a_avancar = -7; // se quiser diminuir, basta por -2  
        Date dataInicio = new Date(hoje.getTime() + ((1000 * 24 * 60 * 60) * dias_a_avancar));

        session = HibernateUtil.getSessionFactory().openSession();

        Double valorNotas = 0.0;
        Double valorIssNotas = 0.0;
        Integer quantidadeNota = 0;

        Criteria criteriaQuantidadeNotas = session.createCriteria(NFSE.class);
        criteriaQuantidadeNotas = criteriaQuantidadeNotas.add(Restrictions.like("cpfCnpjPrestador", cpfCnpjPrestador));
        criteriaQuantidadeNotas.add(Restrictions.between("dataEmissao", dataInicio, dateFim));
        criteriaQuantidadeNotas.setProjection(Projections.rowCount());

        Criteria criteriaValorNotas = session.createCriteria(NFSE.class);
        criteriaValorNotas = criteriaValorNotas.add(Restrictions.eq("cpfCnpjPrestador", cpfCnpjPrestador));
        criteriaValorNotas.add(Restrictions.between("dataEmissao", dataInicio, dateFim));
        criteriaValorNotas.setProjection(Projections.sum("valorBaseCalculo"));

        try {
            quantidadeNota = ((Integer) criteriaQuantidadeNotas.list().get(0)).intValue();
            valorNotas = ((Double) criteriaValorNotas.list().get(0)).doubleValue();
        } catch (NullPointerException e) {
            VisaoGeral visao = new VisaoGeral();

            visao.setIssDevido(valorIssNotas.toString());
            visao.setQuantidade(quantidadeNota.toString());
            visao.setValorTotalNota(valorNotas.toString());

            return visao;
        }


//        Criteria criteriaValorIssNotas = session.createCriteria(NFSE.class);
//        criteriaValorIssNotas = criteriaValorIssNotas.add(Restrictions.eq("cpfCnpjPrestador", cpfCnpjPrestador));
//        criteriaValorIssNotas.add(Restrictions.eq("dataEmissao", dateGrava));
//        criteriaValorIssNotas.setProjection(Projections.sum("valorIss"));
//        
//        valorIssNotas = ((Double) criteriaValorIssNotas.list().get(0)).doubleValue();

        VisaoGeral visao = new VisaoGeral();

        visao.setIssDevido(valorIssNotas.toString());
        visao.setQuantidade(quantidadeNota.toString());
        visao.setValorTotalNota(valorNotas.toString());

        return visao;

    }

    public VisaoGeral buscaQuantidadeNotasMesVigente(String cpfCnpjPrestador) throws Exception {

        DateFormat dataf = new SimpleDateFormat("dd/MM/yyyy");
        Date data = Calendar.getInstance(Locale.getDefault()).getTime();
        String dataStr = dataf.format(data);

        Date dateFim = new Date(dataf.parse(dataStr).getTime());


        Calendar calendar = Calendar.getInstance();
        int lastDate = calendar.getActualMinimum(Calendar.DATE);
        calendar.set(Calendar.DATE, lastDate);
        String dataIStr = dataf.format(calendar.getTime());

        Date dateInicio = new Date(dataf.parse(dataIStr).getTime());

        session = HibernateUtil.getSessionFactory().openSession();

        Double valorNotas = 0.0;
        Double valorIssNotas = 0.0;
        Integer quantidadeNota = 0;

        Criteria criteriaQuantidadeNotas = session.createCriteria(NFSE.class);
        criteriaQuantidadeNotas = criteriaQuantidadeNotas.add(Restrictions.like("cpfCnpjPrestador", cpfCnpjPrestador));
        criteriaQuantidadeNotas.add(Restrictions.between("dataEmissao", dateInicio, dateFim));
        criteriaQuantidadeNotas.setProjection(Projections.rowCount());

        Criteria criteriaValorNotas = session.createCriteria(NFSE.class);
        criteriaValorNotas = criteriaValorNotas.add(Restrictions.eq("cpfCnpjPrestador", cpfCnpjPrestador));
        criteriaValorNotas.add(Restrictions.between("dataEmissao", dateInicio, dateFim));
        criteriaValorNotas.setProjection(Projections.sum("valorBaseCalculo"));

        try {
            quantidadeNota = ((Integer) criteriaQuantidadeNotas.list().get(0)).intValue();
            valorNotas = ((Double) criteriaValorNotas.list().get(0)).doubleValue();
        } catch (NullPointerException e) {
            VisaoGeral visao = new VisaoGeral();

            visao.setIssDevido(valorIssNotas.toString());
            visao.setQuantidade(quantidadeNota.toString());
            visao.setValorTotalNota(valorNotas.toString());

            return visao;
        }


//        Criteria criteriaValorIssNotas = session.createCriteria(NFSE.class);
//        criteriaValorIssNotas = criteriaValorIssNotas.add(Restrictions.eq("cpfCnpjPrestador", cpfCnpjPrestador));
//        criteriaValorIssNotas.add(Restrictions.eq("dataEmissao", dateGrava));
//        criteriaValorIssNotas.setProjection(Projections.sum("valorIss"));
//        
//        valorIssNotas = ((Double) criteriaValorIssNotas.list().get(0)).doubleValue();

        VisaoGeral visao = new VisaoGeral();

        visao.setIssDevido(valorIssNotas.toString());
        visao.setQuantidade(quantidadeNota.toString());
        visao.setValorTotalNota(valorNotas.toString());

        return visao;

    }

    public Integer buscaQuantidadeNotasPeloMes(String mes, String cpfCnpjPrestador, String tipoNota) throws Exception {

        //pega a data atual em string
        DateFormat dataf = new SimpleDateFormat("dd/MM/yyyy");

        //?
        //Date dateFim = new Date(dataf.parse(dataStr).getTime());

        //Define a data de inicio
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(calendar.YEAR);
        String dataIStr = "01/" + mes + "/" + String.valueOf(year);
        Date dateInicio = new Date(dataf.parse(dataIStr).getTime());

        //Define a data final
        //calendar.set(calendar.MONTH, Integer.parseInt(mes));
        calendar.setTime(dateInicio);
        int lastDate = calendar.getActualMaximum(calendar.DATE);
        calendar.set(Calendar.DATE, lastDate);
        String dataFStr = dataf.format(calendar.getTime());
        Date dateFim = new Date(dataf.parse(dataFStr).getTime());

        session = HibernateUtil.getSessionFactory().openSession();

        Criteria criteria = session.createCriteria(NFSE.class);
        if (tipoNota.equals("P")) {
            criteria = criteria.add(Restrictions.eq("cpfCnpjPrestador", cpfCnpjPrestador));
        } else if (tipoNota.equals("T")) {
            criteria = criteria.add(Restrictions.eq("cpfCnpjTomador", cpfCnpjPrestador));
        }

        criteria.add(Restrictions.between("dataEmissao", dateInicio, dateFim));
        criteria.setProjection(Projections.rowCount());
        return ((Integer) criteria.list().get(0)).intValue();

    }

    public List<NFSE> buscaNotasPeloMes(String mes, String cpfCnpjPrestador, String tipoNota) throws Exception {

        //pega a data atual em string
        DateFormat dataf = new SimpleDateFormat("dd/MM/yyyy");

        //?
        //Date dateFim = new Date(dataf.parse(dataStr).getTime());

        //Define a data de inicio
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(calendar.YEAR);
        String dataIStr = "01/" + mes + "/" + String.valueOf(year);
        Date dateInicio = new Date(dataf.parse(dataIStr).getTime());

        //Define a data final
        //calendar.set(calendar.MONTH, Integer.parseInt(mes));
        calendar.setTime(dateInicio);
        int lastDate = calendar.getActualMaximum(calendar.DATE);
        calendar.set(Calendar.DATE, lastDate);
        String dataFStr = dataf.format(calendar.getTime());
        Date dateFim = new Date(dataf.parse(dataFStr).getTime());

        session = HibernateUtil.getSessionFactory().openSession();

        Criteria criteria = session.createCriteria(NFSE.class);
        if (tipoNota.equals("P")) {
            criteria = criteria.add(Restrictions.eq("cpfCnpjPrestador", cpfCnpjPrestador));
        } else if (tipoNota.equals("T")) {
            criteria = criteria.add(Restrictions.eq("cpfCnpjTomador", cpfCnpjPrestador));
        }

        criteria.add(Restrictions.between("dataEmissao", dateInicio, dateFim));
        return criteria.list();

    }

    public List<NFSE> buscaNotasPorFiltroBoleto(String cnpjCpfCcm, String cnpjCpf, String nomeRazao, Date dataIni, Date dataFim, String tipo) throws Exception {
        // List<NFSE> notas = new ArrayList<NFSE>();

        session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(NFSE.class);


        //PRESTADOR
        if (tipo.equals("P")) {

            criteria = criteria.add(Restrictions.eq("cpfCnpjPrestador", cnpjCpfCcm));
            criteria = criteria.add(Restrictions.isNull("boletoPagamento"));

            if (!cnpjCpf.equals("")) {
                criteria = criteria.add(Restrictions.eq("cpfCnpjTomador", cnpjCpf));
            }
            if (!nomeRazao.equals("")) {
                criteria = criteria.add(Restrictions.like("nomeRazaoTomador", "%" + nomeRazao + "%"));
            }
            if ((dataIni != null) && (dataFim != null)) {
                criteria.add(Restrictions.between("dataEmissao", dataIni, dataFim));
            }
            criteria = criteria.addOrder(Order.asc("dataEmissao"));
        }
        return criteria.list();
    }        

    public List<NFSE> buscaNotasPorFiltro(String cnpjCpfCcm, String cnpjCpf, String nomeRazao, Date dataIni, Date dataFim, String tipo) throws Exception {
        // List<NFSE> notas = new ArrayList<NFSE>();

        session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(NFSE.class);
        //criteria.add(Restrictions.eq("cancelada", false));

        //PRESTADOR
        if (tipo.equals("P")) {

            criteria = criteria.add(Restrictions.eq("cpfCnpjPrestador", cnpjCpfCcm));

            if (!cnpjCpf.equals("")) {
                criteria = criteria.add(Restrictions.eq("cpfCnpjTomador", cnpjCpf));
            }
            if (!nomeRazao.equals("")) {
                criteria = criteria.add(Restrictions.like("nomeRazaoTomador", "%" + nomeRazao + "%"));
            }
            if ((dataIni != null) && (dataFim != null)) {

                criteria.add(Restrictions.between("dataEmissao", dataIni, dataFim));
            }

            //TOMADOR    
        } else if (tipo.equals("T")) {

            criteria = criteria.add(Restrictions.eq("cpfCnpjTomador", cnpjCpfCcm));            
            criteria = criteria.add(Restrictions.eq("notaTerceiro", false));

            if (!cnpjCpf.equals("")) {
                criteria = criteria.add(Restrictions.eq("cpfCnpjPrestador", cnpjCpf));
            }
            if (!nomeRazao.equals("")) {
                criteria = criteria.add(Restrictions.like("nomeRazaoPrestador", "%" + nomeRazao + "%"));
            }
            if ((dataIni != null) && (dataFim != null)) {

                criteria.add(Restrictions.between("dataEmissao", dataIni, dataFim));
            }
        }

        return criteria.list();
    }

    public static void main(String args[]) {

        DAONFSE daoNfse = new DAONFSE();
        try {
           NFSE listaNotas = daoNfse.buscaNotaRetidoFonte("155");

            System.out.println(listaNotas.getCpfCnpjTomador());
        } catch (Exception ex) {
            System.out.println("ERRO "+ex);
        }
    }
}
