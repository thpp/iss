/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.ws.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ThiagoHenrique
 */
@XmlRootElement(name="nfse")
@XmlAccessorType(XmlAccessType.FIELD)
public class NFSEWS {
    
    private Integer idBanco;
    
    // dados nota fiscal
    private String numeroNota;
    private String dataEmissao;    
    private String codigoVerifica;
    private String descServico;

    private String servicoPrestado;
    private Double valorNota;
    private Double valorDeducoes;
    private Double valorBaseCalculo;
    private Double aliquota;
    private Double valorIss;
    private Double valorCredito;    
    private String dataVencimento;
    private Double valorIssPrefeituara = 0.0;
    private String localExecucao;
    private String retidoFonte;
    private String hora;
    
    //dados Prestador
    private String cpfCnpjPrestador;
    private String nomeRazaoPrestador;
    private String enderecoPrestador;
    private String cidadePrestador;
    private String imPrestador;
    private String ieRgPrestador;
    private String estadoPrestador;
    private String tipoTributacao;
    private String naturezaOperacao;
    
    private String emailPrestador;
    
    //dados tomador
    private String cpfCnpjTomador;
    private String nomeRazaoTomador;
    private String enderecoTomador;
    private String cidadeTomador;
    private String imTomador;
    private String ieRgTomador;
    private String estadoTomador;
    private String emailTomador;
    
    //dados nota terceiro
    private String dataLancamentoTerceiro;
    private String horaLancamentoTerceiro;
    private String flgTerceiro;
    private String enderecoValidacaoTerceiro;

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

    public String getCodigoVerifica() {
        return codigoVerifica;
    }

    public void setCodigoVerifica(String codigoVerifica) {
        this.codigoVerifica = codigoVerifica;
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

    public String getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(String dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public String getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(String dataVencimento) {
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

    public String getNumeroNota() {
        return numeroNota;
    }

    public void setNumeroNota(String numeroNota) {
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

    public Double getValorIssPrefeituara() {
        return valorIssPrefeituara;
    }

    public void setValorIssPrefeituara(Double valorIssPrefeituara) {
        this.valorIssPrefeituara = valorIssPrefeituara;
    }

    public Double getValorNota() {
        return valorNota;
    }

    public void setValorNota(Double valorNota) {
        this.valorNota = valorNota;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getDataLancamentoTerceiro() {
        return dataLancamentoTerceiro;
    }

    public void setDataLancamentoTerceiro(String dataLancamentoTerceiro) {
        this.dataLancamentoTerceiro = dataLancamentoTerceiro;
    }

    public String getEmailPrestador() {
        return emailPrestador;
    }

    public void setEmailPrestador(String emailPrestador) {
        this.emailPrestador = emailPrestador;
    }

    public String getEnderecoValidacaoTerceiro() {
        return enderecoValidacaoTerceiro;
    }

    public void setEnderecoValidacaoTerceiro(String enderecoValidacaoTerceiro) {
        this.enderecoValidacaoTerceiro = enderecoValidacaoTerceiro;
    }

    public String getFlgTerceiro() {
        return flgTerceiro;
    }

    public void setFlgTerceiro(String flgTerceiro) {
        this.flgTerceiro = flgTerceiro;
    }

    public String getHoraLancamentoTerceiro() {
        return horaLancamentoTerceiro;
    }

    public void setHoraLancamentoTerceiro(String horaLancamentoTerceiro) {
        this.horaLancamentoTerceiro = horaLancamentoTerceiro;
    }
}
