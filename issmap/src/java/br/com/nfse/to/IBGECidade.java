/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.to;

import br.com.nfse.dao.DAOEstadoIbge;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author ThiagoHenrique
 */
@Entity
@Table(name = "ibge_cidades")
public class IBGECidade implements Serializable {

    @Id
    @Column(name = "id")
    private Integer codigo;
    @Column(name = "iduf")
    private Integer codigoEstado;
    @Column(length = 70)
    private String nome;
    @Transient
    private IBGEEstado estado;

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigoEstado() {
        return codigoEstado;
    }

    public void setCodigoEstado(Integer codigoEstado) {
        this.codigoEstado = codigoEstado;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public IBGEEstado getEstado() {

        estado = new DAOEstadoIbge().buscarProCodigo(codigoEstado);

        return estado;
    }

    public void setEstado(IBGEEstado estado) {
        this.estado = estado;
    }
}
