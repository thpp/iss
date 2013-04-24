/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.ws.beans;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Thiago
 */
@XmlRootElement
public class ListaBoletoSF {
    
    private List<BoletosSF> listaBoleto;

    public List<BoletosSF> getListaBoleto() {
        return listaBoleto;
    }

    public void setListaBoleto(List<BoletosSF> listaBoleto) {
        this.listaBoleto = listaBoleto;
    }
}
