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
import javax.persistence.Temporal;


/**
 *
 * @author ThiagoHenrique
 */
@Entity(name="historico_bloqueio")
public class HistoricoBloqueio implements Serializable {
    
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "bloqueio_lancamentos_sequence_generator")
    @SequenceGenerator(name = "bloqueio_lancamentos_sequence_generator", sequenceName = "bloqueio_lancamentos_sequence_id", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer codigo;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date databloqueio;
    private String motivo;
    @ManyToOne
    private Ccm ccm;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataLiberacao;


    public Date getDataLiberacao() {
        return dataLiberacao;
    }

    public void setDataLiberacao(Date dataLiberacao) {
        this.dataLiberacao = dataLiberacao;
    }
    
    public Ccm getCcm() {
        return ccm;
    }

    public void setCcm(Ccm ccm) {
        this.ccm = ccm;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Date getDatabloqueio() {
        return databloqueio;
    }

    public void setDatabloqueio(Date databloqueio) {
        this.databloqueio = databloqueio;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
