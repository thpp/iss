/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.to;

/**
 *
 * @author Thiago
 */
public class VisaoGeral {
    
    private String quantidade;
    private String valorTotalNota;
    private String issDevido;

    
    public String getIssDevido() {
        return issDevido;
    }

    public void setIssDevido(String issDevido) {
        this.issDevido = issDevido;
    }
    
    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

    public String getValorTotalNota() {
        return valorTotalNota;
    }

    public void setValorTotalNota(String valorTotalNota) {
        this.valorTotalNota = valorTotalNota;
    }
}
