/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.ws.beans;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Thiago
 */
@XmlRootElement
public class BoletoSemBaixa {
    
    private String cgarqNro;

    public String getCgarqNro() {
        return cgarqNro;
    }

    public void setCgarqNro(String cgarqNro) {
        this.cgarqNro = cgarqNro;
    }
}
