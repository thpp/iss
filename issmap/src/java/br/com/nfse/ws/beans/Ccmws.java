
package br.com.nfse.ws.beans;


import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;



@XmlRootElement(name="ccm")
@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType(propOrder={"codigoValido","cpfCnpj","email","nomeRazao","representanteLegal","telefone","cep","rua","numero","bairro","complemneto","cidade","estado","contador","crc","socios"})
public class Ccmws implements Serializable  {
    
    private boolean  codigoValido;
    private String cpfCnpj;       
    private String email; 
    private String nomeRazao;    
    private String representanteLegal;    
    private String telefone;
    private List<Sociows> socios;       
    
    //endereço
    private String cep;
    private String rua;
    private String numero;
    private String bairro;
    private String complemento;
    private String cidade;
    private String estado;
    
    //Para contadores
    private Boolean contador;
    private String crc;

    public Ccmws() {
    }

    public boolean isCodigoValido() {
        return codigoValido;
    }

    public void setCodigoValido(boolean codigoValido) {
        this.codigoValido = codigoValido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNomeRazao() {
        return nomeRazao;
    }

    public void setNomeRazao(String nomeRazao) {
        this.nomeRazao = nomeRazao;
    }

    public String getRepresentanteLegal() {
        return representanteLegal;
    }

    public void setRepresentanteLegal(String representanteLegal) {
        this.representanteLegal = representanteLegal;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    
    public List<Sociows> getSocio() {
        return socios;
    }

    public void setSocio(List<Sociows> socios) {
        this.socios = socios;
    }
    
    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public Boolean getContador() {
        return contador;
    }

    public void setContador(Boolean contador) {
        this.contador = contador;
    }

    public String getCrc() {
        return crc;
    }

    public void setCrc(String crc) {
        this.crc = crc;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }
}
