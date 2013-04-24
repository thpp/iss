/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.backingbean;

import br.com.nfse.jsfuntil.ConfiguracaoSyst;
import br.com.nfse.jsfuntil.JSFUtil;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.jrimum.bopepo.BancosSuportados;
import org.jrimum.bopepo.Boleto;
import org.jrimum.bopepo.view.BoletoViewer;
import org.jrimum.domkee.comum.pessoa.endereco.CEP;
import org.jrimum.domkee.comum.pessoa.endereco.Endereco;
import org.jrimum.domkee.comum.pessoa.endereco.UnidadeFederativa;
import org.jrimum.domkee.financeiro.banco.febraban.Agencia;
import org.jrimum.domkee.financeiro.banco.febraban.Carteira;
import org.jrimum.domkee.financeiro.banco.febraban.Cedente;
import org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria;
import org.jrimum.domkee.financeiro.banco.febraban.NumeroDaConta;
import org.jrimum.domkee.financeiro.banco.febraban.Sacado;
import org.jrimum.domkee.financeiro.banco.febraban.SacadorAvalista;
import org.jrimum.domkee.financeiro.banco.febraban.TipoDeTitulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo.EnumAceite;

/**
 *
 * @author Thiago
 */
@ManagedBean(name = "MBBoleto")
@ViewScoped
public class TesteBoleto {

