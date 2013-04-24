/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.to;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.hibernate.annotations.Cascade;

/**
 *
 * @author ThiagoHenrique
 */
@Entity(name="genero_atividade")
public class GeneroAtividade implements Serializable {
    
    @Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    
    @Column(length= 500)
    private String nome;
    
    @OneToMany(cascade= CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "generoAtividade")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<Ccm> listaCcm;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Ccm> getListaCcm() {
        return listaCcm;
    }

    public void setListaCcm(List<Ccm> listaCcm) {
        this.listaCcm = listaCcm;
    }
    
}
