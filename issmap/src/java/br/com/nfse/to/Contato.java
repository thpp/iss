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
@Table(name="contatos")
public class Contato implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contato_lancamentos_sequence_generator")
    @SequenceGenerator(name = "contato_lancamentos_sequence_generator", sequenceName = "contato_lancamentos_sequence_id", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(length=45)
    private String nome;
    @Column(length=30)
    private String cargo;
    @Column(length=45)
    private String email;
    @Column(name="fone_fax", length=10 )
    private String foneFax;

    @ManyToOne
    private Ccm ccm1;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFoneFax() {
        return foneFax;
    }

    public void setFoneFax(String foneFax) {
        this.foneFax = foneFax;
    }

    public Ccm getCcm1() {
        return ccm1;
    }

    public void setCcm1(Ccm ccm1) {
        this.ccm1 = ccm1;
    }
}