    public void imprimeBoleto() {

        /*
         * INFORMANDO DADOS SOBRE O CEDENTE.
         */
        Cedente cedente = new Cedente(ConfiguracaoSyst.NOME_CIDADE, "00.000.208/0001-00");

        /*
         * INFORMANDO DADOS SOBRE O SACADO.
         */
        Sacado sacado = new Sacado("ITAU Unibanco S/A","60.701.190/0617-40");

        // Informando o endereço do sacado.
        Endereco enderecoSac = new Endereco();
        enderecoSac.setUF(UnidadeFederativa.SP);
        enderecoSac.setLocalidade("Santa Cruz do Rio Pardo");
        enderecoSac.setCep(new CEP("18800-000"));
        enderecoSac.setBairro("Centro");
        enderecoSac.setLogradouro("PCA. Ataiaba Leonel");
        enderecoSac.setNumero("003");
        sacado.addEndereco(enderecoSac);

        /*
         * INFORMANDO DADOS SOBRE O SACADOR AVALISTA.
         */
//        SacadorAvalista sacadorAvalista = new SacadorAvalista("JRimum Enterprise", "00.000.000/0001-91");
//
//        // Informando o endereço do sacador avalista.
//        Endereco enderecoSacAval = new Endereco();
//        enderecoSacAval.setUF(UnidadeFederativa.SP);
//        enderecoSacAval.setLocalidade("Brasília");
//        enderecoSacAval.setCep(new CEP("59000-000"));
//        enderecoSacAval.setBairro("Grande Centro");
//        enderecoSacAval.setLogradouro("Rua Eternamente Principal");
//        enderecoSacAval.setNumero("001");
//        sacadorAvalista.addEndereco(enderecoSacAval);

        /*
         * INFORMANDO OS DADOS SOBRE O TÍTULO.
         */

        // Informando dados sobre a conta bancária do título.
        ContaBancaria contaBancaria = new ContaBancaria(BancosSuportados.BANCO_SANTANDER.create());
        contaBancaria.setNumeroDaConta(new NumeroDaConta(123456, "0"));
        contaBancaria.setCarteira(new Carteira(102));
        contaBancaria.setAgencia(new Agencia(1234, "1"));

        Titulo titulo = new Titulo(contaBancaria, sacado, cedente);
        titulo.setNumeroDoDocumento("123456");
        titulo.setNossoNumero("99345678912");
        titulo.setDigitoDoNossoNumero("5");
        titulo.setValor(BigDecimal.valueOf(0.23));
        titulo.setDataDoDocumento(new Date());
        titulo.setDataDoVencimento(new Date());
        titulo.setTipoDeDocumento(TipoDeTitulo.RC_RECIBO);
        titulo.setAceite(EnumAceite.N);
        titulo.setDesconto(new BigDecimal(0.05));
        titulo.setDeducao(BigDecimal.ZERO);
        titulo.setMora(BigDecimal.ZERO);
        titulo.setAcrecimo(BigDecimal.ZERO);
        titulo.setValorCobrado(BigDecimal.ZERO);

        
        
        /*
         * INFORMANDO OS DADOS SOBRE O BOLETO.
         */
        Boleto boleto = new Boleto(titulo);

        

        boleto.setLocalPagamento("Pagável preferencialmente na Rede X ou em "
                + "qualquer Banco até o Vencimento.");
        boleto.setInstrucaoAoSacado("Senhor sacado, sabemos sim que o valor "
                + "cobrado não é o esperado, aproveite o DESCONTÃO!");
        boleto.setInstrucao1("Após o vencimanto aplicar sobre a parcela:");
        boleto.setInstrucao2("Multa de 2% (dois por cento) ao mês, até o limite de 10% (dez por cento),");
        boleto.setInstrucao3("acrescimentos de juros de mora de 1% (um por cento) ao mês, a partir do mês");
        boleto.setInstrucao4("imediato ao vencimento, sendo considerado mês completo qualquer fração deste");
        boleto.setInstrucao8("APÓS o Vencimento, Pagável Somente na Rede X.");
        
        boleto.addTextosExtras("txtFcCarteira", "COB");
        boleto.addTextosExtras("txtFcEspecie", "R$");
        boleto.addTextosExtras("txtFcEspecieDocumento", "RC-CI");
        
        boleto.addTextosExtras("txtRsCompetencia", "01/06/2012 a 20/06/2012");
        boleto.addTextosExtras("txtRsAliquota", "3%");
        boleto.addTextosExtras("txtRsMovimentoMensal", "R$ 15000,00");
        boleto.addTextosExtras("txtRsIM", "00017");
        
        boleto.addTextosExtras("txtFcCompetencia", "01/06/2012 a 20/06/2012");
        boleto.addTextosExtras("txtFcAliquota", "3%");
        boleto.addTextosExtras("txtFcMovimentoMensal", "R$ 15000,00");

        /*
         * GERANDO O BOLETO BANCÁRIO.
         */
        // Instanciando um objeto "BoletoViewer", classe responsável pela
        // geração do boleto bancário.

        //Informando o template personalizado:


        FacesContext facesContext = FacesContext.getCurrentInstance();
        ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
        String arquivo = scontext.getRealPath("/PDF/BoletoPersonalizado.pdf");


        BoletoViewer boletoViewer = new BoletoViewer(boleto);
        boletoViewer.setTemplate(arquivo);

        //BoletoViewer boletoViewer = new BoletoViewer(boleto);

        // Gerando o arquivo. No caso o arquivo mencionado será salvo na mesma
        // pasta do projeto. Outros exemplos:
        // WINDOWS: boletoViewer.getAsPDF("C:/Temp/MeuBoleto.pdf");
        // LINUX: boletoViewer.getAsPDF("/home/temp/MeuBoleto.pdf");
        //File arquivoPdf = boletoViewer.getPdfAsFile("MeuPrimeiroBoleto.pdf");
        File arquivoPdf = boletoViewer.getPdfAsFile("BoletoPersonalizado.pdf");

        try {
            
            facesContext.responseComplete();

            byte[] pdfAsBytes = boletoViewer.getPdfAsByteArray();

            HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=boleto.pdf");

            response.setContentLength(pdfAsBytes.length);
            OutputStream output = response.getOutputStream();
            output.write(pdfAsBytes, 0, pdfAsBytes.length);
            output.flush();
            output.close();

            FacesContext.getCurrentInstance().responseComplete();

        } catch (IOException ex) {
            Logger.getLogger(TesteBoleto.class.getName()).log(Level.SEVERE, null, ex);
        }




    }
}
