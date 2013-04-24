/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.backingbean;

import br.com.nfse.dao.DAOUsuario;
import br.com.nfse.jsfuntil.UsuarioSalt;
import br.com.nfse.to.Usuario;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Thiago Henrique
 */
@ManagedBean(name = "MBSenhaUsuario")
@ViewScoped
public class SenhaUsuarioBaking implements java.io.Serializable {
    
    private String senha;
    private String confirmaSenha = null;
    private String dicaSenha;
    private boolean confirmaSenhaHidden = false;
    private boolean mostraCadastroSenha = false;
    private String cpfBusca;
    private String empresaContratante;
    private String nomeUsuario;
    private Usuario usuario = null;
    
    public void validaCadastro(){
    
       if(this.usuario != null){
           
           this.usuario.setSenha(senha);
           this.usuario.setDicaSenha(dicaSenha);
           this.usuario.setCadastroConfirmado("ativado.png");
           
           if(usuario.getSalt() != null){
               FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario já validado", ""));
               return;
           }
           
           UsuarioSalt criptografia = new UsuarioSalt();
            try {
                criptografia.criarLoginSenha(this.usuario);
            } catch (NoSuchAlgorithmException ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao criptografar senha ", ""));
                Logger.getLogger(SenhaUsuarioBaking.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao criptografar senha ", ""));
                Logger.getLogger(SenhaUsuarioBaking.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            DAOUsuario daoUsuario = new DAOUsuario();
            try {
                daoUsuario.salvar(this.usuario);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cadastro validado com sucesso.",""));
            } catch (SQLException ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao validar cadastro. ", ""));
                Logger.getLogger(SenhaUsuarioBaking.class.getName()).log(Level.SEVERE, null, ex);
            }
           
       }
    
    
}            
    
    public void buscaUsuario(){
        
//        System.out.println("Emtrou no metodo busca CPF!!!!!!!!!!!!!!!!!!");
//        System.out.println("CPF procurado ====> "+cpfBusca);
        mostraCadastroSenha = true;
//        System.out.print("Mudando a visualização para true =====>> "+mostraCadastroSenha);
        DAOUsuario daoUsuario = new DAOUsuario();
        
        String cpf = cpfBusca.replaceAll("\\.", "").replaceAll("\\-", "") ;

        usuario = daoUsuario.buscaUsuarioPorCpf(cpf);
        empresaContratante = usuario.getCcm().getNomeRazao()+" - "+ usuario.getCcm().getCnpjCpf();
        nomeUsuario = usuario.getNome()+" - "+usuario.getLogin();        
        
    }
    
    
    
    public void carregaValorSenha() {

        System.out.println("Senha " + senha);
        System.out.println("confima senha " + confirmaSenha);

        if (confirmaSenha != null && !confirmaSenha.equals("")) {
            validaSenhas();
        }
    }

    public void validaSenhas() {
        
        System.out.println("confima senha " + confirmaSenha);
        
        if (senha != null && !senha.equals("")) {
            if (!senha.equals(confirmaSenha)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "Senhas Incorretas"));
                confirmaSenhaHidden = true;
            } else {
                confirmaSenhaHidden = false;
            }
        }
    }

    public String getCpfBusca() {
        return cpfBusca;
    }

    public void setCpfBusca(String cpfBusca) {
        this.cpfBusca = cpfBusca;
    }
    
    public boolean isMostraCadastroSenha() {
        return mostraCadastroSenha;
    }

    public void setMostraCadastroSenha(boolean mostraCadastroSenha) {
        this.mostraCadastroSenha = mostraCadastroSenha;
    }

    public String getConfirmaSenha() {
        return confirmaSenha;
    }

    public void setConfirmaSenha(String confirmaSenha) {
        this.confirmaSenha = confirmaSenha;
    }

    public boolean isConfirmaSenhaHidden() {
        return confirmaSenhaHidden;
    }

    public void setConfirmaSenhaHidden(boolean confirmaSenhaHidden) {
        this.confirmaSenhaHidden = confirmaSenhaHidden;
    }

    public String getDicaSenha() {
        return dicaSenha;
    }

    public void setDicaSenha(String dicaSenha) {
        this.dicaSenha = dicaSenha;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmpresaContratante() {
        return empresaContratante;
    }

    public void setEmpresaContratante(String empresaContratante) {
        this.empresaContratante = empresaContratante;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }
}
