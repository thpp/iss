/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.ws.beans;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Thiago
 */
@XmlRootElement
public class SegundaViaFixo {
    
    private String numeroDocumento;
    private String dataVencimento;
    private String codigoBarras;
    private Double valorIss;
    private Double valorTaxaLicenca;
    private Double valorTaxaExpediente;
    private Double valorIncendio;
    private String nossoNumero;
    private Double total;
    private Double totalDesconto;
    private String parcela;

    
    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(String dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public Double getValorIncendio() {
        return valorIncendio;
    }

    public void setValorIncendio(Double valorIncendio) {
        this.valorIncendio = valorIncendio;
    }

    public Double getValorIss() {
        return valorIss;
    }

    public void setValorIss(Double valorIss) {
        this.valorIss = valorIss;
    }

    public Double getValorTaxaExpediente() {
        return valorTaxaExpediente;
    }

    public void setValorTaxaExpediente(Double valorTaxaExpediente) {
        this.valorTaxaExpediente = valorTaxaExpediente;
    }

    public Double getValorTaxaLicenca() {
        return valorTaxaLicenca;
    }

    public void setValorTaxaLicenca(Double valorTaxaLicenca) {
        this.valorTaxaLicenca = valorTaxaLicenca;
    }

    public String getNossoNumero() {
        return nossoNumero;
    }

    public void setNossoNumero(String nossoNumero) {
        this.nossoNumero = nossoNumero;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getTotalDesconto() {
        return totalDesconto;
    }

    public void setTotalDesconto(Double totalDesconto) {
        this.totalDesconto = totalDesconto;
    }

    public String getParcela() {
        return parcela;
    }

    public void setParcela(String parcela) {
        this.parcela = parcela;
    }
}
