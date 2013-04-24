
package br.com.nfse.to.relatorio;

import java.io.Serializable;


public class DesbloqueioPFRelatorio implements Serializable{
   
    private String razaoSocial;
    private String cnpj;
    private String nomeRepLegal;
    private String cpfRepLegal;
    private String coderificacao;
    private String data;
    private String caminhoLogo;
   
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCoderificacao() {
        return coderificacao;
    }

    public void setCoderificacao(String coderificacao) {
        this.coderificacao = coderificacao;
    }

    public String getCpfRepLegal() {
        return cpfRepLegal;
    }

    public void setCpfRepLegal(String cpfRepLegal) {
        this.cpfRepLegal = cpfRepLegal;
    }

    public String getNomeRepLegal() {
        return nomeRepLegal;
    }

    public void setNomeRepLegal(String nomeRepLegal) {
        this.nomeRepLegal = nomeRepLegal;
    }

    public String getCaminhoLogo() {
        return caminhoLogo;
    }

    public void setCaminhoLogo(String caminhoLogo) {
        this.caminhoLogo = caminhoLogo;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }
}
