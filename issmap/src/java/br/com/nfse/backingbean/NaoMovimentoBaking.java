/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.backingbean;


import br.com.nfse.jsfuntil.AutenticaUsuario;
import br.com.nfse.to.Usuario;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Thiago
 */
@ManagedBean(name = "MBNaoMovimento")
@ViewScoped
public class NaoMovimentoBaking {
    
    private String nomeRazao;
    private String cpfCnpj;
    private String cep;
    private String logradouro;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String complemento;
    private String telefone;
    private String email;
    private String inscricaoMunicipal;
    private String inscricaoEstadual;
    
    private String nomeRepresentanteLegal;
    private String cpfRepresentanteLegal;
    
    private String dia;
    private String mes;
    private String ano;
    
    private Usuario currentUser;
    private String template;

    
    public NaoMovimentoBaking(){
       
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();
        this.currentUser = (Usuario) session.getAttribute("usuario");
        this.template = AutenticaUsuario.verificaTemplate(this.currentUser);
        
        DateFormat diaf = new SimpleDateFormat("d");
        DateFormat mesf = new SimpleDateFormat("MMMM");
        DateFormat anof = new SimpleDateFormat("yyyy");
        
        Date datas = Calendar.getInstance(Locale.getDefault()).getTime();
        
        this.dia = diaf.format(datas);
        this.mes = mesf.format(datas);
        this.ano = anof.format(datas);        
        
        this.nomeRazao = this.currentUser.getCcm().getNomeRazao();
        this.cpfCnpj = this.currentUser.getCcm().getCnpjCpf();
        this.cep = this.currentUser.getCcm().getCnpjCpf();
        this.numero = this.currentUser.getCcm().getNumeroPredio().toString();
        this.cidade = this.currentUser.getCcm().getLogradouro().getBairro().getCidade().getNome();
        this.estado = this.currentUser.getCcm().getLogradouro().getBairro().getCidade().getEstado().getSigla();
        this.inscricaoMunicipal = this.currentUser.getCcm().getIm();
        this.inscricaoEstadual = this.currentUser.getCcm().getIeRg();
        String rua = currentUser.getCcm().getRua();

        if(rua==null){
            this.logradouro = currentUser.getCcm().getLogradouro().getNome();
            this.bairro = currentUser.getCcm().getLogradouro().getBairro().getNome();
        }else{
            this.logradouro = currentUser.getCcm().getRua();
            this.bairro = currentUser.getCcm().getBairro();
        }
    }
    
    public void emiteNaoMovimento(){
        
        if(nomeRepresentanteLegal.equals("") || cpfRepresentanteLegal.equals("")){
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "CNPJ/CPF Incorreto", ""));
            System.out.println("Campos nullos");
        }else{
            
            
            
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Declaração de Nao movimento emitida",""));
            
            
            System.out.println("Campos não nullos");
        }
        
        System.out.println("Chamaou metodoo!!");
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
    
    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNomeRazao() {
        return nomeRazao;
    }

    public void setNomeRazao(String nomeRazao) {
        this.nomeRazao = nomeRazao;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public String getInscricaoMunicipal() {
        return inscricaoMunicipal;
    }

    public void setInscricaoMunicipal(String inscricaoMunicipal) {
        this.inscricaoMunicipal = inscricaoMunicipal;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getCpfRepresentanteLegal() {
        return cpfRepresentanteLegal;
    }

    public void setCpfRepresentanteLegal(String cpfRepresentanteLegal) {
        this.cpfRepresentanteLegal = cpfRepresentanteLegal;
    }

    public String getNomeRepresentanteLegal() {
        return nomeRepresentanteLegal;
    }

    public void setNomeRepresentanteLegal(String nomeRepresentanteLegal) {
        this.nomeRepresentanteLegal = nomeRepresentanteLegal;
    }
}
