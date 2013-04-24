/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.to;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;

/**
 *
 * @author ThiagoHenrique
 */
@Entity
public class NFSE implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nfse_lancamentos_sequence_generator")
    @SequenceGenerator(name = "nfse_lancamentos_sequence_generator", sequenceName = "nfse_lancamentos_sequence_id", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer idBanco;
    
    // dados nota fiscal
    @Column( length=30)
    private Long numeroNota;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataEmissao;    
    @Column( length=30)
    private String cogigoVerifica;
    @Column( length=600)
    private String descServico;
    @Column(name="flg_cancelada")
    private boolean cancelada;
    @Column(name="flg_substituida")
    private boolean substituida;
    private String servicoPrestado;
    @Column( length=15)
    private Double valorNota;
    @Column( length=15)
    private Double valorDeducoes;
    @Column( length=15)
    private Double valorBaseCalculo;
    @Column( length=10)
    private Double aliquota;
    @Column( length=15)
    private Double valorIss;
    @Column( length=15)
    private Double valorCredito; 
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataVencimento;
    @Column(name="valor_iss_prefeitura", length=15)
    private Double valorIssPrefeituara = 0.0;    
    private String localExecucao;
    @Column( length=5)
    private String retidoFonte;
    
    //Dados que mostram nota de fora retida na fonte /retida no issmap
    @Column(name="flg_terceiro")
    private boolean notaTerceiro;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name="data_lancamento_nota_terceiro")
    private Date dataLancamentoNotaTerceiro;
    @Column(name="hora_lancamento_nota_terceiro",length=5)
    private String horaLancamentoNotaTerceiro;
    @Column(name="endereco_validacao_nota_terceiros", length=60)
    private String enderecoValidacaoNotaTerceiros;
    
    //dados Prestador
    @Column( length=15)
    private String cpfCnpjPrestador;
    @Column( length=60)
    private String nomeRazaoPrestador;
    @Column( length=60)
    private String enderecoPrestador;
    @Column( length=100)
    private String cidadePrestador;
    @Column( length=30)
    private String imPrestador;
    @Column( length=30)
    private String ieRgPrestador;
    @Column( length=30)
    private String estadoPrestador;
    @Column( length=30)
    private String tipoTributacao;
    @Column( length=30)
    private String naturezaOperacao;    
    @Column( length=30)
    private String tipoMovimento;
    @Column( length=10)
    private String cepPrestador;
    @Column( length=60)
    private String emailPrestador;
    
    //dados tomador
    @Column( length=15)
    private String cpfCnpjTomador;
    @Column( length=60)
    private String nomeRazaoTomador;
    @Column( length=60)
    private String enderecoTomador;
    @Column( length=100)
    private String cidadeTomador;
    @Column( length=30)
    private String imTomador;
    @Column( length=30)
    private String ieRgTomador;
    @Column( length=30)
    private String estadoTomador;
    @Column( length=60)
    private String emailTomador;    
    @Column( length=10)
    private String hora;
    @Column( length=10)
    private String cepTomador;
    
    //Guia Pagamento
    @ManyToOne(optional=true)
    private BoletoIssMap boletoPagamento;        
    
    //Substituição de Nota
    @OneToOne(optional=true, fetch= FetchType.EAGER)
    private NFSE notaSubstituida;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataSubstituicao;

    public Date getDataSubstituicao() {
        return dataSubstituicao;
    }

    public void setDataSubstituicao(Date dataSubstituicao) {
        this.dataSubstituicao = dataSubstituicao;
    }

    public NFSE getNotaSubstituida() {
        return notaSubstituida;
    }

    public void setNotaSubstituida(NFSE notaSubstituida) {
        this.notaSubstituida = notaSubstituida;
    }

    public String getIeRgPrestador() {
        return ieRgPrestador;
    }

    public void setIeRgPrestador(String ieRgPrestador) {
        this.ieRgPrestador = ieRgPrestador;
    }

    public String getIeRgTomador() {
        return ieRgTomador;
    }

    public void setIeRgTomador(String ieRgTomador) {
        this.ieRgTomador = ieRgTomador;
    }
    
    public Double getAliquota() {
        return aliquota;
    }

    public void setAliquota(Double aliquota) {
        this.aliquota = aliquota;
    }

    public String getCidadePrestador() {
        return cidadePrestador;
    }

    public void setCidadePrestador(String cidadePrestador) {
        this.cidadePrestador = cidadePrestador;
    }

    public String getCidadeTomador() {
        return cidadeTomador;
    }

    public void setCidadeTomador(String cidadeTomador) {
        this.cidadeTomador = cidadeTomador;
    }

    public String getCogigoVerifica() {
        return cogigoVerifica;
    }

    public void setCogigoVerifica(String cogigoVerifica) {
        this.cogigoVerifica = cogigoVerifica;
    }

    public String getCpfCnpjPrestador() {
        return cpfCnpjPrestador;
    }

    public void setCpfCnpjPrestador(String cpfCnpjPrestador) {
        this.cpfCnpjPrestador = cpfCnpjPrestador;
    }

    public String getCpfCnpjTomador() {
        return cpfCnpjTomador;
    }

    public void setCpfCnpjTomador(String cpfCnpjTomador) {
        this.cpfCnpjTomador = cpfCnpjTomador;
    }

    public Date getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getDescServico() {
        return descServico;
    }

    public void setDescServico(String descServico) {
        this.descServico = descServico;
    }

    public String getEmailTomador() {
        return emailTomador;
    }

    public void setEmailTomador(String emailTomador) {
        this.emailTomador = emailTomador;
    }

    public String getEnderecoPrestador() {
        return enderecoPrestador;
    }

    public void setEnderecoPrestador(String enderecoPrestador) {
        this.enderecoPrestador = enderecoPrestador;
    }

    public String getEnderecoTomador() {
        return enderecoTomador;
    }

    public void setEnderecoTomador(String enderecoTomador) {
        this.enderecoTomador = enderecoTomador;
    }

    public String getEstadoPrestador() {
        return estadoPrestador;
    }

    public void setEstadoPrestador(String estadoPrestador) {
        this.estadoPrestador = estadoPrestador;
    }

    public String getEstadoTomador() {
        return estadoTomador;
    }

    public void setEstadoTomador(String estadoTomador) {
        this.estadoTomador = estadoTomador;
    }

    public Integer getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(Integer idBanco) {
        this.idBanco = idBanco;
    }

    public String getImPrestador() {
        return imPrestador;
    }

    public void setImPrestador(String imPrestador) {
        this.imPrestador = imPrestador;
    }

    public String getImTomador() {
        return imTomador;
    }

    public void setImTomador(String imTomador) {
        this.imTomador = imTomador;
    }
    
    public String getLocalExecucao() {
        return localExecucao;
    }

    public void setLocalExecucao(String localExecucao) {
        this.localExecucao = localExecucao;
    }

    public String getNaturezaOperacao() {
        return naturezaOperacao;
    }

    public void setNaturezaOperacao(String naturezaOperacao) {
        this.naturezaOperacao = naturezaOperacao;
    }

    public String getNomeRazaoPrestador() {
        return nomeRazaoPrestador;
    }

    public void setNomeRazaoPrestador(String nomeRazaoPrestador) {
        this.nomeRazaoPrestador = nomeRazaoPrestador;
    }

    public String getNomeRazaoTomador() {
        return nomeRazaoTomador;
    }

    public void setNomeRazaoTomador(String nomeRazaoTomador) {
        this.nomeRazaoTomador = nomeRazaoTomador;
    }

    public Long getNumeroNota() {
        return numeroNota;
    }

    public void setNumeroNota(Long numeroNota) {
        this.numeroNota = numeroNota;
    }

    public String getRetidoFonte() {
        return retidoFonte;
    }

    public void setRetidoFonte(String retidoFonte) {
        this.retidoFonte = retidoFonte;
    }

    public String getServicoPrestado() {
        return servicoPrestado;
    }

    public void setServicoPrestado(String servicoPrestado) {
        this.servicoPrestado = servicoPrestado;
    }

    public String getTipoTributacao() {
        return tipoTributacao;
    }

    public void setTipoTributacao(String tipoTributacao) {
        this.tipoTributacao = tipoTributacao;
    }

    public Double getValorBaseCalculo() {
        return valorBaseCalculo;
    }

    public void setValorBaseCalculo(Double valorBaseCalculo) {
        this.valorBaseCalculo = valorBaseCalculo;
    }

    public Double getValorCredito() {
        return valorCredito;
    }

    public void setValorCredito(Double valorCredito) {
        this.valorCredito = valorCredito;
    }

    public Double getValorDeducoes() {
        return valorDeducoes;
    }

    public void setValorDeducoes(Double valorDeducoes) {
        this.valorDeducoes = valorDeducoes;
    }

    public Double getValorIss() {
        return valorIss;
    }

    public void setValorIss(Double valorIss) {
        this.valorIss = valorIss;
    }

    public Double getValorNota() {
        return valorNota;
    }

    public void setValorNota(Double valorNota) {
        this.valorNota = valorNota;
    }

    public Double getValorIssPrefeituara() {
        return valorIssPrefeituara;
    }

    public void setValorIssPrefeituara(Double valorIssPrefeituara) {
        this.valorIssPrefeituara = valorIssPrefeituara;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public BoletoIssMap getBoletoPagamento() {
        return boletoPagamento;
    }

    public void setBoletoPagamento(BoletoIssMap boletoPagamento) {
        this.boletoPagamento = boletoPagamento;
    }

    public String getTipoMovimento() {
        return tipoMovimento;
    }

    public void setTipoMovimento(String tipoMovimento) {
        this.tipoMovimento = tipoMovimento;
    }

    public String getCepTomador() {
        return cepTomador;
    }

    public void setCepTomador(String cepTomador) {
        this.cepTomador = cepTomador;
    }

    public String getCepPrestador() {
        return cepPrestador;
    }

    public void setCepPrestador(String cepPrestador) {
        this.cepPrestador = cepPrestador;
    }

    public boolean isCancelada() {
        return cancelada;
    }

    public void setCancelada(boolean cancelada) {
        this.cancelada = cancelada;
    }

    public boolean isSubstituida() {
        return substituida;
    }

    public void setSubstituida(boolean substituida) {
        this.substituida = substituida;
    }

    public String getEmailPrestador() {
        return emailPrestador;
    }

    public void setEmailPrestador(String emailPrestador) {
        this.emailPrestador = emailPrestador;
    }

    public boolean isNotaTerceiro() {
        return notaTerceiro;
    }

    public void setNotaTerceiro(boolean notaTerceiro) {
        this.notaTerceiro = notaTerceiro;
    }

    public Date getDataLancamentoNotaTerceiro() {
        return dataLancamentoNotaTerceiro;
    }

    public void setDataLancamentoNotaTerceiro(Date dataLancamentoNotaTerceiro) {
        this.dataLancamentoNotaTerceiro = dataLancamentoNotaTerceiro;
    }

    public String getHoraLancamentoNotaTerceiro() {
        return horaLancamentoNotaTerceiro;
    }

    public void setHoraLancamentoNotaTerceiro(String horaLancamentoNotaTerceiro) {
        this.horaLancamentoNotaTerceiro = horaLancamentoNotaTerceiro;
    }

    public String getEnderecoValidacaoNotaTerceiros() {
        return enderecoValidacaoNotaTerceiros;
    }

    public void setEnderecoValidacaoNotaTerceiros(String enderecoValidacaoNotaTerceiros) {
        this.enderecoValidacaoNotaTerceiros = enderecoValidacaoNotaTerceiros;
    }
}
