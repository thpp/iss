package br.com.nfse.ws.beans;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author José Garcia Netto
 */
@XmlRootElement
public class Enderecos {

    private List<Endereco> endereco;

    public Enderecos(List<Endereco> endereco) {
        this.endereco = endereco;
    }

    public Enderecos() {
        this.endereco = new ArrayList<Endereco>();
    }

    public List<Endereco> getEndereco() {
        return endereco;
    }

    public void setEndereco(List<Endereco> endereco) {
        this.endereco = endereco;
    }
}
