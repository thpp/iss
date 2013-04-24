/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.ws.beans;

import br.com.nfse.jsfuntil.Moeda;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Thiago
 */
@XmlRootElement
public class BoletosSF {
    
    private String numeroDocumento;
    private String competencia;
    private String vencimento;
    private String valor;
    private String pagamento;
    private String logo;
    private Double total;
    private Double totalDesconto;
    private Boolean botao;

    public String getCompetencia() {
        
        if(competencia.equals("0")){
            competencia = "Parcela Única";
        }else if(competencia.equals("1")){
            competencia = "Janeiro";
        }else if(competencia.equals("2")){
            competencia = "Fevereiro";
        }else if(competencia.equals("3")){
            competencia = "Março"; 
        }else if(competencia.equals("4")){
            competencia = "Abril";
        }else if(competencia.equals("5")){
            competencia = "Maio";
        }else if(competencia.equals("6")){
            competencia = "Junho";
        }else if(competencia.equals("7")){
            competencia = "Julho";
        }else if(competencia.equals("8")){
            competencia = "Agosto";
        }else if(competencia.equals("9")){
            competencia = "Setembro";
        }else if(competencia.equals("10")){
            competencia = "Outubro";
        }else if(competencia.equals("11")){
            competencia = "Novembro";
        }else if(competencia.equals("12")){
            competencia = "Dezembro";
        }        
        
        return competencia;
    }

    public void setCompetencia(String competencia) {
        this.competencia = competencia;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getPagamento() {
        return pagamento;
    }

    public void setPagamento(String pagamento) {
        this.pagamento = pagamento;
    }

    public String getValor() {
        
        Double resultado = total - totalDesconto;
        valor = new Moeda().formata(resultado);
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public String getLogo() {
        
        if(pagamento == null){
            logo = "desativado.png";
        }else{
            logo = "ativado.png";
        }
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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

    public Boolean getBotao() {
        if(pagamento == null){
            botao = false;
        }else{
            botao = true;
        }
        return botao;
    }

    public void setBotao(Boolean botao) {
        this.botao = botao;
    }
}
