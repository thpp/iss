/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.backingbean;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 *
 * @author Thiago
 */
@ManagedBean(name = "MBSemNota")
public class SemNotaBaking {
    
    public SemNotaBaking(){
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nota nao encontrada! ", "Verifique se o CPF/CNPJ do Tomador ou o codigo de verificaçao estao corretos "));
    }
    
    
}
