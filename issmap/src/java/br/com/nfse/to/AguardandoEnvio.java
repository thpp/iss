/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.to;

import javax.persistence.OneToOne;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
/**
 *
 * @author ThiagoHenrique
 */
@Entity
@Table(name="Aguardando_Envio")
public class AguardandoEnvio implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aguardandoenvio_sequence_generator")
    @SequenceGenerator(name = "aguardandoenvio_sequence_generator", sequenceName = "aguardandoenvio_sequence_id", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer idBanco;
    
    @OneToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    private NFSE notaEspera;
    
    
    
    public Integer getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(Integer idBanco) {
        this.idBanco = idBanco;
    }

    public NFSE getNotaEspera() {
        return notaEspera;
    }

    public void setNotaEspera(NFSE notaEspera) {
        this.notaEspera = notaEspera;
    }
}
