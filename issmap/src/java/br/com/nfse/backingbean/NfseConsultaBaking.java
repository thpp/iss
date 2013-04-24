package br.com.nfse.backingbean;

import br.com.nfse.dao.DAONFSE;
import br.com.nfse.jsfuntil.AutenticaUsuario;
import br.com.nfse.jsfuntil.Moeda;
import br.com.nfse.jsfuntil.NotaUtil;
import br.com.nfse.to.NFSE;
import br.com.nfse.to.relatorio.NfseRelatorio;
import br.com.nfse.to.Usuario;
import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
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

@ManagedBean(name = "MBNfseConsulta")
@ViewScoped
public class NfseConsultaBaking {

    // atributos prestador
    private String cpfCnpjPrestador = "";
    private String nomeRazaoPrestador = "";
    private Date dataIniPrestador;
    private Date dataFimPrestador;
    // atributos tomador
    private String nomeRazaoTomador = "";
    private String cpfCnpjTomador = "";
    private Date dataIniTomador;
    private Date dataFimTomador;
    private List<NFSE> notas;
    private Usuario currentUser;
    private String template;
    private Date dataIniRF;
    private Date dataFimRF;
    private Date dataHoje = new Date();
    private NFSE notaSelecionada = new NFSE();
    private List<NFSE> listaNotasRetidoFonte;
    private Boolean exibeNotasTributacaoNormal = true;

    public NfseConsultaBaking() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();
        this.currentUser = (Usuario) session.getAttribute("usuario");
        
        if(currentUser.getCcm().getCodigoEventualGemmap() != null){
            exibeNotasTributacaoNormal = false;
        }

