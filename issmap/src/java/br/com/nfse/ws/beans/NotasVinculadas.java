/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.ws.beans;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Thiago
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class NotasVinculadas {
    
    private List<String> codigos = new ArrayList<String>();

    public List<String> getCodigos() {
        return codigos;
    }

    public void setCodigos(List<String> codigos) {
        this.codigos = codigos;
    }
    
    public void setAtributo(String atributo){
        codigos.add(atributo);
    }
}
