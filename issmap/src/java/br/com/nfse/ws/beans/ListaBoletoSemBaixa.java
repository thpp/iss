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
public class ListaBoletoSemBaixa {
    
    private List<BoletoSemBaixa> boletosSemBaixa;

    public List<BoletoSemBaixa> getBoletosSemBaixa() {
        return boletosSemBaixa;
    }

    public void setBoletosSemBaixa(List<BoletoSemBaixa> boletosSemBaixa) {
        this.boletosSemBaixa = boletosSemBaixa;
    }
}
