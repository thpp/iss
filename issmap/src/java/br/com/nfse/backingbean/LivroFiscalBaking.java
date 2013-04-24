/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.backingbean;

import br.com.nfse.dao.DAONFSE;
import br.com.nfse.jsfuntil.AutenticaUsuario;
import br.com.nfse.jsfuntil.Moeda;
import br.com.nfse.jsfuntil.NotaUtil;
import br.com.nfse.to.NFSE;
import br.com.nfse.to.Usuario;
import br.com.nfse.to.relatorio.NFSELivroFiscal;
import br.com.nfse.to.relatorio.Teste;
import br.com.nfse.to.relatorio.TesteObjeto;
import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;

/**
 *
 * @author Thiago
 */
@ManagedBean(name = "MBLivroFiscal")
@ViewScoped
public class LivroFiscalBaking {

    private String competencia;
    private Usuario currentUser;
    private String template;

    public LivroFiscalBaking() {

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();
        this.currentUser = (Usuario) session.getAttribute("usuario");
        this.template = AutenticaUsuario.verificaTemplate(this.currentUser);

    }

    public void imprimeLivroFiscal() {


        DAONFSE daoNfse = new DAONFSE();
        List<NFSELivroFiscal> listaLivroFiscal = new ArrayList<NFSELivroFiscal>();
        DateFormat dataf = new SimpleDateFormat("dd/MM/yyyy");
        Moeda moeda = new Moeda();
        try {
            List<NFSE> listaNota = daoNfse.buscaNotasPeloMes(competencia, this.currentUser.getCcm().getCnpjCpf(), "P");

            for (NFSE nfse : listaNota) {
                
                NFSELivroFiscal nfseLivro = new NFSELivroFiscal();
                
                nfseLivro.setAliquota(nfse.getAliquota().toString());
                nfseLivro.setBaseCalculo(moeda.formata(nfse.getValorNota()));
                String data = dataf.format(nfse.getDataEmissao());
                nfseLivro.setDiaEmissao(data.substring(0, 2));
                nfseLivro.setImpostoDevido(moeda.formata(nfse.getValorIss()));
                nfseLivro.setIsentaOutras("0,00");
                nfseLivro.setNumeroNota(NotaUtil.fomrmataNumeroNota(nfse.getNumeroNota()));
                nfseLivro.setObs(moeda.formata(nfse.getValorNota()));
                nfseLivro.setSerie("A");
                nfseLivro.setRemecaDevolucao("0,00");
                
                listaLivroFiscal.add(nfseLivro);
                
            }
            

            JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listaLivroFiscal);

            HashMap parameters = new HashMap();

            try {

                FacesContext facesContext = FacesContext.getCurrentInstance();
                facesContext.responseComplete();
                ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
                JasperPrint jasperPrint = JasperFillManager.fillReport(scontext.getRealPath("/WEB-INF/report/livroFiscal.jasper"), parameters, ds);
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

                Logger.getLogger(NfseLancaBaking.class.getName()).log(Level.SEVERE, null, e);
            }

        } catch (Exception ex) {
            Logger.getLogger(LivroFiscalBaking.class.getName()).log(Level.SEVERE, null, ex);
        }


    }    
    
        public void imprimeTeste() {

        Teste t = new Teste();
        
        t.setNome("Thiago Lindo");
        
         List<TesteObjeto> lista = new ArrayList<TesteObjeto>();
         
         TesteObjeto testeO = new TesteObjeto();
         TesteObjeto testeO1 = new TesteObjeto();
         
         testeO.setNome("Thiago");
         testeO.setIdade("23");
         lista.add(testeO);
         testeO1.setNome("Douglas");
         testeO1.setIdade("22");
         lista.add(testeO1);
                           
         t.setListaNumeros(lista);         
            
        List<Teste> testeLista = new ArrayList<Teste>();        
        
        testeLista.add(t);
        
        try {
            
            JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(testeLista);

            HashMap parameters = new HashMap();

            try {

                FacesContext facesContext = FacesContext.getCurrentInstance();
                facesContext.responseComplete();
                ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
                JasperPrint jasperPrint = JasperFillManager.fillReport(scontext.getRealPath("/WEB-INF/report/teste.jasper"), parameters, ds);
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

                Logger.getLogger(NfseLancaBaking.class.getName()).log(Level.SEVERE, null, e);
            }

        } catch (Exception ex) {
            Logger.getLogger(LivroFiscalBaking.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
    

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getCompetencia() {
        return competencia;
    }

    public void setCompetencia(String competencia) {
        this.competencia = competencia;
    }
}
