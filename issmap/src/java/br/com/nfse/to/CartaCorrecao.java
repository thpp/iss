/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.to;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;


/**
 *
 * @author Thiago
 */
@Entity
public class CartaCorrecao implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "carta_sequence_generator")
    @SequenceGenerator(name = "carta_sequence_generator", sequenceName = "carta_sequence_id", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer idBanco;
    @Column( length=600)
    private String textoCartaCorracao;
    @OneToOne
    private NFSE nota;

    public Integer getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(Integer idBanco) {
        this.idBanco = idBanco;
    }
    
    public NFSE getNota() {
        return nota;
    }

    public void setNota(NFSE nota) {
        this.nota = nota;
    }
    
    public String getTextoCartaCorracao() {
        return textoCartaCorracao;
    }

    public void setTextoCartaCorracao(String textoCartaCorracao) {
        this.textoCartaCorracao = textoCartaCorracao;
    }
}
