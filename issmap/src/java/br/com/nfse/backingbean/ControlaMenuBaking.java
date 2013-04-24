/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.nfse.backingbean;

import br.com.nfse.to.Usuario;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Thiago Henrique
 */
@ManagedBean(name="MBMenu")
@SessionScoped
public class ControlaMenuBaking {

     private Integer index = 0;
     private Boolean menuContador;  
     private Usuario currentUser;
     
    public ControlaMenuBaking() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();
        this.currentUser = (Usuario) session.getAttribute("usuario");
        menuContador = currentUser.getCcm().getContadorProfissional();
        index = new Integer(0);
        index = 0;
    }

    public void altera0(){
        index = new Integer(0);
        index = 0;
    }

    public void altera1(){
        index = new Integer(0);
        index = 1;
    }

    public void altera2(){
        index = new Integer(0);
        index = 2;
    }

    public void altera3(){
        index = new Integer(0);
        index = 3;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Boolean getMenuContador() {
        return menuContador;
    }
}
