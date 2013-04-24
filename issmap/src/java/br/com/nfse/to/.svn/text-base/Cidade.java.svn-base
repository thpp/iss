/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.nfse.to;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 *
 * @author Thiago Henrique
 */
@Entity
@Table(name="cidades")
public class Cidade implements Serializable {

   @Id
   @GeneratedValue(strategy=GenerationType.AUTO)
   @Column(name="cd_cidade")
   private Integer codigo;

   @ManyToOne()
   @Fetch(FetchMode.JOIN)
   @JoinColumn(name="cd_uf",nullable=false)
   private Estado estado;

   @Column(name="ds_cidade_nome",length=100, nullable=false)
   private String nome;

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
