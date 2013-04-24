package br.com.nfse.to.relatorio;

import java.util.Date;

/**
 *
 * @author ThiagoHenrique
 */
public class NfseRelatorio {
    
    private String numeroNota;
    private String dataEmissao;
    private String cogigoVerifica;
    private String cpfCnpjPrestador;
    private String nomeRazaoPrestador;
    private String enderecoPrestador;
    private String cidadePrestador;
    private String imRgPrestador;
    private String estadoPrestador;
    private String cpfCnpjTomador;
    private String nomeRazaoTomador;
    private String enderecoTomador;
    private String cidadeTomador;
    private String imRgTomador;
    private String estadoTomador;
    private String descServico;
    private String servicoPrestado;
    private String valorDeducoes;
    private String valorBaseCalculo;
    private String aliquota;
    private String valorIss;
    private String valorCredito;
    private String dataVencimento;
    private String valorNota;
    private String emailTomador;
    private String caminhoImagem;
    private String qrCode;
    private String caminhoLogoPrefeitura;
    private String naturezaOperacao;
    private String tipoTributacao;
    private String localExecucao;
    private String retidoFonte;
    private String txtSubstitui;

    public String getEmailTomador() {
        return emailTomador;
    }

    public void setEmailTomador(String emailTomador) {
        this.emailTomador = emailTomador;
    }

    public String getCpfCnpjPrestador() {
        return cpfCnpjPrestador;
    }

    public void setCpfCnpjPrestador(String cpfCnpjPrestador) {
        this.cpfCnpjPrestador = cpfCnpjPrestador;
    }

    public String getEnderecoPrestador() {
        return enderecoPrestador;
    }

    public void setEnderecoPrestador(String enderecoPrestador) {
        this.enderecoPrestador = enderecoPrestador;
    }

    public String getImRgPrestador() {
        return imRgPrestador;
    }

    public void setImRgPrestador(String imRgPrestador) {
        this.imRgPrestador = imRgPrestador;
    }

    public String getNomeRazaoPrestador() {
        return nomeRazaoPrestador;
    }

    public void setNomeRazaoPrestador(String nomeRazaoPrestador) {
        this.nomeRazaoPrestador = nomeRazaoPrestador;
    }
    
    public String getCogigoVerifica() {
        return cogigoVerifica;
    }

    public void setCogigoVerifica(String cogigoVerifica) {
        this.cogigoVerifica = cogigoVerifica;
    }

    public String getNumeroNota() {
        return numeroNota;
    }

    public void setNumeroNota(String numeroNota) {
        this.numeroNota = numeroNota;
    }

    public String getCidadePrestador() {
        return cidadePrestador;
    }

    public void setCidadePrestador(String cidadePrestador) {
        this.cidadePrestador = cidadePrestador;
    }

    public String getEstadoPrestador() {
        return estadoPrestador;
    }

    public void setEstadoPrestador(String estadoPrestador) {
        this.estadoPrestador = estadoPrestador;
    }

    public String getCidadeTomador() {
        return cidadeTomador;
    }

    public void setCidadeTomador(String cidadeTomador) {
        this.cidadeTomador = cidadeTomador;
    }

    public String getCpfCnpjTomador() {
        return cpfCnpjTomador;
    }

    public void setCpfCnpjTomador(String cpfCnpjTomador) {
        this.cpfCnpjTomador = cpfCnpjTomador;
    }

    public String getEnderecoTomador() {
        return enderecoTomador;
    }

    public void setEnderecoTomador(String enderecoTomador) {
        this.enderecoTomador = enderecoTomador;
    }

    public String getEstadoTomador() {
        return estadoTomador;
    }

    public void setEstadoTomador(String estadoTomador) {
        this.estadoTomador = estadoTomador;
    }

    public String getImRgTomador() {
        return imRgTomador;
    }

    public void setImRgTomador(String imRgTomador) {
        this.imRgTomador = imRgTomador;
    }

    public String getNomeRazaoTomador() {
        return nomeRazaoTomador;
    }

    public void setNomeRazaoTomador(String nomeRazaoTomador) {
        this.nomeRazaoTomador = nomeRazaoTomador;
    }

    public String getAliquota() {
        return aliquota;
    }

    public void setAliquota(String aliquota) {
        this.aliquota = aliquota;
    }

    public String getDescServico() {
        return descServico;
    }

    public void setDescServico(String descServico) {
        this.descServico = descServico;
    }

    public String getServicoPrestado() {
        return servicoPrestado;
    }

    public void setServicoPrestado(String servicoPrestado) {
        this.servicoPrestado = servicoPrestado;
    }

    public String getValorBaseCalculo() {
        return valorBaseCalculo;
    }

    public void setValorBaseCalculo(String valorBaseCalculo) {
        this.valorBaseCalculo = valorBaseCalculo;
    }

    public String getValorCredito() {
        return valorCredito;
    }

    public void setValorCredito(String valorCredito) {
        this.valorCredito = valorCredito;
    }

    public String getValorDeducoes() {
        return valorDeducoes;
    }

    public void setValorDeducoes(String valorDeducoes) {
        this.valorDeducoes = valorDeducoes;
    }

    public String getValorIss() {
        return valorIss;
    }

    public void setValorIss(String valorIss) {
        this.valorIss = valorIss;
    }

    public String getValorNota() {
        return valorNota;
    }

    public void setValorNota(String valorNota) {
        this.valorNota = valorNota;
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

    public String getCaminhoImagem() {
        return caminhoImagem;
    }

    public void setCaminhoImagem(String caminhoImagem) {
        this.caminhoImagem = caminhoImagem;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getCaminhoLogoPrefeitura() {
        return caminhoLogoPrefeitura;
    }

    public void setCaminhoLogoPrefeitura(String caminhoLogoPrefeitura) {
        this.caminhoLogoPrefeitura = caminhoLogoPrefeitura;
    }

    public String getNaturezaOperacao() {
        return naturezaOperacao;
    }

    public void setNaturezaOperacao(String naturezaOperacao) {
        this.naturezaOperacao = naturezaOperacao;
    }

    public String getTipoTributacao() {
        return tipoTributacao;
    }

    public void setTipoTributacao(String tipoTributacao) {
        this.tipoTributacao = tipoTributacao;
    }

    public String getLocalExecucao() {
        return localExecucao;
    }

    public void setLocalExecucao(String localExecucao) {
        this.localExecucao = localExecucao;
    }

    public String getRetidoFonte() {
        return retidoFonte;
    }

    public void setRetidoFonte(String retidoFonte) {
        this.retidoFonte = retidoFonte;
    }

    public String getTxtSubstitui() {
        return txtSubstitui;
    }

    public void setTxtSubstitui(String txtSubstitui) {
        this.txtSubstitui = txtSubstitui;
    }
}
