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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author ThiagoHenrique
 */
@Entity
@Table(name="servico")
public class Servico implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "servico_lancamentos_sequence_generator")
    @SequenceGenerator(name = "servico_lancamentos_sequence_generator", sequenceName = "servico_lancamentos_sequence_id", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(length=5, unique=true)
    private String codigoItem;    
    private String itemTabela;
    private Double valorJ;
    private Double valorF;
    private Double aliquotaJ;
    private Double aliquotaF;
    private boolean vetado;
    private String descricao;
    @Column(name="flg_retencao_fonte")
    private boolean retencaoFonte;
    @Column(name="flg_plfacultativo")
    private boolean plFacultativo;
    
    // anotacao dada para um atributo que náo deve ser mapeada no banco...    
    @Transient
    @Column(name="flg_tp_movimento")
    private String tipoMovimento;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getAliquotaF() {
        return aliquotaF;
    }

    public void setAliquotaF(Double aliquotaF) {
        this.aliquotaF = aliquotaF;
    }

    public Double getAliquotaJ() {
        return aliquotaJ;
    }

    public void setAliquotaJ(Double aliquotaJ) {
        this.aliquotaJ = aliquotaJ;
    }

    public String getCodigoItem() {
        return codigoItem;
    }

    public void setCodigoItem(String codigoItem) {
        this.codigoItem = codigoItem;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getItemTabela() {
        return itemTabela;
    }

    public void setItemTabela(String itemTabela) {
        this.itemTabela = itemTabela;
    }

    public Double getValorF() {
        return valorF;
    }

    public void setValorF(Double valorF) {
        this.valorF = valorF;
    }

    public Double getValorJ() {
        return valorJ;
    }

    public void setValorJ(Double valorJ) {
        this.valorJ = valorJ;
    }

    public boolean isVetado() {
        return vetado;
    }

    public void setVetado(boolean vetado) {
        this.vetado = vetado;
    }

    public boolean isPlFacultativo() {
        return plFacultativo;
    }

    public void setPlFacultativo(boolean plFacultativo) {
        this.plFacultativo = plFacultativo;
    }

    public boolean isRetencaoFonte() {
        return retencaoFonte;
    }

    public void setRetencaoFonte(boolean retencaoFonte) {
        this.retencaoFonte = retencaoFonte;
    }

    public String getTipoMovimento() {
        return tipoMovimento;
    }

    public void setTipoMovimento(String tipoMovimento) {
        this.tipoMovimento = tipoMovimento;
    }
}
