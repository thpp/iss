/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.nfse.backingbean;

import br.com.nfse.dao.DAOUsuario;
import br.com.nfse.jsfuntil.AutenticaUsuario;
import br.com.nfse.jsfuntil.UsuarioSalt;
import br.com.nfse.to.Usuario;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
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
@ManagedBean(name = "MBAlteraSenha")
@ViewScoped
public class AlteraSenhaBaking implements java.io.Serializable {

    private boolean confirmaSenhaHidden = false;
    private String senha;
    private String confirmaSenha;
    private String dicaSenha;
    private String senhaAnterior;
    private String template;
    Usuario currentUser;

    public AlteraSenhaBaking() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();
         this.currentUser = (Usuario) session.getAttribute("usuario");
         this.template = AutenticaUsuario.verificaTemplate(this.currentUser);

    }


     public void carregaValorSenha() {
        System.out.println("Entrou no metodo carrega valor senha!!");

        System.out.println("Senha " + senha);
        System.out.println("confima senha " + confirmaSenha);

        if (confirmaSenha != null) {
            System.out.println("chama validação");
            validaSenhas();
        }
    }

    public void validaSenhas() {

        if (senha != null) {
            if (!senha.equals(confirmaSenha)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "Senhas Incorretas"));
                confirmaSenhaHidden = true;
            } else {
                confirmaSenhaHidden = false;
            }
        }
    }

    public void salvar(){

        System.out.println("Entrou no metodo");
        UsuarioSalt usuarioSalt = new UsuarioSalt();
        try {
            if(usuarioSalt.autentica(this.currentUser, this.senhaAnterior)){
                try {
                    this.currentUser.setSenha(this.senha);
                    usuarioSalt.criarLoginSenha(this.currentUser);
                    DAOUsuario daoUsuario = new DAOUsuario();
                    daoUsuario.salvar(this.currentUser);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.FACES_MESSAGES, "Senha Alterada com Sucesso"));
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(AlteraSenhaBaking.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "Senha Incorreta ao Usuario "+currentUser.getLogin()));
            }


        } catch (SQLException ex) {
            Logger.getLogger(AlteraSenhaBaking.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AlteraSenhaBaking.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getDicaSenha() {
        return dicaSenha;
    }

    public void setDicaSenha(String dicaSenha) {
        this.dicaSenha = dicaSenha;
    }

    public String getSenhaAnterior() {
        return senhaAnterior;
    }

    public void setSenhaAnterior(String senhaAnterior) {
        this.senhaAnterior = senhaAnterior;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
