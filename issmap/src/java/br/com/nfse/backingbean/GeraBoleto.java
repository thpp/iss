/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.backingbean;

import br.com.nfse.dao.DAOBoleto;
import br.com.nfse.dao.DAONFSE;
import br.com.nfse.jsfuntil.AutenticaUsuario;
import br.com.nfse.jsfuntil.ConfiguracaoSyst;
import br.com.nfse.jsfuntil.Moeda;
import br.com.nfse.jsfuntil.NotaUtil;
import br.com.nfse.to.BoletoIssMap;
import br.com.nfse.to.HistoricoNota;
import br.com.nfse.to.HistoricoNotaRrtidoFonte;
import br.com.nfse.to.NFSE;
import br.com.nfse.to.TesteCampoLivre;
import br.com.nfse.to.Usuario;
import br.com.nfse.ws.beans.BoletoSemBaixa;
import br.com.nfse.ws.beans.BoletosSF;
import br.com.nfse.ws.beans.DadosBoletoWS;
import br.com.nfse.ws.beans.ListaBoletoSemBaixa;
import br.com.nfse.ws.beans.ListaBoletoSF;
import br.com.nfse.ws.beans.SegundaViaFixo;
import br.com.nfse.ws.cliente.BuscaBoletoSimlesNacional;
import br.com.nfse.ws.cliente.BuscaBoletosBaixados;
import br.com.nfse.ws.cliente.BuscaBoletosFixo;
import br.com.nfse.ws.cliente.BuscaDadosBoletoGemmap;
import br.com.nfse.ws.cliente.BuscaSegundaViaFixo;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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
import org.jrimum.domkee.financeiro.banco.febraban.TipoDeTitulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo.EnumAceite;

/**
 *
 * @author Thiago
 */
@ManagedBean(name = "MBGeraBoleto")
@ViewScoped
public class GeraBoleto {

    private String quantidadeNota;
    private String totalNota;
    private String aliquota;
    private String totalBoleto;
    private Date dataIni;
    private Date dataFim;
    private List<NFSE> notas;
    private String template;
    Usuario currentUser;
    private Boolean mostraDados = false;
    private List<BoletoIssMap> listaBoletos = new ArrayList<BoletoIssMap>();
    private List<BoletoIssMap> listaBoletosRetidoFonte;
    private Date dataIniBolotoGerado;
    private Date dataFimBolotoGerado;
    private BoletoIssMap boletoSelecionado;
    private String escolhePDF;
    Moeda moeda = new Moeda();
    private String numeroDocumento;
    private Boolean geraBoleto;
    private Boolean botaoSegundaVia;
    private Boolean tabelaSNF;
    private Boolean buscaSimples;
    private List<BoletosSF> boletosSNF;
    private Boolean tabelaBoletos;
    private String mesSelecionado;
    private List<BoletosSF> boletosSNFlAux;
    private BoletosSF fixoSelecionado;
    private String textoColunaCommtParcela = "Competêcia";
    private String ficha = "";
    DateFormat dataF = new SimpleDateFormat("dd/MM/yyyy");
    private List<NFSE> listaNotasRetidoFonte = new ArrayList<NFSE>();
    private String quantidadeNotaRetidoFonte;
    private String totalNotaRetidoFonte;
    private String totalBoletoRetidoFonte;
    private String totalIssDevidoRetidoFonte;
    private Boolean mostraDadosGeraBoletoRetidoFonte = false;
    private String tituloPainelBoletos = "";
    private Date dataIniRF;
    private Date dataFimRF;
    private Date dataIniBolotoGeradoRF;
    private Date dataFimBolotoGeradoRF;
    private String numeroDocumentoRF;
    private Date dataHoje = new Date();
    private Date dataMinimaBuscaRF = new Date();
    private NFSE notaSelecionada = new NFSE();
    private String tipooServico;
    private Boolean guiasGeradasRendered = true;

    public GeraBoleto() {

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();
        this.currentUser = (Usuario) session.getAttribute("usuario");
        this.template = AutenticaUsuario.verificaTemplate(this.currentUser);

        if (currentUser.getCcm().getCodigoEventualGemmap() == null) {
            aliquota = currentUser.getCcm().getListaServicosPrestados().get(0).getAliquotaJ().toString();
            String[] Servicos = currentUser.getCcm().getServicosPrestados().split(";");
            Servicos = Servicos[0].split(",");
             tipooServico = Servicos[1];             
        } else {
            aliquota = "0";
            tipooServico = "E";
        }
        
        boletosSNFlAux = new ArrayList<BoletosSF>();
        buscaBoletosEmAbertosRetidoFonte();
        buscaNotasEmAbertoRetidoFonte();
        if (listaBoletosRetidoFonte.isEmpty()) {
        } else {
            try {
                verificaBaixaBoletosRF();
            } catch (NullPointerException ex) {
            }
        }
        if (currentUser.getCcm().isSimples()) {
            tituloPainelBoletos = "Guias de Pagamento Simples Nacional";
            geraBoleto = false;
            botaoSegundaVia = false;
            tabelaBoletos = false;
            tabelaSNF = true;
            buscaSimples = true;
            boletosSNF = new ArrayList<BoletosSF>();
            buscaBoletosSimplesNacional();
        } else if (tipooServico.equals("F")) {
            tituloPainelBoletos = "Guias de Pagamento Geradas Faturamento";
            tabelaBoletos = true;
            tabelaSNF = false;
            buscaSimples = false;
            botaoSegundaVia = true;
            geraBoleto = true;
            ficha = ConfiguracaoSyst.FICHA_VARIAVEL;
            buscaNotasEmAberto();
            buscaBoletosEmAbertos();
            if (listaBoletos.isEmpty()) {
            } else {
                try {
                    verificaBaixaBoletos();
                } catch (NullPointerException ex) {
                }
            }
        } else if (tipooServico.equals("M")) {
            tituloPainelBoletos = "Guias de Pagamento Geradas Fixo";
            geraBoleto = false;
            botaoSegundaVia = true;
            tabelaBoletos = false;
            tabelaSNF = true;
            buscaSimples = false;
            boletosSNF = new ArrayList<BoletosSF>();
            ficha = "00" + ConfiguracaoSyst.FICHA_FIXO;
            buscaBoletosFixo();
        } else if(tipooServico.equals("E")){
            geraBoleto = false;
            botaoSegundaVia = false;
            tabelaBoletos = false;
            tabelaSNF = false;
            buscaSimples = false;
            guiasGeradasRendered = false;
        }
    }

