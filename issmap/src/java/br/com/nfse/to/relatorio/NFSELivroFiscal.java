/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.to.relatorio;

/**
 *
 * @author Thiago
 */
public class NFSELivroFiscal {
    
    private String diaEmissao;
    private String serie;
    private String numeroNota;
    private String baseCalculo;
    private String aliquota;
    private String impostoDevido;
    private String isentaOutras;
    private String remecaDevolucao;
    private String obs;

    public String getAliquota() {
        return aliquota;
    }

    public void setAliquota(String aliquota) {
        this.aliquota = aliquota;
    }

    public String getBaseCalculo() {
        return baseCalculo;
    }

    public void setBaseCalculo(String baseCalculo) {
        this.baseCalculo = baseCalculo;
    }

    public String getDiaEmissao() {
        return diaEmissao;
    }

    public void setDiaEmissao(String diaEmissao) {
        this.diaEmissao = diaEmissao;
    }

    public String getImpostoDevido() {
        return impostoDevido;
    }

    public void setImpostoDevido(String impostoDevido) {
        this.impostoDevido = impostoDevido;
    }

    public String getIsentaOutras() {
        return isentaOutras;
    }

    public void setIsentaOutras(String isentaOutras) {
        this.isentaOutras = isentaOutras;
    }

    public String getNumeroNota() {
        return numeroNota;
    }

    public void setNumeroNota(String numeroNota) {
        this.numeroNota = numeroNota;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getRemecaDevolucao() {
        return remecaDevolucao;
    }

    public void setRemecaDevolucao(String remecaDevolucao) {
        this.remecaDevolucao = remecaDevolucao;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }
}
