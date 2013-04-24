/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.jsfuntil;


import br.com.nfse.dao.DAONFSE;
import br.com.nfse.to.NFSE;
import br.com.nfse.to.relatorio.NfseRelatorio;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;

/**
 *
 * @author Usuario
 */
public class NotaUtil {
    
    
    public void imprimeNota(NFSE notaFiscal) throws IOException, SQLException, ClassNotFoundException{
          
        

        NfseRelatorio notaFiscalRelatorio = new NfseRelatorio();
        
        DateFormat dataf = new SimpleDateFormat("dd/MM/yyyy");

        //itens relatório
        notaFiscalRelatorio.setCogigoVerifica(notaFiscal.getCogigoVerifica());
        notaFiscalRelatorio.setDataEmissao(dataf.format(notaFiscal.getDataEmissao())+" "+ notaFiscal.getHora());
        notaFiscalRelatorio.setNumeroNota(fomrmataNumeroNota(notaFiscal.getNumeroNota()));
        notaFiscalRelatorio.setCidadePrestador(notaFiscal.getCidadePrestador());
        notaFiscalRelatorio.setNomeRazaoPrestador(notaFiscal.getNomeRazaoPrestador());
        notaFiscalRelatorio.setEnderecoPrestador(notaFiscal.getEnderecoPrestador());
        notaFiscalRelatorio.setImRgPrestador(notaFiscal.getImPrestador());
        notaFiscalRelatorio.setEstadoPrestador(notaFiscal.getEstadoPrestador());
        notaFiscalRelatorio.setCpfCnpjPrestador(notaFiscal.getCpfCnpjPrestador());

        notaFiscalRelatorio.setCidadeTomador(notaFiscal.getCidadeTomador());
        notaFiscalRelatorio.setNomeRazaoTomador(notaFiscal.getNomeRazaoTomador());
        notaFiscalRelatorio.setEnderecoTomador(notaFiscal.getEnderecoTomador());
        notaFiscalRelatorio.setImRgTomador(notaFiscal.getImTomador());
        notaFiscalRelatorio.setEstadoTomador(notaFiscal.getEstadoTomador());
        notaFiscalRelatorio.setCpfCnpjTomador(notaFiscal.getCpfCnpjTomador());
        notaFiscalRelatorio.setEmailTomador(notaFiscal.getEmailTomador());

        notaFiscalRelatorio.setDescServico(notaFiscal.getDescServico());
        notaFiscalRelatorio.setServicoPrestado(notaFiscal.getServicoPrestado());
        notaFiscalRelatorio.setValorDeducoes(notaFiscal.getValorDeducoes().toString());
        notaFiscalRelatorio.setValorBaseCalculo(notaFiscal.getValorBaseCalculo().toString());
        notaFiscalRelatorio.setAliquota(notaFiscal.getAliquota().toString());
        notaFiscalRelatorio.setValorIss(notaFiscal.getValorIss().toString());
        notaFiscalRelatorio.setValorCredito(notaFiscal.getValorCredito().toString());
        notaFiscalRelatorio.setValorNota(notaFiscal.getValorNota().toString());
        notaFiscalRelatorio.setNaturezaOperacao(notaFiscal.getNaturezaOperacao());
        notaFiscalRelatorio.setTipoTributacao(notaFiscal.getTipoTributacao());
        notaFiscalRelatorio.setLocalExecucao(notaFiscal.getLocalExecucao());

        notaFiscalRelatorio.setRetidoFonte("SIM");
        notaFiscalRelatorio.setQrCode("http://chart.apis.google.com/chart?chs=150x150&cht=qr&chld=L|0&chl=http%3A%2F%2F201.43.6.22:9090/IssMap/?cod=" + notaFiscal.getCogigoVerifica());

        FacesContext facesContext1 = FacesContext.getCurrentInstance();
        ServletContext scontext1 = (ServletContext) facesContext1.getExternalContext().getContext();
        String caminhoImagem = scontext1.getRealPath("/img/logo_iss.gif");

        notaFiscalRelatorio.setCaminhoImagem(caminhoImagem);

        FacesContext facesContext2 = FacesContext.getCurrentInstance();
        ServletContext scontext2 = (ServletContext) facesContext2.getExternalContext().getContext();
        String caminhoLogoPrefeitura = scontext2.getRealPath("/img/logo_scruz.png");

        notaFiscalRelatorio.setCaminhoLogoPrefeitura(caminhoLogoPrefeitura);

        List<NfseRelatorio> listaNfse = new ArrayList<NfseRelatorio>();

        listaNfse.add(notaFiscalRelatorio);

        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listaNfse);

        HashMap parameters = new HashMap();

        try {

            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.responseComplete();
            ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
            JasperPrint jasperPrint = JasperFillManager.fillReport(scontext.getRealPath("/WEB-INF/report/nfse.jasper"), parameters, ds);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
            exporter.exportReport();
            byte[] bytes = baos.toByteArray();

            if (bytes != null && bytes.length > 0) {

                HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
                response.setContentType("application/pdf");
                response.setHeader("Content-disposition", "inline; filename=\"relatorioPorData.pdf\"");
                response.setContentLength(bytes.length);
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(bytes, 0, bytes.length);
                outputStream.flush();
                outputStream.close();
            }

        } catch (Exception e) {

            Logger.getLogger(NotaUtil.class.getName()).log(Level.SEVERE, null, e);
        }     
        
        
    }
    
    public static String fomrmataNumeroNota(Long numeroNota){
        
        NumberFormat format = new DecimalFormat("0000000");          
        return format.format(numeroNota);  

    }
    
    public static Long proximoNumeroNota(String cpfCnpjPrestador){
        
        DAONFSE dao = new DAONFSE();
        return (dao.numeroUltimaNota(cpfCnpjPrestador)+1);
    }
    
    
}