        this.template = AutenticaUsuario.verificaTemplate(this.currentUser);
    }

    public void filtraNotasPrestador() throws Exception {
        DAONFSE daoNfse = new DAONFSE();
        this.notas = new ArrayList<NFSE>();
        String tipo = "P";

        if ((this.dataIniPrestador != null) && (this.dataFimPrestador != null)) {

            DateFormat datafI = new SimpleDateFormat("dd/MM/yyyy");

            String dataStrIni = datafI.format(dataIniPrestador);
            dataIniPrestador = new Date(datafI.parse(dataStrIni).getTime());

            DateFormat datafF = new SimpleDateFormat("dd/MM/yyyy");
            String dataStrFim = datafF.format(dataFimPrestador);
            dataFimPrestador = new Date(datafF.parse(dataStrFim).getTime());

            if (dataIniPrestador.after(dataFimPrestador)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "Datas Incorretas"));
                return;
            }

        }
        this.notas = daoNfse.buscaNotasPorFiltro(currentUser.getCcm().getCnpjCpf(), cpfCnpjPrestador, nomeRazaoPrestador, dataIniPrestador, dataFimPrestador, tipo);
    }

    public void imprimeNota() {

        DateFormat dataf = new SimpleDateFormat("dd/MM/yyyy");

        NfseRelatorio notaFiscalRelatorio = new NfseRelatorio();
        Moeda moeda = new Moeda();

        //itens relatório
        notaFiscalRelatorio.setCogigoVerifica(notaSelecionada.getCogigoVerifica());
        notaFiscalRelatorio.setDataEmissao(dataf.format(notaSelecionada.getDataEmissao()) + " " + notaSelecionada.getHora());
        notaFiscalRelatorio.setNumeroNota(NotaUtil.fomrmataNumeroNota(notaSelecionada.getNumeroNota()));
        notaFiscalRelatorio.setCidadePrestador(notaSelecionada.getCidadePrestador());
        notaFiscalRelatorio.setNomeRazaoPrestador(notaSelecionada.getNomeRazaoPrestador());
        notaFiscalRelatorio.setEnderecoPrestador(notaSelecionada.getEnderecoPrestador());
        notaFiscalRelatorio.setImRgPrestador(notaSelecionada.getImPrestador());
        notaFiscalRelatorio.setEstadoPrestador(notaSelecionada.getEstadoPrestador());
        notaFiscalRelatorio.setCpfCnpjPrestador(notaSelecionada.getCpfCnpjPrestador());

        notaFiscalRelatorio.setCidadeTomador(notaSelecionada.getCidadeTomador());
        notaFiscalRelatorio.setNomeRazaoTomador(notaSelecionada.getNomeRazaoTomador());
        notaFiscalRelatorio.setEnderecoTomador(notaSelecionada.getEnderecoTomador());
        notaFiscalRelatorio.setImRgTomador(notaSelecionada.getImTomador());
        notaFiscalRelatorio.setEstadoTomador(notaSelecionada.getEstadoTomador());
        notaFiscalRelatorio.setCpfCnpjTomador(notaSelecionada.getCpfCnpjTomador());
        notaFiscalRelatorio.setEmailTomador(notaSelecionada.getEmailTomador());

        notaFiscalRelatorio.setDescServico(notaSelecionada.getDescServico());
        notaFiscalRelatorio.setServicoPrestado(notaSelecionada.getServicoPrestado());
        notaFiscalRelatorio.setValorDeducoes(moeda.formata(notaSelecionada.getValorDeducoes()));
        notaFiscalRelatorio.setValorBaseCalculo(moeda.formata(notaSelecionada.getValorBaseCalculo()));
        notaFiscalRelatorio.setAliquota(notaSelecionada.getAliquota().toString());
        notaFiscalRelatorio.setValorIss(moeda.formata(notaSelecionada.getValorIss()));
        notaFiscalRelatorio.setValorCredito(moeda.formata(notaSelecionada.getValorCredito()));
        notaFiscalRelatorio.setValorNota(moeda.formata(notaSelecionada.getValorNota()));
        notaFiscalRelatorio.setNaturezaOperacao(notaSelecionada.getNaturezaOperacao());
        notaFiscalRelatorio.setTipoTributacao(notaSelecionada.getTipoTributacao());
        notaFiscalRelatorio.setLocalExecucao(notaSelecionada.getLocalExecucao());

        // Verifica se é uma nota substitutiva
        if (notaSelecionada.getNotaSubstituida() != null) {
            notaFiscalRelatorio.setTxtSubstitui("Nota Fiscal substitutiva a nota nº " + notaSelecionada.getNumeroNota() + " e codigo de verificação " + notaSelecionada.getCogigoVerifica());
        } else {
            notaFiscalRelatorio.setTxtSubstitui("");
        }

        // Verifica se é uma nota substituida
        String nomeNota = "nfse.jasper";
        if (notaSelecionada.isSubstituida()) {
            nomeNota = "nfsecancelada.jasper";
            notaFiscalRelatorio.setTxtSubstitui("Nota Fiscal Eletrônica cancelada e substituida pela nota nº " + notaSelecionada.getNumeroNota() + " e codigo de verificação " + notaSelecionada.getCogigoVerifica());
        } else if (notaSelecionada.isCancelada()) {
            nomeNota = "nfsecancelada.jasper";
            notaFiscalRelatorio.setTxtSubstitui("Nota Fiscal Eletrônica cancelada e sem efeitos tributários.");
        }

        notaFiscalRelatorio.setRetidoFonte(notaSelecionada.getRetidoFonte());
        notaFiscalRelatorio.setQrCode("http://chart.apis.google.com/chart?chs=150x150&cht=qr&chld=L|0&chl=http%3A%2F%2Fissmap.micromap.com.br?cod=" + notaSelecionada.getCogigoVerifica());

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
            JasperPrint jasperPrint = JasperFillManager.fillReport(scontext.getRealPath("/WEB-INF/report/" + nomeNota), parameters, ds);
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


    }

    public void filtraNotasTomador() throws Exception {
        DAONFSE daoNfse = new DAONFSE();
        List<NFSE> lista = new ArrayList<NFSE>();
        String tipo = "T";

        if ((this.dataIniTomador != null) && (this.dataFimTomador != null)) {
            DateFormat dataf = new SimpleDateFormat("dd/MM/yyyy");
            String dataStrIni = dataf.format(dataIniTomador);
            dataIniTomador = new Date(dataf.parse(dataStrIni).getTime());

            String dataStrFim = dataf.format(dataFimTomador);
            dataFimTomador = new Date(dataf.parse(dataStrFim).getTime());

            if (dataIniTomador.after(dataFimTomador)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "Datas Incorretas"));
                return;
            }
        }

        lista = daoNfse.buscaNotasPorFiltro(currentUser.getCcm().getCnpjCpf(), cpfCnpjTomador, nomeRazaoTomador, dataIniTomador, dataFimTomador, tipo);

        this.notas = lista;
    }

    public void filtraNotasRetidoFonte() throws Exception {

        DAONFSE daoNfse = new DAONFSE();
        this.listaNotasRetidoFonte = new ArrayList<NFSE>();

        if ((this.dataIniRF != null) && (this.dataFimRF != null)) {

            DateFormat datafI = new SimpleDateFormat("dd/MM/yyyy");

            String dataStrIni = datafI.format(dataIniRF);
            dataIniRF = new Date(datafI.parse(dataStrIni).getTime());

            DateFormat datafF = new SimpleDateFormat("dd/MM/yyyy");
            String dataStrFim = datafF.format(dataFimRF);
            dataFimRF = new Date(datafF.parse(dataStrFim).getTime());

            if (dataIniRF.after(dataFimRF)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "Datas Incorretas"));
                return;
            }
            this.listaNotasRetidoFonte = daoNfse.buscaNotasRetidoFonte(currentUser.getCcm().getCnpjCpf(), dataIniRF, dataFimRF);


            if (listaNotasRetidoFonte.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Nenhuma nota foi encontrada", ""));
            }


        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "Informe o periodo de busca"));
        }
    }

    public NFSE getNotaSelecionada() {
        return notaSelecionada;
    }

    public void setNotaSelecionada(NFSE notaSelecionada) {
        this.notaSelecionada = notaSelecionada;
    }

    public String getCpfCnpjPrestador() {
        return cpfCnpjPrestador;
    }

    public void setCpfCnpjPrestador(String cpfCnpjPrestador) {
        this.cpfCnpjPrestador = cpfCnpjPrestador;
    }

    public String getCpfCnpjTomador() {
        return cpfCnpjTomador;
    }

    public void setCpfCnpjTomador(String cpfCnpjTomador) {
        this.cpfCnpjTomador = cpfCnpjTomador;
    }

    public Date getDataFimPrestador() {
        return dataFimPrestador;
    }

    public void setDataFimPrestador(Date dataFimPrestador) {
        this.dataFimPrestador = dataFimPrestador;
    }

    public Date getDataFimTomador() {
        return dataFimTomador;
    }

    public void setDataFimTomador(Date dataFimTomador) {
        this.dataFimTomador = dataFimTomador;
    }

    public Date getDataIniPrestador() {
        return dataIniPrestador;
    }

    public void setDataIniPrestador(Date dataIniPrestador) {
        this.dataIniPrestador = dataIniPrestador;
    }

    public Date getDataIniTomador() {
        return dataIniTomador;
    }

    public void setDataIniTomador(Date dataIniTomador) {
        this.dataIniTomador = dataIniTomador;
    }

    public String getNomeRazaoPrestador() {
        return nomeRazaoPrestador;
    }

    public void setNomeRazaoPrestador(String nomeRazaoPrestador) {
        this.nomeRazaoPrestador = nomeRazaoPrestador;
    }

    public String getNomeRazaoTomador() {
        return nomeRazaoTomador;
    }

    public void setNomeRazaoTomador(String nomeRazaoTomador) {
        this.nomeRazaoTomador = nomeRazaoTomador;
    }

    public List<NFSE> getNotas() {
        return notas;
    }

    public void setNotas(List<NFSE> notas) {
        this.notas = notas;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Date getDataFimRF() {
        return dataFimRF;
    }

    public void setDataFimRF(Date dataFimRF) {
        this.dataFimRF = dataFimRF;
    }

    public Date getDataIniRF() {
        return dataIniRF;
    }

    public void setDataIniRF(Date dataIniRF) {
        this.dataIniRF = dataIniRF;
    }

    public Date getDataHoje() {
        return dataHoje;
    }

    public void setDataHoje(Date dataHoje) {
        this.dataHoje = dataHoje;
    }
    
    public List<NFSE> getListaNotasRetidoFonte() {
        return listaNotasRetidoFonte;
    }

    public void setListaNotasRetidoFonte(List<NFSE> listaNotasRetidoFonte) {
        this.listaNotasRetidoFonte = listaNotasRetidoFonte;
    }

    public Boolean getExibeNotasTributacaoNormal() {
        return exibeNotasTributacaoNormal;
    }

    public void setExibeNotasTributacaoNormal(Boolean exibeNotasTributacaoNormal) {
        this.exibeNotasTributacaoNormal = exibeNotasTributacaoNormal;
    }
}
