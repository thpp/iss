package br.com.nfse.ws.beans;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Mobiliario implements Serializable {

    private String nome;
    private String fantasia;
    private String flagPessoa;
    private String dataNascimetoAbertura;
    private String email;
    private String cnpjCpf;
    private String ieRg;
    private String inscricaoMunicipal;
    private String dataAbertura;
    private String dataEncerramento;
    private String flagIssEletronico;
    private String dataLiberacao;
    private String dataDesbloqueio;
    private String dataBloqueioIssMap;    
    private String motivoBloqueioIssMap;
    private String dataSimples;
    private String dataMEI;
    private String documentoProfissionalLiberal;
    private String flagCobranca;
    private String flagAutonomo;
    private String flagSimples;
    private String flagMEI;
    private String flagProfissionalLiberal;
    private String numeroPessoa;
    private String numeroGenero;
    private String servicos;
    private Enderecos enderecos;
    private String codIss;
    private String codEventual;

    public String getCnpjCpf() {
        return cnpjCpf;
    }

    public void setCnpjCpf(String cnpjCpf) {
        this.cnpjCpf = cnpjCpf;
    }

    public String getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(String dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public String getDataBloqueioIssMap() {
        return dataBloqueioIssMap;
    }

    public void setDataBloqueioIssMap(String dataBloqueioIssMap) {
        this.dataBloqueioIssMap = dataBloqueioIssMap;
    }

    public String getDataDesbloqueio() {
        return dataDesbloqueio;
    }

    public void setDataDesbloqueio(String dataDesbloqueio) {
        this.dataDesbloqueio = dataDesbloqueio;
    }

    public String getDataEncerramento() {
        return dataEncerramento;
    }

    public void setDataEncerramento(String dataEncerramento) {
        this.dataEncerramento = dataEncerramento;
    }

    public String getDataLiberacao() {
        return dataLiberacao;
    }

    public void setDataLiberacao(String dataLiberacao) {
        this.dataLiberacao = dataLiberacao;
    }

    public String getDataMEI() {
        return dataMEI;
    }

    public void setDataMEI(String dataMEI) {
        this.dataMEI = dataMEI;
    }

    public String getDataNascimetoAbertura() {
        return dataNascimetoAbertura;
    }

    public void setDataNascimetoAbertura(String dataNascimetoAbertura) {
        this.dataNascimetoAbertura = dataNascimetoAbertura;
    }

    public String getDataSimples() {
        return dataSimples;
    }

    public void setDataSimples(String dataSimples) {
        this.dataSimples = dataSimples;
    }

    public String getDocumentoProfissionalLiberal() {
        return documentoProfissionalLiberal;
    }

    public void setDocumentoProfissionalLiberal(String documentoProfissionalLiberal) {
        this.documentoProfissionalLiberal = documentoProfissionalLiberal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFantasia() {
        return fantasia;
    }

    public void setFantasia(String fantasia) {
        this.fantasia = fantasia;
    }

    public String getFlagAutonomo() {
        return flagAutonomo;
    }

    public void setFlagAutonomo(String flagAutonomo) {
        this.flagAutonomo = flagAutonomo;
    }

    public String getFlagCobranca() {
        return flagCobranca;
    }

    public void setFlagCobranca(String flagCobranca) {
        this.flagCobranca = flagCobranca;
    }

    public String getFlagIssEletronico() {
        return flagIssEletronico;
    }

    public void setFlagIssEletronico(String flagIssEletronico) {
        this.flagIssEletronico = flagIssEletronico;
    }

    public String getFlagMEI() {
        return flagMEI;
    }

    public void setFlagMEI(String flagMEI) {
        this.flagMEI = flagMEI;
    }

    public String getFlagPessoa() {
        return flagPessoa;
    }

    public void setFlagPessoa(String flagPessoa) {
        this.flagPessoa = flagPessoa;
    }

    public String getFlagProfissionalLiberal() {
        return flagProfissionalLiberal;
    }

    public void setFlagProfissionalLiberal(String flagProfissionalLiberal) {
        this.flagProfissionalLiberal = flagProfissionalLiberal;
    }

    public String getFlagSimples() {
        return flagSimples;
    }

    public void setFlagSimples(String flagSimples) {
        this.flagSimples = flagSimples;
    }

    public String getIeRg() {
        return ieRg;
    }

    public void setIeRg(String ieRg) {
        this.ieRg = ieRg;
    }

    public String getInscricaoMunicipal() {
        return inscricaoMunicipal;
    }

    public void setInscricaoMunicipal(String inscricaoMunicipal) {
        this.inscricaoMunicipal = inscricaoMunicipal;
    }

    public String getMotivoBloqueioIssMap() {
        return motivoBloqueioIssMap;
    }

    public void setMotivoBloqueioIssMap(String motivoBloqueioIssMap) {
        this.motivoBloqueioIssMap = motivoBloqueioIssMap;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNumeroGenero() {
        return numeroGenero;
    }

    public void setNumeroGenero(String numeroGenero) {
        this.numeroGenero = numeroGenero;
    }

    public String getNumeroPessoa() {
        return numeroPessoa;
    }

    public void setNumeroPessoa(String numeroPessoa) {
        this.numeroPessoa = numeroPessoa;
    }

    public String getServicos() {
        return servicos;
    }

    public void setServicos(String servicos) {
        this.servicos = servicos;
    }

    public Enderecos getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(Enderecos enderecos) {
        this.enderecos = enderecos;
    }

    public String getCodIss() {
        return codIss;
    }

    public void setCodIss(String codIss) {
        this.codIss = codIss;
    }

    public String getCodEventual() {
        return codEventual;
    }

    public void setCodEventual(String codEventual) {
        this.codEventual = codEventual;
    }
}