    public void segundaViaFixo() {

        SegundaViaFixo segundaVia = new SegundaViaFixo();
        try {
            segundaVia = new BuscaSegundaViaFixo().retornaBoletoFixo(currentUser.getCcm().getCodISSGemmap(), Integer.parseInt(ConfiguracaoSyst.anoEx()), Integer.parseInt(ficha), Integer.parseInt(fixoSelecionado.getNumeroDocumento()), ConfiguracaoSyst.CODIGO_CEDENTE);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro ao se conectar com a Prefeitura Municipal", ""));
        }

        /*
         * INFORMANDO DADOS SOBRE O CEDENTE.
         */
        Cedente cedente = new Cedente(ConfiguracaoSyst.NOME_CIDADE, ConfiguracaoSyst.CPFCNPJ_PREFEITURA);

        /*
         * INFORMANDO DADOS SOBRE O SACADO.
         */
        Sacado sacado = new Sacado(currentUser.getCcm().getNomeRazao(), currentUser.getCcm().getCnpjCpf());

        // Informando o endereço do sacado.
        Endereco enderecoSac = new Endereco();
        enderecoSac.setUF(UnidadeFederativa.SP);
        enderecoSac.setLocalidade(currentUser.getCcm().getLogradouro().getBairro().getCidade().getNome());
        enderecoSac.setCep(new CEP(currentUser.getCcm().getLogradouro().getCep()));

        if (currentUser.getCcm().getRua() == null) {
            enderecoSac.setLogradouro(currentUser.getCcm().getLogradouro().getNome());
            enderecoSac.setBairro(currentUser.getCcm().getLogradouro().getBairro().getNome());
        } else {
            enderecoSac.setLogradouro(currentUser.getCcm().getRua());
            enderecoSac.setBairro(currentUser.getCcm().getBairro());
        }
        enderecoSac.setNumero(currentUser.getCcm().getNumeroPredio().toString());
        sacado.addEndereco(enderecoSac);

        ContaBancaria contaBancaria = new ContaBancaria(BancosSuportados.HSBC.create());
        //contaBancaria.setNumeroDaConta(new NumeroDaConta(ConfiguracaoSyst.numeroConta, ConfiguracaoSyst.digitoConta));
        contaBancaria.setCarteira(new Carteira(ConfiguracaoSyst.CARTEIRA));
        //contaBancaria.setAgencia(new Agencia(ConfiguracaoSyst.numeroAgencia, ConfiguracaoSyst.digitoAgencia));

        Titulo titulo = new Titulo(contaBancaria, sacado, cedente);
        titulo.setNumeroDoDocumento(segundaVia.getNumeroDocumento());

        String nossoNumero = segundaVia.getNossoNumero();

        titulo.setNossoNumero(nossoNumero.substring(0, nossoNumero.length() - 1));
        titulo.setDigitoDoNossoNumero(nossoNumero.substring(nossoNumero.length() - 1));
        Double total = segundaVia.getTotal() - segundaVia.getTotalDesconto();
        titulo.setValor(BigDecimal.valueOf(total));
        titulo.setDataDoDocumento(new Date());

        DateFormat datafF = new SimpleDateFormat("dd/MM/yyyy");
        try {
            titulo.setDataDoVencimento(dataF.parse(segundaVia.getDataVencimento()));
        } catch (ParseException ex) {
            Logger.getLogger(GeraBoleto.class.getName()).log(Level.SEVERE, null, ex);
        }

        titulo.setTipoDeDocumento(TipoDeTitulo.RC_RECIBO);
        titulo.setAceite(EnumAceite.N);
        titulo.setDesconto(BigDecimal.ZERO);
        titulo.setDeducao(BigDecimal.ZERO);
        titulo.setMora(BigDecimal.ZERO);
        titulo.setAcrecimo(BigDecimal.ZERO);
        titulo.setValorCobrado(BigDecimal.ZERO);

        /*
         * INFORMANDO OS DADOS SOBRE O BOLETO.
         */

        String campoLivreStr = segundaVia.getCodigoBarras().substring(19);

        TesteCampoLivre compoLivre = new TesteCampoLivre();
        compoLivre.read(campoLivreStr);
        Boleto boleto = new Boleto(titulo, compoLivre);

        boleto.setLocalPagamento("PAGÁVEL EM QUALQUER BANCO ATÉ O VENCIMENTO.");
        boleto.setInstrucaoAoSacado("Finalidade: ISSQN FIXO ");
        boleto.setInstrucao1("");
        boleto.setInstrucao2("Após o vencimanto aplicar sobre a parcela:");
        boleto.setInstrucao3("Multa de 2% (dois por cento) ao mês, até o limite de");
        boleto.setInstrucao4("10% (dez por cento),acrescimentos de juros de mora de 1% ");
        boleto.setInstrucao5("(um por cento) ao mês, a partir do mês imediato ao vencimento,");
        boleto.setInstrucao6("sendo considerado mês completo qualquer fração deste.");
        boleto.setInstrucao8("Finalidade: ISSQN FIXO ");

        boleto.addTextosExtras("txtFcCarteira", "COB");
        boleto.addTextosExtras("txtFcEspecie", "R$");
        boleto.addTextosExtras("txtFcEspecieDocumento", "RC-CI");

        boleto.addTextosExtras("txtVlrIss", moeda.formata(segundaVia.getValorIss()));
        boleto.addTextosExtras("txtVrlTaxaLicenca", moeda.formata(segundaVia.getValorTaxaLicenca()));
        boleto.addTextosExtras("txtVlrTaxaIncendio", moeda.formata(segundaVia.getValorIncendio()));
        boleto.addTextosExtras("txtVlrExpediente", moeda.formata(segundaVia.getValorTaxaExpediente()));
        boleto.addTextosExtras("txtVlrSDesconto", moeda.formata(segundaVia.getTotal()));
        boleto.addTextosExtras("txtVlrCDesconto", moeda.formata(segundaVia.getTotal() - segundaVia.getTotalDesconto()));

        boleto.addTextosExtras("txtParcela", segundaVia.getParcela() + "/" + boletosSNF.size());
        boleto.addTextosExtras("txtRsParcela", segundaVia.getParcela() + "/" + boletosSNF.size());

        //boleto.addTextosExtras("txtRsCompetencia", segundaVia.);
        //addTextosExtras("txtRsAliquota", new Moeda().formata(boletoSelecionado.getAliquota()));
        //boleto.addTextosExtras("txtRsMovimentoMensal", "R$ " + moeda.formata(boletoSelecionado.getMovimento()));
        boleto.addTextosExtras("txtRsIM", currentUser.getCcm().getIm());

        //boleto.addTextosExtras("txtFcCompetencia", dataF.format(boletoSelecionado.getDataInicioCompetencia()) + " a " + dataF.format(boletoSelecionado.getDataFimCompetencia()));
        //boleto.addTextosExtras("txtFcAliquota", boletoSelecionado.getAliquota().toString());
        //boleto.addTextosExtras("txtFcMovimentoMensal", "R$ " + boletoSelecionado.getMovimento());

        boleto.addTextosExtras("txtAnoEx", ConfiguracaoSyst.anoEx());


        boleto.addTextosExtras("txtFicha", ficha);
        boleto.addTextosExtras("txtNro", segundaVia.getNumeroDocumento());

        boleto.addTextosExtras("txtFcDescontoAbatimento", "R$ ");
        boleto.addTextosExtras("txtFcOutraDeducao", "R$ ");
        boleto.addTextosExtras("txtFcMoraMulta", "R$ ");
        boleto.addTextosExtras("txtFcOutroAcrescimo", "R$ ");
        boleto.addTextosExtras("txtFcValorCobrado", "R$ ");


        FacesContext facesContext = FacesContext.getCurrentInstance();
        ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
        String arquivo = scontext.getRealPath("/PDF/BoletoPersonalizadoFixo.pdf");


        BoletoViewer boletoViewer = new BoletoViewer(boleto);
        boletoViewer.setTemplate(arquivo);

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

    public void filtraBoletoSimplesNacional() {

        if (mesSelecionado == null) {
            if (boletosSNFlAux.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Escolha o mês de competêcia ", ""));
            } else {
                boletosSNF = boletosSNFlAux;
            }
        } else {
            if (boletosSNF.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sem boletos ", ""));
            } else {
                if (boletosSNFlAux.isEmpty()) {
                    boletosSNFlAux = boletosSNF;

                    List<BoletosSF> listaAux = new ArrayList<BoletosSF>();

                    for (BoletosSF boleto : boletosSNF) {
                        if (boleto.getCompetencia().equals(mesSelecionado)) {
                            listaAux.add(boleto);
                        }
                    }
                    boletosSNF = new ArrayList<BoletosSF>();
                    boletosSNF = listaAux;
                } else {
                    boletosSNF = boletosSNFlAux;

                    List<BoletosSF> listaAux = new ArrayList<BoletosSF>();

                    for (BoletosSF boleto : boletosSNF) {
                        if (boleto.getCompetencia().equals(mesSelecionado)) {
                            listaAux.add(boleto);
                        }
                    }
                    boletosSNF = new ArrayList<BoletosSF>();
                    boletosSNF = listaAux;
                }
            }
            System.out.println(mesSelecionado);
        }


    }

    private void buscaBoletosSimplesNacional() {
        ListaBoletoSF listaSimplesNacional = new BuscaBoletoSimlesNacional().retornaBoletoSimplesNacional(ConfiguracaoSyst.anoEx(), currentUser.getCcm().getCodISSGemmap().toString());
        boletosSNF = listaSimplesNacional.getListaBoleto();
    }

    private void buscaBoletosFixo() {

        ListaBoletoSF listaFixo = new BuscaBoletosFixo().retornaBoletoFixo(ConfiguracaoSyst.anoEx(), currentUser.getCcm().getCodISSGemmap().toString());
        boletosSNF = listaFixo.getListaBoleto();
        textoColunaCommtParcela = "Parcela";
        if (boletosSNF != null) {
            for (int i = 0; i < boletosSNF.size(); i++) {

                BoletosSF bsnf = new BoletosSF();
                bsnf = boletosSNF.get(i);
                Date dataVencimento = new Date();
                try {
                    dataVencimento = dataF.parse(bsnf.getVencimento());
                    if (bsnf.getCompetencia().equals("Parcela Única") && dataAtual().after(dataVencimento)) {
                        boletosSNF.remove(i);
                    }

                    boletosSNF.get(i).setCompetencia(i + 1 + "/" + boletosSNF.size());

                } catch (ParseException ex) {
                    Logger.getLogger(GeraBoleto.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private Date dataAtual() {
        DateFormat dataf = new SimpleDateFormat("yyyy");
        Date data = Calendar.getInstance(Locale.getDefault()).getTime();
        return data;
    }

    private void verificaBaixaBoletos() {

        ListaBoletoSemBaixa listaBoletosBaixados = new BuscaBoletosBaixados().retornaBoletosBaixados(ConfiguracaoSyst.anoEx(), this.currentUser.getCcm().getCodISSGemmap().toString());
        DAOBoleto boletoDao = new DAOBoleto();
        if (listaBoletosBaixados.getBoletosSemBaixa() != null) {

            for (BoletoIssMap boletoIssMap : listaBoletos) {
                for (BoletoSemBaixa boleto : listaBoletosBaixados.getBoletosSemBaixa()) {
                    if (boletoIssMap.getNumeroDocumento().equals(boleto.getCgarqNro())) {
                        try {
                            boletoIssMap.setBaixa(true);
                            boletoIssMap.setLogoSituacao("ativado.png");
                            boletoDao.salvarMerge(boletoIssMap);
                            //buscaBoletosEmAbertos();
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Boletos recebidos recentemente ", ""));
                        } catch (SQLException ex) {
                            Logger.getLogger(GeraBoleto.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        } else {
            System.out.println("Lista VaziaAAAAAA");
        }
    }

    private void verificaBaixaBoletosRF() {

        ListaBoletoSemBaixa listaBoletosBaixados = new BuscaBoletosBaixados().retornaBoletosBaixados(ConfiguracaoSyst.anoEx(), this.currentUser.getCcm().getCodISSGemmap().toString());
        DAOBoleto boletoDao = new DAOBoleto();
        if (listaBoletosBaixados.getBoletosSemBaixa() != null) {

            for (BoletoIssMap boletoIssMap : listaBoletosRetidoFonte) {
                for (BoletoSemBaixa boleto : listaBoletosBaixados.getBoletosSemBaixa()) {
                    if (boletoIssMap.getNumeroDocumento().equals(boleto.getCgarqNro())) {
                        try {
                            boletoIssMap.setBaixa(true);
                            boletoIssMap.setLogoSituacao("ativado.png");
                            boletoDao.salvarMerge(boletoIssMap);
                            //buscaBoletosEmAbertos();
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Boletos recebidos recentemente ", ""));
                        } catch (SQLException ex) {
                            Logger.getLogger(GeraBoleto.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        } else {
            System.out.println("Lista VaziaAAAAAA");
        }
    }

    private void buscaBoletosEmAbertos() {
        listaBoletos = new ArrayList<BoletoIssMap>();
        listaBoletos = new DAOBoleto().retornaBoletosEmAbertoVariavel(currentUser.getCcm());
    }

    private void buscaBoletosEmAbertosRetidoFonte() {
        listaBoletosRetidoFonte = new ArrayList<BoletoIssMap>();
        listaBoletosRetidoFonte = new DAOBoleto().retornaBoletosEmAbertoRetidoFonte(currentUser.getCcm());
    }

    private void buscaNotasEmAberto() {

        DAONFSE daoNfse = new DAONFSE();
        this.notas = new ArrayList<NFSE>();

        try {
            this.notas = daoNfse.buscaNotasAbertasPrestador(currentUser.getCcm().getCnpjCpf());

            if (notas.isEmpty()) {
            } else {
                Double valorTotalNota = 0.0;
                for (NFSE nfse : notas) {
                    valorTotalNota = valorTotalNota + nfse.getValorBaseCalculo();
                }

                quantidadeNota = String.valueOf(notas.size());
                totalNota = moeda.formata(valorTotalNota);

                totalBoleto = moeda.formata(valorTotalNota * Moeda.convertPorcentagem(aliquota));
                mostraDados = true;
            }

        } catch (Exception ex) {
            Logger.getLogger(GeraBoleto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void buscaNotasEmAbertoRetidoFonte() {

        DAONFSE daoNfse = new DAONFSE();

        listaNotasRetidoFonte = new ArrayList<NFSE>();
        try {

            listaNotasRetidoFonte = daoNfse.buscaNotasAbertasRetidoFonte(currentUser.getCcm().getCnpjCpf());
            if (listaNotasRetidoFonte.isEmpty()) {
            } else {
                Double valorTotalNota = 0.0;
                Double valorTotalIss = 0.0;
                for (NFSE nfse : listaNotasRetidoFonte) {
                    valorTotalNota = valorTotalNota + nfse.getValorBaseCalculo();
                    valorTotalIss = valorTotalIss + nfse.getValorIss();
                }

                quantidadeNotaRetidoFonte = String.valueOf(listaNotasRetidoFonte.size());
                totalNotaRetidoFonte = moeda.formata(valorTotalNota);
                totalIssDevidoRetidoFonte = moeda.formata(valorTotalIss);

//              totalBoletoRetidoFonte = moeda.formata(valorTotalNota * Moeda.convertPorcentagem(aliquota));
                mostraDadosGeraBoletoRetidoFonte = true;
            }

        } catch (Exception ex) {
            Logger.getLogger(GeraBoleto.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void filtraNotasPrestador() throws Exception {
        DAONFSE daoNfse = new DAONFSE();
        this.notas = new ArrayList<NFSE>();
        String tipo = "P";

        if ((this.dataIni == null) && (this.dataFim == null)) {
            buscaNotasEmAberto();
            return;
        }

        if ((this.dataIni != null) && (this.dataFim != null)) {

            DateFormat datafI = new SimpleDateFormat("dd/MM/yyyy");

            String dataStrIni = datafI.format(dataIni);
            dataIni = new Date(datafI.parse(dataStrIni).getTime());

            DateFormat datafF = new SimpleDateFormat("dd/MM/yyyy");
            String dataStrFim = datafF.format(dataFim);
            dataFim = new Date(datafF.parse(dataStrFim).getTime());

            if (dataIni.after(dataFim)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "Datas Incorretas"));
                return;
            }
            this.notas = daoNfse.buscaNotasPorFiltroBoleto(currentUser.getCcm().getCnpjCpf(), "", "", dataIni, dataFim, tipo);


            if (notas.isEmpty()) {
                limpaBusca();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Nenhuma nota foi encontrada", ""));
            } else {
                Double valorTotalNota = 0.0;
                for (NFSE nfse : notas) {
                    valorTotalNota = valorTotalNota + nfse.getValorBaseCalculo();
                }

                quantidadeNota = String.valueOf(notas.size());
                totalNota = moeda.formata(valorTotalNota);

                totalBoleto = moeda.formata(valorTotalNota * Moeda.convertPorcentagem(aliquota));
                mostraDados = true;
            }


        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "Informe o periodo de busca"));
        }
    }

    public void filtraNotasRetidoFonte() throws Exception {

        DAONFSE daoNfse = new DAONFSE();
        this.listaNotasRetidoFonte = new ArrayList<NFSE>();

        if ((this.dataIniRF == null) && (this.dataFimRF == null)) {
            buscaNotasEmAbertoRetidoFonte();
            return;
        }

        if ((this.dataIniRF != null) && (this.dataFimRF != null)) {

            DateFormat datafI = new SimpleDateFormat("dd/MM/yyyy");

            String dataStrIni = datafI.format(dataIniRF);
            dataIniRF = new Date(datafI.parse(dataStrIni).getTime());

            DateFormat datafF = new SimpleDateFormat("dd/MM/yyyy");
            String dataStrFim = datafF.format(dataFimRF);
            dataFimRF = new Date(datafF.parse(dataStrFim).getTime());

            if (dataIniRF.after(dataFimRF)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "Datas Incorretas"));
                mostraDadosGeraBoletoRetidoFonte = false;
                return;
            }
            this.listaNotasRetidoFonte = daoNfse.buscaNotasAbertasRetidoFonte(currentUser.getCcm().getCnpjCpf(), dataIniRF, dataFimRF);


            if (listaNotasRetidoFonte.isEmpty()) {
                limpaBuscaRF();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Nenhuma nota foi encontrada", ""));
            } else {
                Double valorTotalNota = 0.0;
                Double valorTotalIss = 0.0;
                for (NFSE nfse : listaNotasRetidoFonte) {
                    valorTotalNota = valorTotalNota + nfse.getValorBaseCalculo();
                    valorTotalIss = valorTotalIss + nfse.getValorIss();
                }

                quantidadeNotaRetidoFonte = String.valueOf(listaNotasRetidoFonte.size());
                totalNotaRetidoFonte = moeda.formata(valorTotalNota);
                totalIssDevidoRetidoFonte = moeda.formata(valorTotalIss);

//              totalBoletoRetidoFonte = moeda.formata(valorTotalNota * Moeda.convertPorcentagem(aliquota));
                mostraDadosGeraBoletoRetidoFonte = true;
            }


        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "Informe o periodo de busca"));
        }
    }

    public void limpaBusca() {

        this.notas = new ArrayList<NFSE>();
        quantidadeNota = "";
        totalNota = "";
        totalBoleto = "";
        mostraDados = false;
    }

    public void limpaBuscaRF() {

        listaNotasRetidoFonte = new ArrayList<NFSE>();
        quantidadeNotaRetidoFonte = "";
        totalNotaRetidoFonte = "";
        totalIssDevidoRetidoFonte = "";
        mostraDadosGeraBoletoRetidoFonte = false;

    }

    public void limpaBuscaBoleto() {

        this.listaBoletos = new ArrayList<BoletoIssMap>();


    }

    public void segundaViaBoleto() {

        escolhePDF = "";

        DateFormat dataF = new SimpleDateFormat("dd/MM/yyyy");

        /*
         * INFORMANDO DADOS SOBRE O CEDENTE.
         */
        Cedente cedente = new Cedente(ConfiguracaoSyst.NOME_CIDADE, ConfiguracaoSyst.CPFCNPJ_PREFEITURA);

        /*
         * INFORMANDO DADOS SOBRE O SACADO.
         */
        Sacado sacado = new Sacado(boletoSelecionado.getSacado().getNomeRazao(), boletoSelecionado.getSacado().getCnpjCpf());

        // Informando o endereço do sacado.
        Endereco enderecoSac = new Endereco();
        enderecoSac.setUF(UnidadeFederativa.SP);
        enderecoSac.setLocalidade(boletoSelecionado.getSacado().getLogradouro().getBairro().getCidade().getNome());
        enderecoSac.setCep(new CEP(boletoSelecionado.getSacado().getLogradouro().getCep()));

        if (boletoSelecionado.getSacado().getRua() == null) {
            enderecoSac.setLogradouro(boletoSelecionado.getSacado().getLogradouro().getNome());
            enderecoSac.setBairro(boletoSelecionado.getSacado().getLogradouro().getBairro().getNome());
        } else {
            enderecoSac.setLogradouro(boletoSelecionado.getSacado().getRua());
            enderecoSac.setBairro(boletoSelecionado.getSacado().getBairro());
        }
        enderecoSac.setNumero(boletoSelecionado.getSacado().getNumeroPredio().toString());
        sacado.addEndereco(enderecoSac);

        ContaBancaria contaBancaria = new ContaBancaria(BancosSuportados.HSBC.create());
        NumeroDaConta numeroConta = new NumeroDaConta();
        numeroConta.setCodigoDaConta(ConfiguracaoSyst.NUMERO_CONTA);
        numeroConta.setDigitoDaConta(ConfiguracaoSyst.DIGITO_CONTA);
        contaBancaria.setNumeroDaConta(numeroConta);
        contaBancaria.setCarteira(new Carteira(ConfiguracaoSyst.CARTEIRA));
        contaBancaria.setAgencia(new Agencia(ConfiguracaoSyst.NUMERO_AGENCIA));

        Titulo titulo = new Titulo(contaBancaria, sacado, cedente);
        titulo.setNumeroDoDocumento(boletoSelecionado.getNumeroDocumento());

        String nossoNumero = boletoSelecionado.getNossoNumero();

        titulo.setNossoNumero(nossoNumero.substring(0, nossoNumero.length() - 3));
        titulo.setDigitoDoNossoNumero(nossoNumero.substring(nossoNumero.length() - 3));
        titulo.setValor(BigDecimal.valueOf(boletoSelecionado.getValorDocumento()));
        titulo.setDataDoDocumento(boletoSelecionado.getDataDocumento());
        titulo.setDataDoVencimento(boletoSelecionado.getDataVencimenro());

        titulo.setTipoDeDocumento(TipoDeTitulo.RC_RECIBO);
        titulo.setAceite(EnumAceite.N);
        titulo.setDesconto(BigDecimal.ZERO);
        titulo.setDeducao(BigDecimal.ZERO);
        titulo.setMora(BigDecimal.ZERO);
        titulo.setAcrecimo(BigDecimal.ZERO);
        titulo.setValorCobrado(BigDecimal.ZERO);

        /*
         * INFORMANDO OS DADOS SOBRE O BOLETO.
         */

        TesteCampoLivre compoLivre = new TesteCampoLivre();
        compoLivre.read(boletoSelecionado.getCampoLivre());
        Boleto boleto = new Boleto(titulo, compoLivre);

        boleto.setLocalPagamento("PAGÁVEL EM QUALQUER BANCO ATÉ O VENCIMENTO.");
        boleto.setInstrucaoAoSacado("Finalidade: ISSQN VARIÁVEL ");
        boleto.setInstrucao1("");
        boleto.setInstrucao2("Após o vencimanto aplicar sobre a parcela:");
        boleto.setInstrucao3("Multa de 2% (dois por cento) ao mês, até o limite de 10% (dez por cento),");
        boleto.setInstrucao4("acrescimentos de juros de mora de 1% (um por cento) ao mês, a partir do mês");
        boleto.setInstrucao5("imediato ao vencimento, sendo considerado mês completo qualquer fração deste.");
        boleto.setInstrucao8("Finalidade: ISSQN VARIÁVEL ");

        boleto.addTextosExtras("txtFcCarteira", "COB");
        boleto.addTextosExtras("txtFcEspecie", "R$");
        boleto.addTextosExtras("txtFcEspecieDocumento", "RC-CI");

        boleto.addTextosExtras("txtRsCompetencia", dataF.format(boletoSelecionado.getDataInicioCompetencia()) + " a " + dataF.format(boletoSelecionado.getDataFimCompetencia()));
        boleto.addTextosExtras("txtRsAliquota", new Moeda().formata(boletoSelecionado.getAliquota()));
        boleto.addTextosExtras("txtRsMovimentoMensal", "R$ " + moeda.formata(boletoSelecionado.getMovimento()));
        boleto.addTextosExtras("txtRsIM", boletoSelecionado.getSacado().getIm());

        boleto.addTextosExtras("txtFcCompetencia", dataF.format(boletoSelecionado.getDataInicioCompetencia()) + " a " + dataF.format(boletoSelecionado.getDataFimCompetencia()));
        boleto.addTextosExtras("txtFcAliquota", boletoSelecionado.getAliquota().toString());
        boleto.addTextosExtras("txtFcMovimentoMensal", "R$ " + boletoSelecionado.getMovimento());

        boleto.addTextosExtras("txtAnoEx", ConfiguracaoSyst.anoEx());
        boleto.addTextosExtras("txtFicha", ficha);
        boleto.addTextosExtras("txtNro", boletoSelecionado.getNumeroDocumento());

        boleto.addTextosExtras("txtFcDescontoAbatimento", "R$ ");
        boleto.addTextosExtras("txtFcOutraDeducao", "R$ ");
        boleto.addTextosExtras("txtFcMoraMulta", "R$ ");
        boleto.addTextosExtras("txtFcOutroAcrescimo", "R$ ");
        boleto.addTextosExtras("txtFcValorCobrado", "R$ ");


        List<NFSE> listaNotas = new ArrayList<NFSE>();

        listaNotas = boletoSelecionado.getNotasBoleto();



        if (listaNotas.isEmpty()) {
            System.out.println("Lista vazia");
        } else {
            /*
             * Informando o historico de notas:
             */
            if (listaNotas.size() > 18) {
                HistoricoNota historicoDespesa = new HistoricoNota();

                for (int i = 0; i <= 17; i++) {
                    historicoDespesa.add(dataF.format(listaNotas.get(i).getDataEmissao()), listaNotas.get(i).getNomeRazaoTomador(), "R$ " + moeda.formata(listaNotas.get(i).getValorBaseCalculo()), NotaUtil.fomrmataNumeroNota(listaNotas.get(i).getNumeroNota()));
                }

                historicoDespesa.add("", "", "", "");
                historicoDespesa.add("", "CONTINUA CONFORME RELAÇÃO EM ANEXO...", "", "");

                boleto.addTextosExtras("txtRsHistoricoDespesaData", historicoDespesa.getDetalhamentoData());
                boleto.addTextosExtras("txtRsHistoricoDespesaDescricao", historicoDespesa.getDetalhamentoDescricao());
                boleto.addTextosExtras("txtRsHistoricoDespesaValor", historicoDespesa.getDetalhamentoValor());
                boleto.addTextosExtras("txtRsHistoricoDespesaNumeroNota", historicoDespesa.getDetalhamentoNumeroNota());


                HistoricoNota historicoDespesaS = new HistoricoNota();

                for (int i = 18; i < listaNotas.size(); i++) {
                    historicoDespesaS.add(dataF.format(listaNotas.get(i).getDataEmissao()), listaNotas.get(i).getNomeRazaoTomador(), "R$ " + moeda.formata(listaNotas.get(i).getValorBaseCalculo()), NotaUtil.fomrmataNumeroNota(listaNotas.get(i).getNumeroNota()));
                }

                historicoDespesaS.add("", "", "", "");
                historicoDespesaS.add("", "", "TOTAL:  R$" + moeda.formata(boletoSelecionado.getValorDocumento()), "");

                boleto.addTextosExtras("txtRsHistoricoDespesaDataS", historicoDespesaS.getDetalhamentoData());
                boleto.addTextosExtras("txtRsHistoricoDespesaDescricaoS", historicoDespesaS.getDetalhamentoDescricao());
                boleto.addTextosExtras("txtRsHistoricoDespesaValorS", historicoDespesaS.getDetalhamentoValor());
                boleto.addTextosExtras("txtRsHistoricoDespesaNumeroNotaS", historicoDespesaS.getDetalhamentoNumeroNota());

                boleto.addTextosExtras("txtSacadoS", boletoSelecionado.getSacado().getNomeRazao() + " - " + boletoSelecionado.getSacado().getCnpjCpf());
                boleto.addTextosExtras("txtDocumentoS", boletoSelecionado.getNumeroDocumento());
                boleto.addTextosExtras("txtDataDocumentoS", dataF.format(boletoSelecionado.getDataDocumento()));
                boleto.addTextosExtras("txtCompetenciaS", dataF.format(boletoSelecionado.getDataInicioCompetencia()) + " a " + dataF.format(boletoSelecionado.getDataFimCompetencia()));
                boleto.addTextosExtras("txtNossoNumeroS", boletoSelecionado.getNossoNumero());
                //boleto.addTextosExtras("txtAgenciaS", ConfiguracaoSyst.numeroAgencia.toString() + "-" + ConfiguracaoSyst.digitoAgencia + "/" + ConfiguracaoSyst.numeroConta + "-" + ConfiguracaoSyst.digitoConta);

                escolhePDF = "BoletoPersonalizadoDetalhesDuasFolhas.pdf";

            } else {
                HistoricoNota historicoDespesa = new HistoricoNota();
                for (NFSE nota : listaNotas) {
                    historicoDespesa.add(dataF.format(nota.getDataEmissao()), nota.getNomeRazaoTomador(), "R$ " + moeda.formata(nota.getValorBaseCalculo()), NotaUtil.fomrmataNumeroNota(nota.getNumeroNota()));
                }

                historicoDespesa.add("", "", "", "");
                historicoDespesa.add("", "", "TOTAL:  R$" + moeda.formata(boletoSelecionado.getValorDocumento()), "");

                boleto.addTextosExtras("txtRsHistoricoDespesaData", historicoDespesa.getDetalhamentoData());
                boleto.addTextosExtras("txtRsHistoricoDespesaDescricao", historicoDespesa.getDetalhamentoDescricao());
                boleto.addTextosExtras("txtRsHistoricoDespesaValor", historicoDespesa.getDetalhamentoValor());
                boleto.addTextosExtras("txtRsHistoricoDespesaNumeroNota", historicoDespesa.getDetalhamentoNumeroNota());

                escolhePDF = "BoletoPersonalizadoDetalhes.pdf";
            }

        }

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
        String arquivo = scontext.getRealPath("/PDF/" + escolhePDF);


        BoletoViewer boletoViewer = new BoletoViewer(boleto);
        boletoViewer.setTemplate(arquivo);

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

    public void imprimeBoleto() {        
        
        try {
            filtraNotasPrestador();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "Verifique a busca notas"));
            limpaBusca();
            return;
        }
        
        if(notas.isEmpty()){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO BUSCAR NOTAS ", "Faça uma nova busca"));
            limpaBusca();
            return;
        }else{
            if(notas.get(0).getBoletoPagamento() != null){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO BUSCAR NOTAS ", "Notas já vinculadas a guia de pagamento"));
                limpaBusca();
                return;
            }
        }

        DateFormat dataF = new SimpleDateFormat("dd/MM/yyyy");

        /*
         * INFORMANDO DADOS SOBRE O CEDENTE.
         */
        Cedente cedente = new Cedente(ConfiguracaoSyst.NOME_CIDADE, ConfiguracaoSyst.CPFCNPJ_PREFEITURA);

        /*
         * INFORMANDO DADOS SOBRE O SACADO.
         */
        Sacado sacado = new Sacado(currentUser.getCcm().getNomeRazao(), currentUser.getCcm().getCnpjCpf());

        // Informando o endereço do sacado.
        Endereco enderecoSac = new Endereco();
        enderecoSac.setUF(UnidadeFederativa.SP);
        enderecoSac.setLocalidade(currentUser.getCcm().getLogradouro().getBairro().getCidade().getNome());
        enderecoSac.setCep(new CEP(currentUser.getCcm().getLogradouro().getCep()));

        if (currentUser.getCcm().getRua() == null) {
            enderecoSac.setLogradouro(currentUser.getCcm().getLogradouro().getNome());
            enderecoSac.setBairro(currentUser.getCcm().getLogradouro().getBairro().getNome());
        } else {
            enderecoSac.setLogradouro(currentUser.getCcm().getRua());
            enderecoSac.setBairro(currentUser.getCcm().getBairro());
        }
        enderecoSac.setNumero(currentUser.getCcm().getNumeroPredio().toString());
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

        String mes = dataF.format(new Date());
        String s[] = mes.split("/");
        mes = s[1];

        DadosBoletoWS dadosBoletoWs = new DadosBoletoWS();
        
        String dataSTRInicio;
        String dataSTRFim;
        
        if ((this.dataIni == null) && (this.dataFim == null)) {
            dataSTRInicio = "0";
            dataSTRFim = "0";
        }else{
            
            DateFormat datafI = new SimpleDateFormat("yyyy-MM-dd");

            dataSTRInicio = datafI.format(dataIni);
            dataSTRFim = datafI.format(dataFim);
        }

        dadosBoletoWs = new BuscaDadosBoletoGemmap().retornaMobiliario(ConfiguracaoSyst.anoEx(), ConfiguracaoSyst.CODIGO_CEDENTE, ficha, Moeda.convert(totalBoleto).toString(), mes, currentUser.getCcm().getCodISSGemmap().toString(), dataSTRInicio, dataSTRFim, currentUser.getCcm().getCnpjCpf(), "p", "0");

        System.out.println("Resultado dodosWS nossoNumero  >>>>>>" + dadosBoletoWs.getNossoNumero());

        // Informando dados sobre a conta bancária do título.
        ContaBancaria contaBancaria = new ContaBancaria(BancosSuportados.HSBC.create());
        contaBancaria.setNumeroDaConta(new NumeroDaConta(ConfiguracaoSyst.NUMERO_CONTA, ConfiguracaoSyst.DIGITO_CONTA));
        contaBancaria.setCarteira(new Carteira(ConfiguracaoSyst.CARTEIRA));
        contaBancaria.setAgencia(new Agencia(ConfiguracaoSyst.NUMERO_AGENCIA));

        Titulo titulo = new Titulo(contaBancaria, sacado, cedente);
        titulo.setNumeroDoDocumento(dadosBoletoWs.getNumeroDocumento());

        String nossoNumero = dadosBoletoWs.getNossoNumero();

        titulo.setNossoNumero(nossoNumero.substring(0, nossoNumero.length() - 3));
        titulo.setDigitoDoNossoNumero(nossoNumero.substring(nossoNumero.length() - 3));

        titulo.setValor(BigDecimal.valueOf(Moeda.convert(totalBoleto)));
        titulo.setDataDoDocumento(new Date());
        try {
            titulo.setDataDoVencimento(new Date(dataF.parse(dadosBoletoWs.getDataVencimento()).getTime()));
        } catch (ParseException ex) {
            System.out.println("ERRO AO CONVERTER A DATA >>>>>" + ex.getMessage());
        }
        titulo.setTipoDeDocumento(TipoDeTitulo.RC_RECIBO);
        titulo.setAceite(EnumAceite.N);
        titulo.setDesconto(BigDecimal.ZERO);
        titulo.setDeducao(BigDecimal.ZERO);
        titulo.setMora(BigDecimal.ZERO);
        titulo.setAcrecimo(BigDecimal.ZERO);
        titulo.setValorCobrado(BigDecimal.ZERO);

        /*
         * INFORMANDO OS DADOS SOBRE O BOLETO.
         */

        String campoLivreStr = dadosBoletoWs.getCodBarras().substring(19);

        TesteCampoLivre compoLivre = new TesteCampoLivre();
        compoLivre.read(campoLivreStr);
        Boleto boleto = new Boleto(titulo, compoLivre);

        boleto.setLocalPagamento("PAGÁVEL EM QUALQUER BANCO ATÉ O VENCIMENTO.");
        boleto.setInstrucaoAoSacado("Finalidade: ISSQN VARIÁVEL ");
        boleto.setInstrucao1("");
        boleto.setInstrucao2("Após o vencimanto aplicar sobre a parcela:");
        boleto.setInstrucao3("Multa de 2% (dois por cento) ao mês, até o limite de 10% (dez por cento),");
        boleto.setInstrucao4("acrescimentos de juros de mora de 1% (um por cento) ao mês, a partir do mês");
        boleto.setInstrucao5("imediato ao vencimento, sendo considerado mês completo qualquer fração deste.");
        boleto.setInstrucao8("Finalidade: ISSQN VARIÁVEL ");


        boleto.addTextosExtras("txtFcCarteira", "SR.");
        boleto.addTextosExtras("txtFcEspecie", "R$");
        boleto.addTextosExtras("txtFcEspecieDocumento", "RC-CI");

        boleto.addTextosExtras("txtRsCompetencia", dataF.format(notas.get(0).getDataEmissao()) + " a " + dataF.format(notas.get(notas.size() - 1).getDataEmissao()));
        boleto.addTextosExtras("txtRsAliquota", aliquota);
        boleto.addTextosExtras("txtRsMovimentoMensal", "R$ " + totalNota);
        boleto.addTextosExtras("txtRsIM", currentUser.getCcm().getIm());

        boleto.addTextosExtras("txtFcCompetencia", dataF.format(notas.get(0).getDataEmissao()) + " a " + dataF.format(notas.get(notas.size() - 1).getDataEmissao()));
        boleto.addTextosExtras("txtFcAliquota", aliquota);
        boleto.addTextosExtras("txtFcMovimentoMensal", "R$ " + totalNota);

        boleto.addTextosExtras("txtAnoEx", ConfiguracaoSyst.anoEx());
        boleto.addTextosExtras("txtFicha", ficha);
        boleto.addTextosExtras("txtNro", dadosBoletoWs.getNumeroDocumento());

        boleto.addTextosExtras("txtFcDescontoAbatimento", "R$ ");
        boleto.addTextosExtras("txtFcOutraDeducao", "R$ ");
        boleto.addTextosExtras("txtFcMoraMulta", "R$ ");
        boleto.addTextosExtras("txtFcOutroAcrescimo", "R$ ");
        boleto.addTextosExtras("txtFcValorCobrado", "R$ ");

        if (notas.isEmpty()) {
            System.out.println("Lista vazia");
        } else {
            /*
             * Informando o historico de notas:
             */
            if (notas.size() > 18) {
                HistoricoNota historicoDespesa = new HistoricoNota();

                for (int i = 0; i <= 17; i++) {
                    historicoDespesa.add(dataF.format(notas.get(i).getDataEmissao()), notas.get(i).getNomeRazaoTomador(), "R$ " + moeda.formata(notas.get(i).getValorBaseCalculo()), NotaUtil.fomrmataNumeroNota(notas.get(i).getNumeroNota()));
                }

                historicoDespesa.add("", "", "", "");
                historicoDespesa.add("", "CONTINUA CONFORME RELAÇÃO EM ANEXO...", "", "");

                boleto.addTextosExtras("txtRsHistoricoDespesaData", historicoDespesa.getDetalhamentoData());
                boleto.addTextosExtras("txtRsHistoricoDespesaDescricao", historicoDespesa.getDetalhamentoDescricao());
                boleto.addTextosExtras("txtRsHistoricoDespesaValor", historicoDespesa.getDetalhamentoValor());
                boleto.addTextosExtras("txtRsHistoricoDespesaNumeroNota", historicoDespesa.getDetalhamentoNumeroNota());


                HistoricoNota historicoDespesaS = new HistoricoNota();

                for (int i = 18; i < notas.size(); i++) {
                    historicoDespesaS.add(dataF.format(notas.get(i).getDataEmissao()), notas.get(i).getNomeRazaoTomador(), "R$ " + moeda.formata(notas.get(i).getValorBaseCalculo()), NotaUtil.fomrmataNumeroNota(notas.get(i).getNumeroNota()));
                }

                historicoDespesaS.add("", "", "", "");
                historicoDespesaS.add("", "", "TOTAL:  R$" + this.totalNota, "");

                boleto.addTextosExtras("txtRsHistoricoDespesaDataS", historicoDespesaS.getDetalhamentoData());
                boleto.addTextosExtras("txtRsHistoricoDespesaDescricaoS", historicoDespesaS.getDetalhamentoDescricao());
                boleto.addTextosExtras("txtRsHistoricoDespesaValorS", historicoDespesaS.getDetalhamentoValor());
                boleto.addTextosExtras("txtRsHistoricoDespesaNumeroNotaS", historicoDespesaS.getDetalhamentoNumeroNota());

                boleto.addTextosExtras("txtSacadoS", currentUser.getCcm().getNomeRazao() + " - " + currentUser.getCcm().getCnpjCpf());
                boleto.addTextosExtras("txtDocumentoS", dadosBoletoWs.getNumeroDocumento());
                boleto.addTextosExtras("txtDataDocumentoS", dataF.format(new Date()));
                boleto.addTextosExtras("txtCompetenciaS", dataF.format(notas.get(0).getDataEmissao()) + " a " + dataF.format(notas.get(notas.size() - 1).getDataEmissao()));
                boleto.addTextosExtras("txtNossoNumeroS", nossoNumero);
                //oleto.addTextosExtras("txtAgenciaS", ConfiguracaoSyst.numeroAgencia.toString() + "-" + ConfiguracaoSyst.digitoAgencia + "/" + ConfiguracaoSyst.numeroConta + "-" + ConfiguracaoSyst.digitoConta);

                escolhePDF = "BoletoPersonalizadoDetalhesDuasFolhas.pdf";

            } else {
                HistoricoNota historicoDespesa = new HistoricoNota();
                for (NFSE nota : notas) {
                    historicoDespesa.add(dataF.format(nota.getDataEmissao()), nota.getNomeRazaoTomador(), "R$ " + moeda.formata(nota.getValorBaseCalculo()), NotaUtil.fomrmataNumeroNota(nota.getNumeroNota()));
                }

                historicoDespesa.add("", "", "", "");
                historicoDespesa.add("", "", "TOTAL:  R$" + this.totalNota, "");

                boleto.addTextosExtras("txtRsHistoricoDespesaData", historicoDespesa.getDetalhamentoData());
                boleto.addTextosExtras("txtRsHistoricoDespesaDescricao", historicoDespesa.getDetalhamentoDescricao());
                boleto.addTextosExtras("txtRsHistoricoDespesaValor", historicoDespesa.getDetalhamentoValor());
                boleto.addTextosExtras("txtRsHistoricoDespesaNumeroNota", historicoDespesa.getDetalhamentoNumeroNota());

                escolhePDF = "BoletoPersonalizadoDetalhes.pdf";
            }
        }

        //boleto.setCodigoDeBarras(dadosBoletoWs.getCodBarras());
        //boleto.addTextosExtras("txtFcCodigoBarra", dadosBoletoWs.getCodBarras());

        /*
         * GERANDO O BOLETO BANCÁRIO.
         */
        // Instanciando um objeto "BoletoViewer", classe responsável pela
        // geração do boleto bancário.

        //Informando o template personalizado:


        FacesContext facesContext = FacesContext.getCurrentInstance();
        ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
        String arquivo = scontext.getRealPath("/PDF/" + escolhePDF);


        BoletoViewer boletoViewer = new BoletoViewer(boleto);
        boletoViewer.setTemplate(arquivo);

        //BoletoViewer boletoViewer = new BoletoViewer(boleto);

        // Gerando o arquivo. No caso o arquivo mencionado será salvo na mesma
        // pasta do projeto. Outros exemplos:
        // WINDOWS: boletoViewer.getAsPDF("C:/Temp/MeuBoleto.pdf");
        // LINUX: boletoViewer.getAsPDF("/home/temp/MeuBoleto.pdf");
        //File arquivoPdf = boletoViewer.getPdfAsFile("MeuPrimeiroBoleto.pdf");
        File arquivoPdf = boletoViewer.getPdfAsFile("BoletoPersonalizado.pdf");

        BoletoIssMap boletoIssMap = new BoletoIssMap();

        boletoIssMap.setFicha(ficha);
        boletoIssMap.setSacado(currentUser.getCcm());
        boletoIssMap.setValorDocumento(Moeda.convert(totalBoleto));
        boletoIssMap.setAliquota(Moeda.convertPorcentagem(aliquota));
        boletoIssMap.setCampoLivre(campoLivreStr);
        boletoIssMap.setDataInicioCompetencia(notas.get(0).getDataEmissao());
        boletoIssMap.setDataFimCompetencia(notas.get(notas.size() - 1).getDataEmissao());
        boletoIssMap.setDataDocumento(new Date());
        boletoIssMap.setMovimento(Moeda.convert(totalNota));
        boletoIssMap.setNossoNumero(nossoNumero);
        boletoIssMap.setNumeroDocumento(dadosBoletoWs.getNumeroDocumento());
        boletoIssMap.setNotasBoleto(notas);
        boletoIssMap.setBaixa(false);
        boletoIssMap.setLogoSituacao("desativado.png");


        try {
            boletoIssMap.setDataVencimenro(new Date(dataF.parse(dadosBoletoWs.getDataVencimento()).getTime()));
        } catch (ParseException ex) {
            Logger.getLogger(GeraBoleto.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            boletoIssMap = new DAOBoleto().salvarBoletoRetorno(boletoIssMap);

            if (boletoIssMap != null) {
                for (NFSE nfse : notas) {
                    nfse.setBoletoPagamento(boletoIssMap);
                    new DAONFSE().salvar(nfse);
                }
            } else {
                return;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return;
        }

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

    public void geraBoletoRetidoFonte() {
        try {
            filtraNotasRetidoFonte();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "Verifique a busca notas"));
            limpaBuscaRF();
            return;
        }
        
        if(listaNotasRetidoFonte.isEmpty()){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO BUSCAR NOTAS ", "Faça uma nova busca"));
            limpaBuscaRF();
            return;
        }else{
            if(listaNotasRetidoFonte.get(0).getBoletoPagamento() != null){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO BUSCAR NOTAS ", "Notas já vinculadas a guia de pagamento"));
                limpaBuscaRF();
                return;
            }
        }
        

        DateFormat dataF = new SimpleDateFormat("dd/MM/yyyy");

        /*
         * INFORMANDO DADOS SOBRE O CEDENTE.
         */
        Cedente cedente = new Cedente(ConfiguracaoSyst.NOME_CIDADE, ConfiguracaoSyst.CPFCNPJ_PREFEITURA);

        /*
         * INFORMANDO DADOS SOBRE O SACADO.
         */
        Sacado sacado = new Sacado(currentUser.getCcm().getNomeRazao(), currentUser.getCcm().getCnpjCpf());

        // Informando o endereço do sacado.
        Endereco enderecoSac = new Endereco();
        enderecoSac.setUF(UnidadeFederativa.SP);
        enderecoSac.setLocalidade(currentUser.getCcm().getLogradouro().getBairro().getCidade().getNome());
        enderecoSac.setCep(new CEP(currentUser.getCcm().getLogradouro().getCep()));

        if (currentUser.getCcm().getRua() == null) {
            enderecoSac.setLogradouro(currentUser.getCcm().getLogradouro().getNome());
            enderecoSac.setBairro(currentUser.getCcm().getLogradouro().getBairro().getNome());
        } else {
            enderecoSac.setLogradouro(currentUser.getCcm().getRua());
            enderecoSac.setBairro(currentUser.getCcm().getBairro());
        }
        enderecoSac.setNumero(currentUser.getCcm().getNumeroPredio().toString());
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

        String mes = dataF.format(new Date());
        String s[] = mes.split("/");
        mes = s[1];

        DadosBoletoWS dadosBoletoWs = new DadosBoletoWS();
        
        String dataSTRInicio;
        String dataSTRFim;
        
        if ((this.dataIniRF == null) && (this.dataFimRF == null)) {
            dataSTRInicio = "0";
            dataSTRFim = "0";
        }else{
            
            DateFormat datafI = new SimpleDateFormat("yyyy-MM-dd");

            dataSTRInicio = datafI.format(dataIniRF);
            dataSTRFim = datafI.format(dataFimRF);
        }

        dadosBoletoWs = new BuscaDadosBoletoGemmap().retornaMobiliario(ConfiguracaoSyst.anoEx(), ConfiguracaoSyst.CODIGO_CEDENTE, ConfiguracaoSyst.FICHA_RETIDO_FONTE, Moeda.convert(totalIssDevidoRetidoFonte).toString(), mes, currentUser.getCcm().getCodISSGemmap().toString(), dataSTRInicio, dataSTRFim, currentUser.getCcm().getCnpjCpf(),"rf", "0");

        System.out.println("Resultado dodosWS nossoNumero  >>>>>>" + dadosBoletoWs.getNossoNumero());

        // Informando dados sobre a conta bancária do título.
        ContaBancaria contaBancaria = new ContaBancaria(BancosSuportados.HSBC.create());
        contaBancaria.setNumeroDaConta(new NumeroDaConta(ConfiguracaoSyst.NUMERO_CONTA, ConfiguracaoSyst.DIGITO_CONTA));
        contaBancaria.setCarteira(new Carteira(ConfiguracaoSyst.CARTEIRA));
        contaBancaria.setAgencia(new Agencia(ConfiguracaoSyst.NUMERO_AGENCIA));

        Titulo titulo = new Titulo(contaBancaria, sacado, cedente);
        titulo.setNumeroDoDocumento(dadosBoletoWs.getNumeroDocumento());

        String nossoNumero = dadosBoletoWs.getNossoNumero();

        titulo.setNossoNumero(nossoNumero.substring(0, nossoNumero.length() - 3));
        titulo.setDigitoDoNossoNumero(nossoNumero.substring(nossoNumero.length() - 3));

        titulo.setValor(BigDecimal.valueOf(Moeda.convert(totalIssDevidoRetidoFonte)));
        titulo.setDataDoDocumento(new Date());
        try {
            titulo.setDataDoVencimento(new Date(dataF.parse(dadosBoletoWs.getDataVencimento()).getTime()));
        } catch (ParseException ex) {
            System.out.println("ERRO AO CONVERTER A DATA >>>>>" + ex.getMessage());
        }
        titulo.setTipoDeDocumento(TipoDeTitulo.RC_RECIBO);
        titulo.setAceite(EnumAceite.N);
        titulo.setDesconto(BigDecimal.ZERO);
        titulo.setDeducao(BigDecimal.ZERO);
        titulo.setMora(BigDecimal.ZERO);
        titulo.setAcrecimo(BigDecimal.ZERO);
        titulo.setValorCobrado(BigDecimal.ZERO);

        /*
         * INFORMANDO OS DADOS SOBRE O BOLETO.
         */

        String campoLivreStr = dadosBoletoWs.getCodBarras().substring(19);

        TesteCampoLivre compoLivre = new TesteCampoLivre();
        compoLivre.read(campoLivreStr);
        Boleto boleto = new Boleto(titulo, compoLivre);

        boleto.setLocalPagamento("PAGÁVEL EM QUALQUER BANCO ATÉ O VENCIMENTO.");
        boleto.setInstrucaoAoSacado("Finalidade: ISSQN RETIDO NA FONTE ");
        boleto.setInstrucao1("");
        boleto.setInstrucao2("Após o vencimanto aplicar sobre a parcela:");
        boleto.setInstrucao3("Multa de 2% (dois por cento) ao mês, até o limite de 10% (dez por cento),");
        boleto.setInstrucao4("acrescimentos de juros de mora de 1% (um por cento) ao mês, a partir do mês");
        boleto.setInstrucao5("imediato ao vencimento, sendo considerado mês completo qualquer fração deste.");
        boleto.setInstrucao8("Finalidade: ISSQN RETIDO NA FONTE ");


        boleto.addTextosExtras("txtFcCarteira", "SR.");
        boleto.addTextosExtras("txtFcEspecie", "R$");
        boleto.addTextosExtras("txtFcEspecieDocumento", "RC-CI");

        boleto.addTextosExtras("txtRsCompetencia", dataF.format(listaNotasRetidoFonte.get(listaNotasRetidoFonte.size() - 1).getDataEmissao()) + " a " + dataF.format(listaNotasRetidoFonte.get(0).getDataEmissao()));
        //boleto.addTextosExtras("txtRsAliquota", aliquota);
        boleto.addTextosExtras("txtRsMovimentoMensal", "R$ " + totalIssDevidoRetidoFonte);
        boleto.addTextosExtras("txtRsIM", currentUser.getCcm().getIm());

        boleto.addTextosExtras("txtFcCompetencia", dataF.format(listaNotasRetidoFonte.get(listaNotasRetidoFonte.size() - 1).getDataEmissao()) + " a " + dataF.format(listaNotasRetidoFonte.get(0).getDataEmissao()));
//        boleto.addTextosExtras("txtFcAliquota", aliquota);
        boleto.addTextosExtras("txtFcMovimentoMensal", "R$ " + totalIssDevidoRetidoFonte);

        boleto.addTextosExtras("txtAnoEx", ConfiguracaoSyst.anoEx());
        boleto.addTextosExtras("txtFicha", ConfiguracaoSyst.FICHA_RETIDO_FONTE);
        boleto.addTextosExtras("txtNro", dadosBoletoWs.getNumeroDocumento());

        boleto.addTextosExtras("txtFcDescontoAbatimento", "R$ ");
        boleto.addTextosExtras("txtFcOutraDeducao", "R$ ");
        boleto.addTextosExtras("txtFcMoraMulta", "R$ ");
        boleto.addTextosExtras("txtFcOutroAcrescimo", "R$ ");
        boleto.addTextosExtras("txtFcValorCobrado", "R$ ");

        if (listaNotasRetidoFonte.isEmpty()) {
            System.out.println("Lista vazia");
        } else {
            /*
             * Informando o historico de notas:
             */
            if (listaNotasRetidoFonte.size() > 18) {
                HistoricoNotaRrtidoFonte historicoDespesa = new HistoricoNotaRrtidoFonte();

                for (int i = 0; i <= 17; i++) {
                    historicoDespesa.add(dataF.format(listaNotasRetidoFonte.get(i).getDataEmissao()), listaNotasRetidoFonte.get(i).getNomeRazaoPrestador() + " / " + listaNotasRetidoFonte.get(i).getCpfCnpjPrestador(), "R$ " + moeda.formata(listaNotasRetidoFonte.get(i).getValorBaseCalculo()), NotaUtil.fomrmataNumeroNota(listaNotasRetidoFonte.get(i).getNumeroNota()), listaNotasRetidoFonte.get(i).getAliquota().toString(), listaNotasRetidoFonte.get(i).getCidadePrestador(), "R$ " + moeda.formata(listaNotasRetidoFonte.get(i).getValorIss()));
                }

                historicoDespesa.add("", "", "", "", "", "", "");
                historicoDespesa.add("", "CONTINUA CONFORME RELAÇÃO EM ANEXO...", "", "", "", "", "");

                boleto.addTextosExtras("txtRsHistoricoDespesaData", historicoDespesa.getDetalhamentoData());
                boleto.addTextosExtras("txtRsHistoricoDespesaDescricao", historicoDespesa.getDetalhamentoDescricao());
                boleto.addTextosExtras("txtRsHistoricoDespesaValor", historicoDespesa.getDetalhamentoValor());
                boleto.addTextosExtras("txtRsHistoricoDespesaNumeroNota", historicoDespesa.getDetalhamentoNumeroNota());
                boleto.addTextosExtras("txtRsHistoricoDespesaAliquota", historicoDespesa.getDetalhamentoAliquota());
                boleto.addTextosExtras("txtRsHistoricoDespesaCidade", historicoDespesa.getDetalhamentoCidade());
                boleto.addTextosExtras("txtRsHistoricoDespesaIss", historicoDespesa.getDetalhamentoValorIss());


                HistoricoNotaRrtidoFonte historicoDespesaS = new HistoricoNotaRrtidoFonte();

                for (int i = 18; i < notas.size(); i++) {
                    historicoDespesaS.add(dataF.format(listaNotasRetidoFonte.get(i).getDataEmissao()), listaNotasRetidoFonte.get(i).getNomeRazaoPrestador() + " / " + listaNotasRetidoFonte.get(i).getCpfCnpjPrestador(), "R$ " + moeda.formata(listaNotasRetidoFonte.get(i).getValorBaseCalculo()), NotaUtil.fomrmataNumeroNota(listaNotasRetidoFonte.get(i).getNumeroNota()), listaNotasRetidoFonte.get(i).getAliquota().toString(), listaNotasRetidoFonte.get(i).getCidadePrestador(), moeda.formata(listaNotasRetidoFonte.get(i).getValorIss()));
                }

                historicoDespesaS.add("", "", "", "", "", "", "");
                historicoDespesaS.add("", "", "", "", "", "", "TOTAL:  R$" + this.totalIssDevidoRetidoFonte);

                boleto.addTextosExtras("txtRsHistoricoDespesaDataS", historicoDespesaS.getDetalhamentoData());
                boleto.addTextosExtras("txtRsHistoricoDespesaDescricaoS", historicoDespesaS.getDetalhamentoDescricao());
                boleto.addTextosExtras("txtRsHistoricoDespesaValorS", historicoDespesaS.getDetalhamentoValor());
                boleto.addTextosExtras("txtRsHistoricoDespesaNumeroNotaS", historicoDespesaS.getDetalhamentoNumeroNota());
                boleto.addTextosExtras("txtRsHistoricoDespesaAliquotaS", historicoDespesaS.getDetalhamentoAliquota());
                boleto.addTextosExtras("txtRsHistoricoDespesaCidadeS", historicoDespesaS.getDetalhamentoCidade());
                boleto.addTextosExtras("txtRsHistoricoDespesaIssS", historicoDespesaS.getDetalhamentoValorIss());

                boleto.addTextosExtras("txtSacadoS", currentUser.getCcm().getNomeRazao() + " - " + currentUser.getCcm().getCnpjCpf());
                boleto.addTextosExtras("txtDocumentoS", dadosBoletoWs.getNumeroDocumento());
                boleto.addTextosExtras("txtDataDocumentoS", dataF.format(new Date()));
                boleto.addTextosExtras("txtCompetenciaS", dataF.format(listaNotasRetidoFonte.get(listaNotasRetidoFonte.size() - 1).getDataEmissao()) + " a " + dataF.format(listaNotasRetidoFonte.get(0).getDataEmissao()));
                boleto.addTextosExtras("txtNossoNumeroS", nossoNumero);
                //oleto.addTextosExtras("txtAgenciaS", ConfiguracaoSyst.numeroAgencia.toString() + "-" + ConfiguracaoSyst.digitoAgencia + "/" + ConfiguracaoSyst.numeroConta + "-" + ConfiguracaoSyst.digitoConta);

                escolhePDF = "BoletoPersonalizadoDetalhesDuasFolhas.pdf";

            } else {
                HistoricoNotaRrtidoFonte historicoDespesa = new HistoricoNotaRrtidoFonte();
                for (NFSE nota : listaNotasRetidoFonte) {
                    historicoDespesa.add(dataF.format(nota.getDataEmissao()), nota.getNomeRazaoPrestador() + " / " + nota.getCpfCnpjPrestador(), "R$ " + moeda.formata(nota.getValorBaseCalculo()), NotaUtil.fomrmataNumeroNota(nota.getNumeroNota()), nota.getAliquota().toString(), nota.getCidadePrestador(), "R$ " + moeda.formata(nota.getValorIss()));
                }

                historicoDespesa.add("", "", "", "", "", "", "");
                historicoDespesa.add("", "", "", "", "", "", "TOTAL:  R$" + this.totalIssDevidoRetidoFonte);

                boleto.addTextosExtras("txtRsHistoricoDespesaData", historicoDespesa.getDetalhamentoData());
                boleto.addTextosExtras("txtRsHistoricoDespesaDescricao", historicoDespesa.getDetalhamentoDescricao());
                boleto.addTextosExtras("txtRsHistoricoDespesaValor", historicoDespesa.getDetalhamentoValor());
                boleto.addTextosExtras("txtRsHistoricoDespesaNumeroNota", historicoDespesa.getDetalhamentoNumeroNota());
                boleto.addTextosExtras("txtRsHistoricoDespesaAliquota", historicoDespesa.getDetalhamentoAliquota());
                boleto.addTextosExtras("txtRsHistoricoDespesaCidade", historicoDespesa.getDetalhamentoCidade());
                boleto.addTextosExtras("txtRsHistoricoDespesaIss", historicoDespesa.getDetalhamentoValorIss());

                escolhePDF = "BoletoPersonalizadoDetalhesRF.pdf";
            }
        }

        //boleto.setCodigoDeBarras(dadosBoletoWs.getCodBarras());
        //boleto.addTextosExtras("txtFcCodigoBarra", dadosBoletoWs.getCodBarras());

        /*
         * GERANDO O BOLETO BANCÁRIO.
         */
        // Instanciando um objeto "BoletoViewer", classe responsável pela
        // geração do boleto bancário.

        //Informando o template personalizado:


        FacesContext facesContext = FacesContext.getCurrentInstance();
        ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
        String arquivo = scontext.getRealPath("/PDF/" + escolhePDF);


        BoletoViewer boletoViewer = new BoletoViewer(boleto);
        boletoViewer.setTemplate(arquivo);

        //BoletoViewer boletoViewer = new BoletoViewer(boleto);

        // Gerando o arquivo. No caso o arquivo mencionado será salvo na mesma
        // pasta do projeto. Outros exemplos:
        // WINDOWS: boletoViewer.getAsPDF("C:/Temp/MeuBoleto.pdf");
        // LINUX: boletoViewer.getAsPDF("/home/temp/MeuBoleto.pdf");
        //File arquivoPdf = boletoViewer.getPdfAsFile("MeuPrimeiroBoleto.pdf");
        //File arquivoPdf = boletoViewer.getPdfAsFile("BoletoPersonalizado.pdf");

        BoletoIssMap boletoIssMap = new BoletoIssMap();

        boletoIssMap.setFicha(ConfiguracaoSyst.FICHA_RETIDO_FONTE);
        boletoIssMap.setSacado(currentUser.getCcm());
        boletoIssMap.setValorDocumento(Moeda.convert(totalIssDevidoRetidoFonte));
        boletoIssMap.setAliquota(Moeda.convertPorcentagem(aliquota));
        boletoIssMap.setCampoLivre(campoLivreStr);
        boletoIssMap.setDataInicioCompetencia(listaNotasRetidoFonte.get(listaNotasRetidoFonte.size() - 1).getDataEmissao());
        boletoIssMap.setDataFimCompetencia(listaNotasRetidoFonte.get(0).getDataEmissao());
        boletoIssMap.setDataDocumento(new Date());
        boletoIssMap.setMovimento(Moeda.convert(totalIssDevidoRetidoFonte));
        boletoIssMap.setNossoNumero(nossoNumero);
        boletoIssMap.setNumeroDocumento(dadosBoletoWs.getNumeroDocumento());
        boletoIssMap.setNotasBoleto(listaNotasRetidoFonte);
        boletoIssMap.setBaixa(false);
        boletoIssMap.setLogoSituacao("desativado.png");


        try {
            boletoIssMap.setDataVencimenro(new Date(dataF.parse(dadosBoletoWs.getDataVencimento()).getTime()));
        } catch (ParseException ex) {
            Logger.getLogger(GeraBoleto.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            boletoIssMap = new DAOBoleto().salvarBoletoRetorno(boletoIssMap);

            if (boletoIssMap != null) {
                for (NFSE nfse : listaNotasRetidoFonte) {
                    nfse.setBoletoPagamento(boletoIssMap);
                    new DAONFSE().salvar(nfse);
                }
            } else {
                return;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return;
        }

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

    public void buscaBoletos() {

        if ((this.dataIniBolotoGerado == null) && (this.dataFimBolotoGerado == null) && numeroDocumento.equals("")) {
            buscaBoletosEmAbertos();
            return;
        }

        if (!this.numeroDocumento.equals("")) {

            listaBoletos = new DAOBoleto().retornaBoletosPorDocumento(numeroDocumento);
            if (listaBoletos.isEmpty()) {
                limpaBuscaBoleto();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Nenhum boleto foi encontrado", ""));
            }

        } else {
            if ((this.dataIniBolotoGerado != null) && (this.dataFimBolotoGerado != null)) {

                if (dataIniBolotoGerado.after(dataFimBolotoGerado)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "Datas Incorretas"));
                    return;
                }
                listaBoletos = new DAOBoleto().retornaBoletos(dataIniBolotoGerado, dataFimBolotoGerado);


                if (listaBoletos.isEmpty()) {
                    limpaBuscaBoleto();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Nenhum boleto foi encontrado", ""));
                }

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! Informe o periodo de busca", ""));
            }
        }
    }

    public void buscaBoletosRF() {

        if (this.numeroDocumentoRF.equals("") && (this.dataIniBolotoGeradoRF == null) && (this.dataFimBolotoGeradoRF == null)) {
            buscaBoletosEmAbertosRetidoFonte();
            return;
        }

        if (!this.numeroDocumentoRF.equals("")) {

            listaBoletosRetidoFonte = new DAOBoleto().retornaBoletosPorDocumento(numeroDocumentoRF);
            if (listaBoletosRetidoFonte.isEmpty()) {
                limpaBuscaBoleto();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Nenhum boleto foi encontrado", ""));
            }

        } else {
            if ((this.dataIniBolotoGeradoRF != null) && (this.dataFimBolotoGeradoRF != null)) {

                if (dataIniBolotoGeradoRF.after(dataFimBolotoGeradoRF)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "Datas Incorretas"));
                    return;
                }
                listaBoletosRetidoFonte = new DAOBoleto().retornaBoletosRF(dataIniBolotoGeradoRF, dataFimBolotoGeradoRF);


                if (listaBoletosRetidoFonte.isEmpty()) {
                    limpaBuscaBoleto();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Nenhum boleto foi encontrado", ""));
                }

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! Informe o periodo de busca", ""));
            }
        }
    }

    public String getAliquota() {
        return aliquota;
    }

    public void setAliquota(String aliquota) {
        this.aliquota = aliquota;
    }

    public String getTotalBoleto() {
        return totalBoleto;
    }

    public void setTotalBoleto(String totalBoleto) {
        this.totalBoleto = totalBoleto;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Date getDataIni() {
        return dataIni;
    }

    public void setDataIni(Date dataIni) {
        this.dataIni = dataIni;
    }

    public List<NFSE> getNotas() {
        return notas;
    }

    public void setNotas(List<NFSE> notas) {
        this.notas = notas;
    }

    public String getQuantidadeNota() {
        return quantidadeNota;
    }

    public void setQuantidadeNota(String quantidadeNota) {
        this.quantidadeNota = quantidadeNota;
    }

    public String getTotalNota() {
        return totalNota;
    }

    public void setTotalNota(String totalNota) {
        this.totalNota = totalNota;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Boolean getMostraDados() {
        return mostraDados;
    }

    public void setMostraDados(Boolean mostraDados) {
        this.mostraDados = mostraDados;
    }

    public List<BoletoIssMap> getListaBoletos() {
        return listaBoletos;
    }

    public void setListaBoletos(List<BoletoIssMap> listaBoletos) {
        this.listaBoletos = listaBoletos;
    }

    public Date getDataFimBolotoGerado() {
        return dataFimBolotoGerado;
    }

    public void setDataFimBolotoGerado(Date dataFimBolotoGerado) {
        this.dataFimBolotoGerado = dataFimBolotoGerado;
    }

    public Date getDataIniBolotoGerado() {
        return dataIniBolotoGerado;
    }

    public void setDataIniBolotoGerado(Date dataIniBolotoGerado) {
        this.dataIniBolotoGerado = dataIniBolotoGerado;
    }

    public BoletoIssMap getBoletoSelecionado() {
        return boletoSelecionado;
    }

    public void setBoletoSelecionado(BoletoIssMap boletoSelecionado) {
        this.boletoSelecionado = boletoSelecionado;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public Boolean getGeraBoleto() {
        return geraBoleto;
    }

    public void setGeraBoleto(Boolean geraBoleto) {
        this.geraBoleto = geraBoleto;
    }

    public Boolean getBotaoSegundaVia() {
        return botaoSegundaVia;
    }

    public void setBotaoSegundaVia(Boolean botaoSegundaVia) {
        this.botaoSegundaVia = botaoSegundaVia;
    }

    public Boolean getTabelaSNF() {
        return tabelaSNF;
    }

    public void setTabelaSNF(Boolean tabelaSNF) {
        this.tabelaSNF = tabelaSNF;
    }

    public Boolean getTabelaBoletos() {
        return tabelaBoletos;
    }

    public void setTabelaBoletos(Boolean tabelaBoletos) {
        this.tabelaBoletos = tabelaBoletos;
    }

    public String getMesSelecionado() {
        return mesSelecionado;
    }

    public void setMesSelecionado(String mesSelecionado) {
        this.mesSelecionado = mesSelecionado;
    }

    public List<BoletosSF> getBoletosSNF() {
        return boletosSNF;
    }

    public void setBoletosSNF(List<BoletosSF> boletosSNF) {
        this.boletosSNF = boletosSNF;
    }

    public BoletosSF getFixoSelecionado() {
        return fixoSelecionado;
    }

    public void setFixoSelecionado(BoletosSF fixoSelecionado) {
        this.fixoSelecionado = fixoSelecionado;
    }

    public String getTextoColunaCommtParcela() {
        return textoColunaCommtParcela;
    }

    public void setTextoColunaCommtParcela(String textoColunaCommtParcela) {
        this.textoColunaCommtParcela = textoColunaCommtParcela;
    }

    public Boolean getBuscaSimples() {
        return buscaSimples;
    }

    public void setBuscaSimples(Boolean buscaSimples) {
        this.buscaSimples = buscaSimples;
    }

    public String getTituloPainelBoletos() {
        return tituloPainelBoletos;
    }

    public void setTituloPainelBoletos(String tituloPainelBoletos) {
        this.tituloPainelBoletos = tituloPainelBoletos;
    }

    public List<NFSE> getListaNotasRetidoFonte() {
        return listaNotasRetidoFonte;
    }

    public void setListaNotasRetidoFonte(List<NFSE> listaNotasRetidoFonte) {
        this.listaNotasRetidoFonte = listaNotasRetidoFonte;
    }

    public List<BoletoIssMap> getListaBoletosRetidoFonte() {
        return listaBoletosRetidoFonte;
    }

    public void setListaBoletosRetidoFonte(List<BoletoIssMap> listaBoletosRetidoFonte) {
        this.listaBoletosRetidoFonte = listaBoletosRetidoFonte;
    }

    public String getQuantidadeNotaRetidoFonte() {
        return quantidadeNotaRetidoFonte;
    }

    public void setQuantidadeNotaRetidoFonte(String quantidadeNotaRetidoFonte) {
        this.quantidadeNotaRetidoFonte = quantidadeNotaRetidoFonte;
    }

    public String getTotalBoletoRetidoFonte() {
        return totalBoletoRetidoFonte;
    }

    public void setTotalBoletoRetidoFonte(String totalBoletoRetidoFonte) {
        this.totalBoletoRetidoFonte = totalBoletoRetidoFonte;
    }

    public String getTotalIssDevidoRetidoFonte() {
        return totalIssDevidoRetidoFonte;
    }

    public void setTotalIssDevidoRetidoFonte(String totalIssDevidoRetidoFonte) {
        this.totalIssDevidoRetidoFonte = totalIssDevidoRetidoFonte;
    }

    public String getTotalNotaRetidoFonte() {
        return totalNotaRetidoFonte;
    }

    public void setTotalNotaRetidoFonte(String totalNotaRetidoFonte) {
        this.totalNotaRetidoFonte = totalNotaRetidoFonte;
    }

    public Boolean getMostraDadosGeraBoletoRetidoFonte() {
        return mostraDadosGeraBoletoRetidoFonte;
    }

    public void setMostraDadosGeraBoletoRetidoFonte(Boolean mostraDadosGeraBoletoRetidoFonte) {
        this.mostraDadosGeraBoletoRetidoFonte = mostraDadosGeraBoletoRetidoFonte;
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

    public Date getDataFimBolotoGeradoRF() {
        return dataFimBolotoGeradoRF;
    }

    public void setDataFimBolotoGeradoRF(Date dataFimBolotoGeradoRF) {
        this.dataFimBolotoGeradoRF = dataFimBolotoGeradoRF;
    }

    public Date getDataIniBolotoGeradoRF() {
        return dataIniBolotoGeradoRF;
    }

    public void setDataIniBolotoGeradoRF(Date dataIniBolotoGeradoRF) {
        this.dataIniBolotoGeradoRF = dataIniBolotoGeradoRF;
    }

    public String getNumeroDocumentoRF() {
        return numeroDocumentoRF;
    }

    public void setNumeroDocumentoRF(String numeroDocumentoRF) {
        this.numeroDocumentoRF = numeroDocumentoRF;
    }

    public Date getDataHoje() {
        return dataHoje;
    }

    public void setDataHoje(Date dataHoje) {
        this.dataHoje = dataHoje;
    }

    public Date getDataMinimaBuscaRF() {
        return dataMinimaBuscaRF;
    }

    public void setDataMinimaBuscaRF(Date dataMinimaBuscaRF) {
        this.dataMinimaBuscaRF = dataMinimaBuscaRF;
    }

    public NFSE getNotaSelecionada() {
        return notaSelecionada;
    }

    public void setNotaSelecionada(NFSE notaSelecionada) {
        this.notaSelecionada = notaSelecionada;
    }

    public Boolean getGuiasGeradasRendered() {
        return guiasGeradasRendered;
    }

    public void setGuiasGeradasRendered(Boolean guiasGeradasRendered) {
        this.guiasGeradasRendered = guiasGeradasRendered;
    }
}
