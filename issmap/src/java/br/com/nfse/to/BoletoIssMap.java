/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.to;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import org.hibernate.annotations.Cascade;

/**
 *
 * @author Thiago
 */
@Entity
@Table
public class BoletoIssMap implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "boleto_lancamentos_sequence_generator")
    @SequenceGenerator(name = "boleto_lancamentos_sequence_generator", sequenceName = "boleto_lancamentos_sequence_id", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer codigo;
    @ManyToOne
    private Ccm sacado;
    @Column(length = 30)
    private String numeroDocumento;
    @Column(length = 30)
    private String nossoNumero;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataVencimenro;
    @Column(length = 25)
    private String campoLivre;
    private Double valorDocumento;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataDocumento;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataInicioCompetencia;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataFimCompetencia;
    private Double movimento;
    private Double aliquota;
    @Column
    private Boolean baixa;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "boletoPagamento")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<NFSE> notasBoleto;
    @Column(length = 30)
    private String logoSituacao;
    @Column(length=5)
    private String ficha;
    
    @Transient
    private DateFormat dataf = new SimpleDateFormat("dd/MM/yyyy");
    @Transient
    private String dataCompetenciaFormatada;

    public String getDataCompetenciaFormatada() {
        if(dataInicioCompetencia != null && dataFimCompetencia != null){
            dataCompetenciaFormatada = dataf.format(dataInicioCompetencia)+" a "+dataf.format(dataFimCompetencia);
        }        
        return dataCompetenciaFormatada;
    }  

    public String getLogoSituacao() {
        return logoSituacao;
    }

    public void setLogoSituacao(String logoSituacao) {
        this.logoSituacao = logoSituacao;
    }

    public List<NFSE> getNotasBoleto() {
        return notasBoleto;
    }

    public void setNotasBoleto(List<NFSE> notasBoleto) {
        this.notasBoleto = notasBoleto;
    }

    public Double getAliquota() {
        return aliquota;
    }

    public void setAliquota(Double aliquota) {
        this.aliquota = aliquota;
    }

    public String getCampoLivre() {
        return campoLivre;
    }

    public void setCampoLivre(String campoLivre) {
        this.campoLivre = campoLivre;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Date getDataFimCompetencia() {
        return dataFimCompetencia;
    }

    public void setDataFimCompetencia(Date dataFimCompetencia) {
        this.dataFimCompetencia = dataFimCompetencia;
    }

    public Date getDataInicioCompetencia() {
        return dataInicioCompetencia;
    }

    public void setDataInicioCompetencia(Date dataInicioCompetencia) {
        this.dataInicioCompetencia = dataInicioCompetencia;
    }

    public Date getDataDocumento() {
        return dataDocumento;
    }

    public void setDataDocumento(Date dataDocumento) {
        this.dataDocumento = dataDocumento;
    }

    public Date getDataVencimenro() {
        return dataVencimenro;
    }

    public void setDataVencimenro(Date dataVencimenro) {
        this.dataVencimenro = dataVencimenro;
    }

    public Double getMovimento() {
        return movimento;
    }

    public void setMovimento(Double movimento) {
        this.movimento = movimento;
    }

    public String getNossoNumero() {
        return nossoNumero;
    }

    public void setNossoNumero(String nossoNumero) {
        this.nossoNumero = nossoNumero;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public Ccm getSacado() {
        return sacado;
    }

    public void setSacado(Ccm sacado) {
        this.sacado = sacado;
    }

    public Double getValorDocumento() {
        return valorDocumento;
    }

    public void setValorDocumento(Double valorDocumento) {
        this.valorDocumento = valorDocumento;
    }

    public Boolean getBaixa() {
        return baixa;
    }

    public void setBaixa(Boolean baixa) {
        this.baixa = baixa;
    }

    public String getFicha() {
        return ficha;
    }

    public void setFicha(String ficha) {
        this.ficha = ficha;
    }
}
