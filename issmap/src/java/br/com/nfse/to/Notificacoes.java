/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.to;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

/**
 *
 * @author ThiagoHenrique
 */         
@Entity
@Table(name="notificacoes")
public class Notificacoes implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notificacoes_sequence_generator")
    @SequenceGenerator(name = "notificacoes_sequence_generator", sequenceName = "notificacoes_sequence_id", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataEnvio;
    @Column(length=60)
    private String remetente;
    @Column(length=350)
    private String notificacao;
    @ManyToOne
    private Ccm destinatario; 
    @Column(length=8)
    private String hora;
    
    // anotacao dada para um atributo que náo deve ser mapeada no banco...    
    @Transient
    private String dataString;
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Date getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(Date dataEnvio) {
        this.dataEnvio = dataEnvio;
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

    public Ccm getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Ccm destinatario) {
        this.destinatario = destinatario;
    }

    public String getDataString() {               
        return dataString;
    }

    public void setDataString(String dataString) {
        this.dataString = dataString;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
