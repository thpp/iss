/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.backingbean;

import br.com.nfse.dao.DAOCcm;
import br.com.nfse.to.Ccm;
import br.com.nfse.to.Usuario;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Thiago Henrique
 */
@ManagedBean(name = "MBContador")
@ViewScoped
public class AtribuiContador implements java.io.Serializable {

    private String cnpjCpf;
    private String razaoContador;
    private Ccm contador = null;
    private Ccm ccm = null;
    private String permicoes = "";
    private Usuario currentUser;

    /*Permição enviar e-mail automaticamente ao meu contador a cada NF-e emitida corresponde a 1, */
    private boolean isEnviarEmailContador;
    /*Permoção Autorizo meu contador a converta meus RPS em NF-e corresponde a 2, */
    private boolean isConverterRPS;
    /*Permoção Autorizo meu contador a cancelar as NF-e emitidas corresponde a 3, */
    private boolean isCancelarNF;
    /*Permoção Autorizo meu contador a consultar as NF-e emitidas corresponde a 4, */
    private boolean isConsultarNF;
    /*Permoção Autorizo meu contador a emitir as respectivas guias de pagamento corresponde a 5, */
    private boolean isGuiaPagamento;
    private boolean texto = false;

    public AtribuiContador() {

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();
        this.currentUser = (Usuario) session.getAttribute("usuario");

        if (currentUser.getCcm().getContador() != null) {

            this.ccm = currentUser.getCcm();
            this.contador = ccm.getContador();
            this.razaoContador = contador.getNomeRazao();
            this.cnpjCpf = contador.getCnpjCpf();
            permicoes = ccm.getPermicaoContador();
            String permicoesR[] = permicoes.split("\\,");
            texto = true;

            for (int i = 0; i < permicoesR.length; i++) {
                if (permicoesR[i].equals("1")) {
                    isEnviarEmailContador = true;
                } else if (permicoesR[i].equals("2")) {
                    isConverterRPS = true;
                } else if (permicoesR[i].equals("3")) {
                    isCancelarNF = true;
                } else if (permicoesR[i].equals("4")) {
                    isConsultarNF = true;
                } else if (permicoesR[i].equals("5")) {
                    isGuiaPagamento = true;
                }
            }
        }
    }

    public void buscaContador() {

        if (this.cnpjCpf.equals("")) {
            this.cnpjCpf = null;
            this.razaoContador = null;
            this.contador = null;
        } else {

            DAOCcm daoCcm = new DAOCcm();
            try {
                contador = daoCcm.buscaContadorPorCnpjCpf(this.cnpjCpf);
            } catch (Exception ex) {
                Logger.getLogger(AtribuiContador.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (contador != null) {
                razaoContador = contador.getNomeRazao();
            } else {
                this.razaoContador = null;
                contador = null;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Contador não encontrado Verifique CPF/CNPJ", ""));
            }
        }
    }

    public void vinculaContador() {

        if (this.contador != null) {

            permicoes = "";

            if (isEnviarEmailContador) {
                permicoes += "1,";
            }
            if (isConverterRPS) {
                permicoes += "2,";
            }
            if (isCancelarNF) {
                permicoes += "3,";
            }
            if (isConsultarNF) {
                permicoes += "4,";
            }
            if (isGuiaPagamento) {
                permicoes += "5,";
            }

            Ccm mobiliario = currentUser.getCcm();

            mobiliario.setContador(contador);
            mobiliario.setPermicaoContador(permicoes);

            DAOCcm daoCcm = new DAOCcm();
            try {
                daoCcm.salvarMerge(mobiliario);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Contador Vinculado ", ""));
            } catch (SQLException ex) {
                Logger.getLogger(AtribuiContador.class.getName()).log(Level.SEVERE, null, ex);
            }
            texto = true;
            System.out.println(permicoes);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Efetue a busca antes de se vincular", ""));
        }

    }
 
    public void desvincularContador() {

        if (contador != null) {
            contador = null;
            Ccm mobiliario = currentUser.getCcm();
            mobiliario.setContador(contador);
            mobiliario.setPermicaoContador("");

            DAOCcm daoCcm = new DAOCcm();
            try {
                daoCcm.salvarMerge(mobiliario);
                isCancelarNF = false;
                isConsultarNF = false;
                isConverterRPS = false;
                isEnviarEmailContador = false;
                isGuiaPagamento = false;
                cnpjCpf = "";
                razaoContador = "";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Contador Desvinculado ", ""));
            } catch (SQLException ex) {
                Logger.getLogger(AtribuiContador.class.getName()).log(Level.SEVERE, null, ex);
            }

            texto = false;
        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Você não esta vinculado a nenhum contador ", ""));
        }
    }

    public String getCnpjCpf() {
        return cnpjCpf;
    }

    public void setCnpjCpf(String cnpjCpf) {
        this.cnpjCpf = cnpjCpf;
    }

    public String getRazaoContador() {
        return razaoContador;
    }

    public void setRazaoContador(String razaoContador) {
        this.razaoContador = razaoContador;
    }

    public boolean isIsCancelarNF() {
        return isCancelarNF;
    }

    public void setIsCancelarNF(boolean isCancelarNF) {
        this.isCancelarNF = isCancelarNF;
    }

    public boolean isIsConsultarNF() {
        return isConsultarNF;
    }

    public void setIsConsultarNF(boolean isConsultarNF) {
        this.isConsultarNF = isConsultarNF;
    }

    public boolean isIsConverterRPS() {
        return isConverterRPS;
    }

    public void setIsConverterRPS(boolean isConverterRPS) {
        this.isConverterRPS = isConverterRPS;
    }

    public boolean isIsEnviarEmailContador() {
        return isEnviarEmailContador;
    }

    public void setIsEnviarEmailContador(boolean isEnviarEmailContador) {
        this.isEnviarEmailContador = isEnviarEmailContador;
    }

    public boolean isIsGuiaPagamento() {
        return isGuiaPagamento;
    }

    public void setIsGuiaPagamento(boolean isGuiaPagamento) {
        this.isGuiaPagamento = isGuiaPagamento;
    }

    public boolean isTexto() {
        return texto;
    }

    public void setTexto(boolean texto) {
        this.texto = texto;
    }
}
