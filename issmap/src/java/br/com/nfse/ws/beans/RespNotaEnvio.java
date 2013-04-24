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
public class RespNotaEnvio {
    
    private Boolean resposta;

    public Boolean getResposta() {
        return resposta;
    }

    public void setResposta(Boolean resposta) {
        this.resposta = resposta;
    }
}
