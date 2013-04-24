/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.backingbean;

import br.com.nfse.dao.DAOCcm;
import br.com.nfse.dao.DAONFSE;
import br.com.nfse.dao.DAONotificacoes;
import br.com.nfse.jsfuntil.AutenticaUsuario;
import br.com.nfse.to.Notificacoes;
import br.com.nfse.to.Usuario;
import br.com.nfse.to.VisaoGeral;
import br.com.nfse.ws.beans.Notificacao;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author ThiagoHenrique
 */
@ManagedBean(name = "MBGrafico")
@ViewScoped
public class GraficoBaking {

    private Usuario currentUser;
    private String notaHojeTomador;
    private String notaHojePrestador;
    private String notaTotalHoje;
    private String notaAntTomador;
    private String notaAntPrestador;
    private String notaTotalAnterior;
    private String notaSeteTomador;
    private String notaSetePrestador;
    private String notaTotalSete;
    private String notaMesTomador;
    private String notaMesPrestador;
    private String notaTotalMes;
    private String template;
    private Boolean exibeNotasParcial = true;
    private String tamanhoPainelNotificacoes = "300px;";
    private String paginatorRows = "3";
    

    public GraficoBaking() {

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();
        this.currentUser = (Usuario) session.getAttribute("usuario");        
        buscaVisaoGeral(this.currentUser.getCcm().getCnpjCpf());
         if(currentUser.getCcm().getCodigoEventualGemmap() != null){
             exibeNotasParcial = false;
             tamanhoPainelNotificacoes = "500px;";
             paginatorRows = "6";
        }
        setColunasGrafico1Semestre();
        setColunasGrafico2Semestre();
        setListaNotificacoes();
        this.template = AutenticaUsuario.verificaTemplate(this.currentUser);
    }
    private List<ColunasGrafico> colunasGrafico1Semestre;
    private List<ColunasGrafico> colunasGrafico2Semestre;

