/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.ws.beans;

/**
 *
 * @author ThiagoHenrique
 */
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Notificacao {
    
    private String dataEnvio;
    private String remetente;
    private String notificacao;       
    private String hora;

    public String getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(String dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getNotificacao() {
        return notificacao;
    }

    public void setNotificacao(String notificacao) {
        this.notificacao = notificacao;
    }

    public String getRemetente() {
        return remetente;
    }

    public void setRemetente(String remetente) {
        this.remetente = remetente;
    }
}
