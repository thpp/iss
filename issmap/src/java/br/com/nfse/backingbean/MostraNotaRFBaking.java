/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.backingbean;

import br.com.nfse.to.NFSE;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Thiago Henrique
 */
@ManagedBean(name = "MBMostraNotaRF")
@ViewScoped
public final class MostraNotaRFBaking {
    
    NFSE notaSelecionada = new NFSE();

    public NFSE getNotaSelecionada() {
        return notaSelecionada;
    }

    public void setNotaSelecionada(NFSE notaSelecionada) {
        this.notaSelecionada = notaSelecionada;
    }
}