    private void setColunasGrafico1Semestre() {
        DAONFSE daoNfse = new DAONFSE();
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();
        this.currentUser = (Usuario) session.getAttribute("usuario");
        colunasGrafico1Semestre = new ArrayList<ColunasGrafico>();
        try {
            colunasGrafico1Semestre.add(new ColunasGrafico("Janeiro",  daoNfse.buscaQuantidadeNotasPeloMes("01", this.currentUser.getCcm().getCnpjCpf(), "P"), daoNfse.buscaQuantidadeNotasPeloMes("01", this.currentUser.getCcm().getCnpjCpf(), "T")));
            colunasGrafico1Semestre.add(new ColunasGrafico("Fevereiro",daoNfse.buscaQuantidadeNotasPeloMes("02", this.currentUser.getCcm().getCnpjCpf(), "P"), daoNfse.buscaQuantidadeNotasPeloMes("02", this.currentUser.getCcm().getCnpjCpf(), "T")));
            colunasGrafico1Semestre.add(new ColunasGrafico("Março",    daoNfse.buscaQuantidadeNotasPeloMes("03", this.currentUser.getCcm().getCnpjCpf(), "P"), daoNfse.buscaQuantidadeNotasPeloMes("03", this.currentUser.getCcm().getCnpjCpf(), "T")));
            colunasGrafico1Semestre.add(new ColunasGrafico("Abril",    daoNfse.buscaQuantidadeNotasPeloMes("04", this.currentUser.getCcm().getCnpjCpf(), "P"), daoNfse.buscaQuantidadeNotasPeloMes("04", this.currentUser.getCcm().getCnpjCpf(), "T")));
            colunasGrafico1Semestre.add(new ColunasGrafico("Maio",     daoNfse.buscaQuantidadeNotasPeloMes("05", this.currentUser.getCcm().getCnpjCpf(), "P"), daoNfse.buscaQuantidadeNotasPeloMes("05", this.currentUser.getCcm().getCnpjCpf(), "T")));
            colunasGrafico1Semestre.add(new ColunasGrafico("Junho",    daoNfse.buscaQuantidadeNotasPeloMes("06", this.currentUser.getCcm().getCnpjCpf(), "P"), daoNfse.buscaQuantidadeNotasPeloMes("06", this.currentUser.getCcm().getCnpjCpf(), "T")));
        } catch (Exception ex) {
            Logger.getLogger(GraficoBaking.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<ColunasGrafico> getColunasGrafico1Semestre() {
        return colunasGrafico1Semestre;
    }

    public List<ColunasGrafico> getColunasGrafico2Semestre() {
        return colunasGrafico2Semestre;
    }

    private void setColunasGrafico2Semestre() {
        DAONFSE daoNfse = new DAONFSE();
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();
        this.currentUser = (Usuario) session.getAttribute("usuario");
        colunasGrafico2Semestre = new ArrayList<ColunasGrafico>();
        try {
            colunasGrafico2Semestre.add(new ColunasGrafico("Julho", daoNfse.buscaQuantidadeNotasPeloMes("07", this.currentUser.getCcm().getCnpjCpf(), "P"), daoNfse.buscaQuantidadeNotasPeloMes("07", this.currentUser.getCcm().getCnpjCpf(), "T")));
            colunasGrafico2Semestre.add(new ColunasGrafico("Agosto", daoNfse.buscaQuantidadeNotasPeloMes("08", this.currentUser.getCcm().getCnpjCpf(), "P"), daoNfse.buscaQuantidadeNotasPeloMes("08", this.currentUser.getCcm().getCnpjCpf(), "T")));
            colunasGrafico2Semestre.add(new ColunasGrafico("Setembro", daoNfse.buscaQuantidadeNotasPeloMes("09", this.currentUser.getCcm().getCnpjCpf(), "P"), daoNfse.buscaQuantidadeNotasPeloMes("09", this.currentUser.getCcm().getCnpjCpf(), "T")));
            colunasGrafico2Semestre.add(new ColunasGrafico("Outubro", daoNfse.buscaQuantidadeNotasPeloMes("10", this.currentUser.getCcm().getCnpjCpf(), "P"), daoNfse.buscaQuantidadeNotasPeloMes("10", this.currentUser.getCcm().getCnpjCpf(), "T")));
            colunasGrafico2Semestre.add(new ColunasGrafico("Novenbro", daoNfse.buscaQuantidadeNotasPeloMes("11", this.currentUser.getCcm().getCnpjCpf(), "P"), daoNfse.buscaQuantidadeNotasPeloMes("11", this.currentUser.getCcm().getCnpjCpf(), "T")));
            colunasGrafico2Semestre.add(new ColunasGrafico("Dezembro", daoNfse.buscaQuantidadeNotasPeloMes("12", this.currentUser.getCcm().getCnpjCpf(), "P"), daoNfse.buscaQuantidadeNotasPeloMes("12", this.currentUser.getCcm().getCnpjCpf(), "T")));
        } catch (Exception ex) {
            Logger.getLogger(GraficoBaking.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // daki pra frente dados da visão geral.
    List<Notificacoes> listaNotificacoes;

    public List<Notificacoes> getListaNotificacoes() {
        return listaNotificacoes;
    }

    private void setListaNotificacoes() {

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();
        this.currentUser = (Usuario) session.getAttribute("usuario");
        DateFormat formata = new SimpleDateFormat("dd/MM/yyyy");
        DAONotificacoes daoNotifica = new DAONotificacoes();
        Notificacoes notifica;
        
        try {
            List<Notificacoes> lista = new ArrayList<Notificacoes>();
            for (Notificacoes notificacoes : daoNotifica.listaNotificacoes(this.currentUser.getCcm())) {
                notifica = new Notificacoes();
                notifica = notificacoes;
                notifica.setDataString(formata.format(notificacoes.getDataEnvio()));
                lista.add(notifica);
            }
            this.listaNotificacoes = lista;
        } catch (Exception ex) {
            Logger.getLogger(DAONFSE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void buscaVisaoGeral(String cpfCnpj) {

        DAONFSE daoNfse = new DAONFSE();
        try {
            
            VisaoGeral visaoHoje = daoNfse.buscaQuantidadeNotasHoje(cpfCnpj);            
            VisaoGeral visaoDiaAnterior = daoNfse.buscaQuantidadeNotasDiaAnterior(cpfCnpj);            
            VisaoGeral visaoUltimosSeledias = daoNfse.buscaQuantidadeNotasUltSeteDias(cpfCnpj);
            VisaoGeral visaoMesVigente = daoNfse.buscaQuantidadeNotasMesVigente(cpfCnpj);
            
            this.notaHojePrestador = visaoHoje.getQuantidade();
            this.notaHojeTomador = visaoHoje.getValorTotalNota();
            this.notaTotalHoje = visaoHoje.getIssDevido();



            this.notaAntPrestador = visaoDiaAnterior.getQuantidade();
            this.notaAntTomador = visaoDiaAnterior.getValorTotalNota();
            this.notaTotalAnterior = visaoDiaAnterior.getIssDevido();

            this.notaSetePrestador = visaoUltimosSeledias.getQuantidade();
            this.notaSeteTomador = visaoUltimosSeledias.getValorTotalNota();
            this.notaTotalSete = visaoUltimosSeledias.getIssDevido();

            this.notaMesPrestador = visaoMesVigente.getQuantidade();
            this.notaMesTomador = visaoMesVigente.getValorTotalNota();
            this.notaTotalMes = visaoMesVigente.getIssDevido();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }


    }

    public String getNotaAntPrestador() {
        return notaAntPrestador;
    }

    public void setNotaAntPrestador(String notaAntPrestador) {
        this.notaAntPrestador = notaAntPrestador;
    }

    public String getNotaAntTomador() {
        return notaAntTomador;
    }

    public void setNotaAntTomador(String notaAntTomador) {
        this.notaAntTomador = notaAntTomador;
    }

    public String getNotaHojePrestador() {
        return notaHojePrestador;
    }

    public void setNotaHojePrestador(String notaHojePrestador) {
        this.notaHojePrestador = notaHojePrestador;
    }

    public String getNotaHojeTomador() {
        return notaHojeTomador;
    }

    public void setNotaHojeTomador(String notaHojeTomador) {
        this.notaHojeTomador = notaHojeTomador;
    }

    public String getNotaMesPrestador() {
        return notaMesPrestador;
    }

    public void setNotaMesPrestador(String notaMesPrestador) {
        this.notaMesPrestador = notaMesPrestador;
    }

    public String getNotaMesTomador() {
        return notaMesTomador;
    }

    public void setNotaMesTomador(String notaMesTomador) {
        this.notaMesTomador = notaMesTomador;
    }

    public String getNotaSetePrestador() {
        return notaSetePrestador;
    }

    public void setNotaSetePrestador(String notaSetePrestador) {
        this.notaSetePrestador = notaSetePrestador;
    }

    public String getNotaSeteTomador() {
        return notaSeteTomador;
    }

    public void setNotaSeteTomador(String notaSeteTomador) {
        this.notaSeteTomador = notaSeteTomador;
    }

    public String getNotaTotalAnterior() {
        return notaTotalAnterior;
    }

    public void setNotaTotalAnterior(String notaTotalAnterior) {
        this.notaTotalAnterior = notaTotalAnterior;
    }

    public String getNotaTotalHoje() {
        return notaTotalHoje;
    }

    public void setNotaTotalHoje(String notaTotalHoje) {
        this.notaTotalHoje = notaTotalHoje;
    }

    public String getNotaTotalMes() {
        return notaTotalMes;
    }

    public void setNotaTotalMes(String notaTotalMes) {
        this.notaTotalMes = notaTotalMes;
    }

    public String getNotaTotalSete() {
        return notaTotalSete;
    }

    public void setNotaTotalSete(String notaTotalSete) {
        this.notaTotalSete = notaTotalSete;
    }
    
    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Boolean getExibeNotasParcial() {
        return exibeNotasParcial;
    }

    public void setExibeNotasParcial(Boolean exibeNotasParcial) {
        this.exibeNotasParcial = exibeNotasParcial;
    }

    public String getTamanhoPainelNotificacoes() {
        return tamanhoPainelNotificacoes;
    }

    public void setTamanhoPainelNotificacoes(String tamanhoPainelNotificacoes) {
        this.tamanhoPainelNotificacoes = tamanhoPainelNotificacoes;
    }

    public String getPaginatorRows() {
        return paginatorRows;
    }

    public void setPaginatorRows(String paginatorRows) {
        this.paginatorRows = paginatorRows;
    }
}