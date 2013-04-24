/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.backingbean;

import br.com.nfse.dao.DAOCartaCorrecao;
import br.com.nfse.dao.DAONFSE;
import br.com.nfse.jsfuntil.NotaUtil;
import br.com.nfse.to.CartaCorrecao;
import br.com.nfse.to.NFSE;
import br.com.nfse.to.Usuario;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Thiago
 */
@ManagedBean(name = "MBCarta")
@ViewScoped
public class CartaCorrecaoBaking {
    
    private String cpfCnpjTomador;
    private String codigoVerificacao;
    private String nomeRazaoTomador;
    private String servicoPrestado;
    private Double aliquota;
    private String localExecucao;
    private String naturezaOperacao;
    private String tipoTributacao;
    private String tipoMovimento;
    private String retidoFonte;
    private String descServico;
    private Double valorServico;
    private Double deducoes;
    private Double valorIss;
    private Double valorCredito;
    private Double baseCalculo;
    private String cartaCorracao;
    private Boolean exibeCarta = false;
    private Boolean exibeAviso = true;
    private Boolean btAnexa = false;
    private String endTomador;
    private String numeroNota;
    private String dataHora;
    private Usuario currentUser;
    
    private NFSE nota = null;
    private CartaCorrecao carta = new CartaCorrecao();
    DateFormat dataCompleta = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat horaf = new SimpleDateFormat("HH:mm:ss");
    
    public CartaCorrecaoBaking(){        
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();
        this.currentUser = (Usuario) session.getAttribute("usuario");

    }

    public void buscaNotaFical(){
        
        DAONFSE daoNota = new DAONFSE();
        nota = new NFSE();
        
        try {
            
            nota = daoNota.buscaNotaTomador(cpfCnpjTomador, codigoVerificacao,currentUser.getCcm().getCnpjCpf());
            if(nota != null){
                
                nomeRazaoTomador = nota.getNomeRazaoTomador();
                servicoPrestado = nota.getServicoPrestado();
                aliquota = nota.getAliquota();
                localExecucao = nota.getLocalExecucao();
                naturezaOperacao = nota.getNaturezaOperacao();
                tipoTributacao = nota.getTipoTributacao();
                retidoFonte = nota.getRetidoFonte();
                descServico = nota.getDescServico();
                valorServico = nota.getValorNota();
                deducoes = nota.getValorDeducoes();
                valorIss = nota.getValorIss();
                valorCredito = getValorCredito();
                baseCalculo = valorServico - deducoes;
                tipoMovimento = nota.getTipoMovimento();
                endTomador = nota.getEnderecoTomador()+", "+ nota.getCidadeTomador()+" - "+ nota.getEstadoTomador();
                numeroNota = NotaUtil.fomrmataNumeroNota(nota.getNumeroNota());
                dataHora = dataCompleta.format(nota.getDataEmissao()) +" "+ nota.getHora();                
                exibeCarta = true;
                exibeAviso = false;
                
                carta = new DAOCartaCorrecao().buscaCartaPorNota(nota);                
                if(carta != null){
                    cartaCorracao = carta.getTextoCartaCorracao();
                    btAnexa = true;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO!", "Já Possui Cata Anexada"));
                }
                
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO!", "Nenhum Registro Encontrado"));
            }
            
            
        } catch (Exception ex) {
            Logger.getLogger(CartaCorrecaoBaking.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void anexaCarta(){
        
        DAOCartaCorrecao daoCarta = new DAOCartaCorrecao();
        CartaCorrecao cartaCorrecao = new CartaCorrecao();        
        if(!cartaCorracao.equals("")){
            cartaCorrecao.setTextoCartaCorracao(cartaCorracao);
            cartaCorrecao.setNota(nota);
            try {
                daoCarta.salvar(cartaCorrecao);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Salvo!", "Cata Anexada"));
                btAnexa = true;
            } catch (SQLException ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "Cata em Branco"));
                Logger.getLogger(CartaCorrecaoBaking.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String getCodigoVerificacao() {
        return codigoVerificacao;
    }

    public void setCodigoVerificacao(String codigoVerificacao) {
        this.codigoVerificacao = codigoVerificacao;
    }

    public String getCpfCnpjTomador() {
        return cpfCnpjTomador;
    }

    public void setCpfCnpjTomador(String cpfCnpjTomador) {
        this.cpfCnpjTomador = cpfCnpjTomador;
    }

    public Double getAliquota() {
        return aliquota;
    }

    public void setAliquota(Double aliquota) {
        this.aliquota = aliquota;
    }

    public Double getDeducoes() {
        return deducoes;
    }

    public void setDeducoes(Double deducoes) {
        this.deducoes = deducoes;
    }

    public String getDescServico() {
        return descServico;
    }

    public void setDescServico(String descServico) {
        this.descServico = descServico;
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

    public String getNomeRazaoTomador() {
        return nomeRazaoTomador;
    }

    public void setNomeRazaoTomador(String nomeRazaoTomador) {
        this.nomeRazaoTomador = nomeRazaoTomador;
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

    public String getTipoMovimento() {
        return tipoMovimento;
    }

    public void setTipoMovimento(String tipoMovimento) {
        this.tipoMovimento = tipoMovimento;
    }

    public String getTipoTributacao() {
        return tipoTributacao;
    }

    public void setTipoTributacao(String tipoTributacao) {
        this.tipoTributacao = tipoTributacao;
    }

    public Double getValorCredito() {
        return valorCredito;
    }

    public void setValorCredito(Double valorCredito) {
        this.valorCredito = valorCredito;
    }

    public Double getValorIss() {
        return valorIss;
    }

    public void setValorIss(Double valorIss) {
        this.valorIss = valorIss;
    }

    public Double getValorServico() {
        return valorServico;
    }

    public void setValorServico(Double valorServico) {
        this.valorServico = valorServico;
    }

    public Double getBaseCalculo() {
        return baseCalculo;
    }

    public void setBaseCalculo(Double baseCalculo) {
        this.baseCalculo = baseCalculo;
    }

    public Boolean getExibeAviso() {
        return exibeAviso;
    }

    public void setExibeAviso(Boolean exibeAviso) {
        this.exibeAviso = exibeAviso;
    }

    public Boolean getExibeCarta() {
        return exibeCarta;
    }

    public void setExibeCarta(Boolean exibeCarta) {
        this.exibeCarta = exibeCarta;
    }

    public String getCartaCorracao() {
        return cartaCorracao;
    }

    public void setCartaCorracao(String cartaCorracao) {
        this.cartaCorracao = cartaCorracao;
    }

    public Boolean getBtAnexa() {
        return btAnexa;
    }

    public void setBtAnexa(Boolean btAnexa) {
        this.btAnexa = btAnexa;
    }

    public String getEndTomador() {
        return endTomador;
    }

    public void setEndTomador(String endTomador) {
        this.endTomador = endTomador;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    public String getNumeroNota() {
        return numeroNota;
    }

    public void setNumeroNota(String numeroNota) {
        this.numeroNota = numeroNota;
    }
}
