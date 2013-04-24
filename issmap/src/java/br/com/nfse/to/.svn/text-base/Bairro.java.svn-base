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
@Table(name="bairros")
public class Bairro implements Serializable {
  
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  @Column(name="cd_bairro")
  private Integer codigo;

  @ManyToOne()
  @Fetch(FetchMode.JOIN)
  @JoinColumn(name="cd_cidade",nullable=false)
  private Cidade cidade;

  @Column(name="ds_bairro_nome",length=100,nullable=false)
  private String nome;

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }    
}
