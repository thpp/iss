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
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Thiago Henrique
 */
@Entity
@Table(name="socios")
public class Socio implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "socio_lancamentos_sequence_generator")
    @SequenceGenerator(name = "socio_lancamentos_sequence_generator", sequenceName = "socio_lancamentos_sequence_id", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(length=100)
    private String nome;
    @Column(length=11)
    private String cpf;
    @ManyToOne
    private Ccm ccm;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Ccm getCcm() {
        return ccm;
    }

    public void setCcm(Ccm ccm) {
        this.ccm = ccm;
    }
}
