/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.backingbean;

import br.com.nfse.dao.DAOAguardaEnvio;
import br.com.nfse.dao.DAOCcm;
import br.com.nfse.dao.DAOCidadeIBGE;
import br.com.nfse.dao.DAOLogradouro;
import br.com.nfse.dao.DAONFSE;
import br.com.nfse.jsfuntil.CnpjValidator;
import br.com.nfse.jsfuntil.CodigoVerificaNota;
import br.com.nfse.jsfuntil.CpfValidator;
import br.com.nfse.jsfuntil.Moeda;
import br.com.nfse.jsfuntil.NotaUtil;
import br.com.nfse.to.AguardandoEnvio;
import br.com.nfse.to.Ccm;
import br.com.nfse.to.IBGECidade;
import br.com.nfse.to.Logradouro;
import br.com.nfse.to.NFSE;
import br.com.nfse.to.Servico;
import br.com.nfse.to.Usuario;
import br.com.nfse.to.relatorio.NfseRelatorio;
import br.com.nfse.ws.beans.RespNotaEnvio;
import br.com.nfse.ws.cliente.SolicitaBuscaNFSE;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
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
import org.primefaces.context.RequestContext;

/**
 *
 * @author Thiago
 */
@ManagedBean(name = "MBSubstitui")
@ViewScoped
public class SubstituiNfseBaking {

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
    private String cnpjCpf;
    private Boolean exNfse;
    private Boolean botaoCadastraTomador;
    private IBGECidade cidadeIbgeSelecionado = new IBGECidade();
    private String nomeCidadeExecucao = "Santa Cruz do Rio Pardo";
    private String cpfCnpjTomadorCadastra;
    private String nomeTomador;
    private String ieRgTomador;
    private String enderecoTomador;
    private String numeroTomador;
    private String complementoTomador;
    private String bairroTomador;
    private String foneFaxTomador;
    private String cidadeTomador;
    private String estadoTomador;
    private String cepTomador;
    private String emailTomador;
    private String inscricaoMunicipalTomador;
    private String servicoSelecionado;
    private String buscaCidadeSubs;
    Moeda moeda = new Moeda();
    private String valorDeducoesSubs = "0,00";
    private String valorBaseCalculoSubs = "0,00";
    private String valorIssSubs = "0,00";
    private String valorCreditoSubs = "0,00";
    private String valorNotaSubs = "0,00";
    private Double valorIssPrefeituaraSubs = 0.0;
    private Servico servicoResultado = null;
    private List<Servico> listaServicos = new ArrayList<Servico>();
    private String valorAliquotaSubs = "0";
    private List<IBGECidade> listaCidadesIbge = new ArrayList<IBGECidade>();
    private Ccm tomador;
    private String retencaoFonteSubs;
    private String tipoMovimentoServicoSubs;
    private String cobrancaSub;
    private String tipoTributacaoSub;
    private NFSE nota = null;
    DateFormat dataCompleta = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat horaf = new SimpleDateFormat("HH:mm:ss");

    public SubstituiNfseBaking() {

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();
        this.currentUser = (Usuario) session.getAttribute("usuario");

        if (currentUser.getCcm().getCodigoEventualGemmap() == null) {
            this.listaServicos = this.currentUser.getCcm().getListaServicosPrestados();

            this.servicoResultado = buscaServicoLista(this.listaServicos.get(0).getId());
            this.valorAliquotaSubs = this.servicoResultado.getAliquotaJ().toString();

            this.tipoMovimentoServicoSubs = this.servicoResultado.getTipoMovimento();

            if (this.servicoResultado.isRetencaoFonte()) {
                this.retencaoFonteSubs = "SIM";
            } else {
                this.retencaoFonteSubs = "NÃO";
            }
            this.servicoPrestado = this.servicoResultado.getItemTabela();

            if (currentUser.getCcm().isCobraca()) {
                cobrancaSub = "Tributável";
            } else {
                cobrancaSub = "Isento";
            }
            if (currentUser.getCcm().isAutonomo()) {
                tipoTributacaoSub = "Autônomo";
            } else if (currentUser.getCcm().isMei()) {
                tipoTributacaoSub = "Empreendedor Individual";
            } else if (currentUser.getCcm().isProfissionalLiberal()) {
                tipoTributacaoSub = "Profissional Liberal";
            } else if (currentUser.getCcm().isSimples()) {
                tipoTributacaoSub = "Simples Nacional";
            } else {
                tipoTributacaoSub = "Normal";
            }
        }else{
            servicoResultado = new Servico();
            tipoMovimentoServicoSubs = "";
        }
        //this.template = AutenticaUsuario.verificaTemplate(this.currentUser);

    }

    private Servico buscaServicoLista(Integer id) {
        Servico servicoRet = null;

        for (Servico servico : listaServicos) {
            if (servico.getId() == id) {
                servicoRet = servico;
                break;
            }
        }
        return servicoRet;
    }

    public void buscaTomador() {

        DAOCcm ccmDao = new DAOCcm();

        cpfCnpjTomadorCadastra = cpfCnpjTomadorCadastra.replaceAll("\\/", "").replaceAll("\\.", "").replaceAll("-", "");

        if (this.cpfCnpjTomadorCadastra.length() == 14) {
            if (!CnpjValidator.validaCNPJ(this.cpfCnpjTomadorCadastra)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "CNPJ/CPF Incorreto", ""));
                this.exNfse = false;
                limpaCampoTomador();
                return;
            }
        } else if (this.cpfCnpjTomadorCadastra.length() == 11) {
            if (!CpfValidator.validaCPF(this.cpfCnpjTomadorCadastra)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "CNPJ/CPF Incorreto", ""));
                this.exNfse = false;
                limpaCampoTomador();
                return;
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "CNPJ/CPF Incorreto", ""));
            this.exNfse = false;
            limpaCampoTomador();
            return;
        }
        try {
            this.tomador = ccmDao.buscaPorCnpjCpf(cpfCnpjTomadorCadastra);

            if (tomador != null) {
                this.botaoCadastraTomador = true;
                this.exNfse = true;
                this.nomeTomador = tomador.getNomeRazao();
                this.ieRgTomador = tomador.getIeRg();
                this.inscricaoMunicipalTomador = tomador.getIm();
                this.cepTomador = tomador.getLogradouro().getCep();

                this.numeroTomador = tomador.getNumeroPredio().toString();
                this.complementoTomador = tomador.getComplemento();
                this.cidadeTomador = tomador.getLogradouro().getBairro().getCidade().getNome();
                this.estadoTomador = tomador.getLogradouro().getBairro().getCidade().getEstado().getSigla();
                this.foneFaxTomador = tomador.getTelefone();
                this.emailTomador = tomador.getEmail();

                String rua = tomador.getRua();

                if (rua == null) {
                    this.enderecoTomador = tomador.getLogradouro().getNome();
                    this.bairroTomador = tomador.getLogradouro().getBairro().getNome();
                } else {
                    this.enderecoTomador = tomador.getRua();
                    this.bairroTomador = tomador.getBairro();
                }
            } else {
                this.botaoCadastraTomador = false;
                this.exNfse = true;
                limpaCampoTomador();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "CNPJ/CPF Não Cadastrado.", "Preencha os dados e efetue o cadastro para emissão da Nota Fiscal Eletronica de Serviço"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao buscar tomador", e.getMessage()));
        }

    }

    public void cadastraTomador() {
        tomador = new Ccm();

        tomador.setCnpjCpf(this.cpfCnpjTomadorCadastra.replaceAll("\\/", "").replaceAll("\\.", "").replaceAll("-", ""));

        if (this.cpfCnpjTomadorCadastra.length() == 11) {
            tomador.setTipoPessoa(2);
        } else if (this.cpfCnpjTomadorCadastra.length() == 14) {
            tomador.setTipoPessoa(1);
        }

        if (!this.nomeTomador.equals("")) {
            tomador.setNomeRazao(this.nomeTomador);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro campos obrigatorios NOME", ""));
            return;
        }

        if (!this.ieRgTomador.equals("")) {
            tomador.setIeRg(this.ieRgTomador.replaceAll("\\.", "").replaceAll("\\-", ""));
        }
        if (!this.inscricaoMunicipalTomador.equals("")) {
            tomador.setIm(this.inscricaoMunicipalTomador.replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("\\/", ""));
        }

        if (!this.cepTomador.equals("")) {
            DAOLogradouro daoLogradouro = new DAOLogradouro();
            Logradouro logra;
            try {
                logra = daoLogradouro.buscaPorNomeCep(this.cepTomador.replace("-", ""));

                tomador.setLogradouro(logra);

                if (logra.getNome().equals("")) {
                    if (!this.enderecoTomador.equals("")) {
                        tomador.setRua(this.enderecoTomador);
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro campos obrigatorios ENDEREÇO", ""));
                        return;
                    }
                    if (!this.bairroTomador.equals("")) {
                        tomador.setBairro(this.bairroTomador);
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro campos obrigatorios BAIRRO", ""));
                        return;
                    }
                }

            } catch (Exception ex) {
                Logger.getLogger(NfseLancaBaking.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro campos obrigatorios CEP", ""));
            return;
        }

        if (!this.numeroTomador.equals("")) {
            tomador.setNumeroPredio(numeroTomador);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro campos obrigatorios NUMERO", ""));
            return;
        }

        if (!this.complementoTomador.equals("")) {
            tomador.setComplemento(this.complementoTomador);
        }

        if (!this.foneFaxTomador.equals("")) {
            tomador.setTelefone(this.foneFaxTomador.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("-", "").replaceAll(" ", ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Campos obrigatórios FONE/FAX", ""));
            return;
        }

        if (!this.emailTomador.equals("")) {

            Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
            Matcher m = p.matcher(this.emailTomador);

            boolean matchFound = m.matches();

            if (!matchFound) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "E-mail Incorreto"));
                return;
            } else {
                tomador.setEmail(this.emailTomador);
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Campos obrigatórios e-mail", ""));
            return;
        }
        tomador.setTipoTipoContribuinte("T");
        DAOCcm ccmDao = new DAOCcm();
        try {
            ccmDao.salvar(tomador);
            this.botaoCadastraTomador = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cadastro de Tomador Efetuado com Sucesso", ""));
        } catch (SQLException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Errp ao Inserir tomador " + ex.getMessage(), ""));
        }

        //System.out.println("Tudo certo pronto para gravarr!!");
    }

    public void buscaCep() {

        DAOLogradouro daoLogradouro = new DAOLogradouro();
        try {
            Logradouro logradouro = daoLogradouro.buscaPorNomeCep(this.cepTomador.replace("-", ""));

            this.enderecoTomador = logradouro.getNome();
            this.bairroTomador = logradouro.getBairro().getNome();
            this.cidadeTomador = logradouro.getBairro().getCidade().getNome();
            this.estadoTomador = logradouro.getBairro().getCidade().getEstado().getSigla();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "CEP INVÁLIDO"));
        }
    }

    private void limpaCampoTomador() {

        this.nomeTomador = "";
        this.ieRgTomador = "";
        this.inscricaoMunicipalTomador = "";
        this.cepTomador = "";

        this.numeroTomador = "";
        this.complementoTomador = "";
        this.cidadeTomador = "";
        this.estadoTomador = "";
        this.foneFaxTomador = "";
        this.emailTomador = "";

        this.enderecoTomador = "";
        this.bairroTomador = "";

    }

    public void buscaNotaFical() {

        DAONFSE daoNota = new DAONFSE();
        nota = new NFSE();

        try {

            nota = daoNota.buscaNotaTomador(cpfCnpjTomador, codigoVerificacao, currentUser.getCcm().getCnpjCpf());
            if (nota == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO!", "Nenhum Registro Encontrado"));

            } else if (nota.isSubstituida()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO!", "Esta nota fiscal já esta substituida"));

            } else if (nota.isCancelada()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO!", "Esta nota fiscal esta cancelada"));
            } else {

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
                endTomador = nota.getEnderecoTomador() + ", " + nota.getCidadeTomador() + " - " + nota.getEstadoTomador();
                numeroNota = NotaUtil.fomrmataNumeroNota(nota.getNumeroNota());
                dataHora = dataCompleta.format(nota.getDataEmissao()) + " " + nota.getHora();
                exibeCarta = true;
                exibeAviso = false;

            }
        } catch (Exception ex) {
            Logger.getLogger(CartaCorrecaoBaking.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void alteraAliquota() {

        Servico servicoSelecao = new Servico();
        for (Servico serv : this.listaServicos) {
            if (serv.getId().toString().equals(this.servicoSelecionado)) {
                servicoSelecao = serv;
                this.valorAliquotaSubs = servicoSelecao.getAliquotaJ().toString();
                this.servicoPrestado = servicoSelecao.getItemTabela();
                if (servicoSelecao.isRetencaoFonte()) {
                    this.retencaoFonteSubs = "SIM";
                } else {
                    this.retencaoFonteSubs = "NÃO";
                }
                this.tipoMovimentoServicoSubs = servicoSelecao.getTipoMovimento();
                break;
            }
        }
        calculaISS();
    }

    public void calculaBase() {

        Double valorNotaF = 0.0;
        valorNotaF = Moeda.convert(valorNotaSubs);

        Double valorDeducoesF = 0.0;
        valorDeducoesF = Moeda.convert(valorDeducoesSubs);

        Double valorBaseF = 0.0;
        valorBaseF = Moeda.convert(valorBaseCalculoSubs);

        valorBaseF = valorNotaF - valorDeducoesF;
        this.valorBaseCalculoSubs = moeda.formata(valorBaseF);
        this.valorNotaSubs = moeda.formata(valorNotaF);
        //System.out.println("Valor da nota >>>>>>>>>>>>>>>>>> " + this.valorNotaSubs);
        this.valorDeducoesSubs = moeda.formata(valorDeducoesF);

        calculaISS();

    }

    public void calculaISS() {

        Double valorBaseF = 0.0;
        valorBaseF = Moeda.convert(valorBaseCalculoSubs);

        Double porcentagem = 0.0;
        porcentagem = Moeda.convertPorcentagem(valorAliquotaSubs);
        Double valorIssF = 0.0;
        valorIssF = valorBaseF * porcentagem;
        this.valorIssPrefeituaraSubs = valorIssF;
        valorIssSubs = moeda.formata(valorIssF);


        if (currentUser.getCcm().isSimples() || currentUser.getCcm().isMei()) {
            this.valorIssSubs = moeda.formata(0.0);
            return;
        }
        if (!this.tipoMovimentoServicoSubs.equals("VARIAVEL")) {
            this.valorIssSubs = moeda.formata(0.0);
            return;
        }

        calculaCredito();
    }

    public void lancaNotaSubstitutiva() throws IOException, SQLException, ClassNotFoundException {


        if (descServico.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "Informe a descrição do serviço prestado"));
            return;
        }

        if (valorNotaSubs.equals("0,00")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "Informe o valor da nota"));
            return;
        }


        DateFormat dataCompleta = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat diaf = new SimpleDateFormat("d");
        DateFormat mesf = new SimpleDateFormat("MM");
        DateFormat anof = new SimpleDateFormat("yyyy");
        DateFormat horaf = new SimpleDateFormat("HH");
        DateFormat minutof = new SimpleDateFormat("mm");
        DateFormat segundof = new SimpleDateFormat("ss");
        Date datas = Calendar.getInstance(Locale.getDefault()).getTime();
        Long numeroNotaSubistituta = NotaUtil.proximoNumeroNota(currentUser.getCcm().getCnpjCpf());


        String dataRelatorio = diaf.format(datas) + "/" + mesf.format(datas) + "/" + anof.format(datas) + " " + horaf.format(datas) + ":" + minutof.format(datas) + ":" + segundof.format(datas);
        String codigoVerificacao = currentUser.getCcm().getId().toString() + horaf.format(datas) + minutof.format(datas) + segundof.format(datas);
        codigoVerificacao = CodigoVerificaNota.encode(Long.parseLong(codigoVerificacao));
        codigoVerificacao = codigoVerificacao.toUpperCase();
        Ccm ccm = currentUser.getCcm();

        String horaLancamrnto = horaf.format(datas) + ":" + minutof.format(datas) + ":" + segundof.format(datas);

        NFSE notaFiscal = new NFSE();
        NfseRelatorio notaFiscalRelatorio = new NfseRelatorio();

        //itens relatório
        notaFiscalRelatorio.setCogigoVerifica(codigoVerificacao);
        notaFiscalRelatorio.setDataEmissao(dataRelatorio);
        notaFiscalRelatorio.setNumeroNota(NotaUtil.fomrmataNumeroNota(numeroNotaSubistituta));
        notaFiscalRelatorio.setCidadePrestador(ccm.getLogradouro().getBairro().getCidade().getNome());
        notaFiscalRelatorio.setNomeRazaoPrestador(ccm.getNomeRazao());
        notaFiscalRelatorio.setEnderecoPrestador(ccm.getLogradouro().getNome() + " " + ccm.getNumeroPredio());
        notaFiscalRelatorio.setImRgPrestador(ccm.getIm());
        notaFiscalRelatorio.setEstadoPrestador(ccm.getLogradouro().getBairro().getCidade().getEstado().getSigla());
        notaFiscalRelatorio.setCpfCnpjPrestador(ccm.getCnpjCpf());


        // itens nota
        notaFiscal.setCogigoVerifica(codigoVerificacao);
        notaFiscal.setHora(horaLancamrnto);

        DateFormat dataf = new SimpleDateFormat("dd/MM/yyyy");
        Date data = Calendar.getInstance(Locale.getDefault()).getTime();
        String dataStr = dataf.format(data);

        try {
            Date dateGrava = new Date(dataf.parse(dataStr).getTime());
            //System.out.println(dateGrava);
            notaFiscal.setDataEmissao(dateGrava);
            notaFiscal.setDataVencimento(dateGrava);
        } catch (java.text.ParseException ex) {
            Logger.getLogger(NfseLancaBaking.class.getName()).log(Level.SEVERE, null, ex);
        }
        notaFiscal.setNumeroNota(numeroNotaSubistituta);
        notaFiscal.setCidadePrestador(ccm.getLogradouro().getBairro().getCidade().getNome());
        notaFiscal.setNomeRazaoPrestador(ccm.getNomeRazao());
        notaFiscal.setEnderecoPrestador(ccm.getLogradouro().getNome() + " " + ccm.getNumeroPredio());
        notaFiscal.setImPrestador(ccm.getIm());
        notaFiscal.setEstadoPrestador(ccm.getLogradouro().getBairro().getCidade().getEstado().getSigla());
        notaFiscal.setCpfCnpjPrestador(ccm.getCnpjCpf());
        notaFiscal.setIeRgPrestador(ccm.getIeRg());

        if (tomador != null) {
            //itens Relatório
            notaFiscalRelatorio.setCidadeTomador(this.tomador.getLogradouro().getBairro().getCidade().getNome());
            notaFiscalRelatorio.setNomeRazaoTomador(this.tomador.getNomeRazao());
            notaFiscalRelatorio.setEnderecoTomador(this.tomador.getLogradouro().getNome() + " " + this.tomador.getNumeroPredio());
            notaFiscalRelatorio.setImRgTomador(this.tomador.getIm());
            notaFiscalRelatorio.setEstadoTomador(this.tomador.getLogradouro().getBairro().getCidade().getEstado().getSigla());
            notaFiscalRelatorio.setCpfCnpjTomador(this.tomador.getCnpjCpf());
            notaFiscalRelatorio.setEmailTomador(this.tomador.getEmail());

            //itens nota
            notaFiscal.setCidadeTomador(this.tomador.getLogradouro().getBairro().getCidade().getNome());
            notaFiscal.setNomeRazaoTomador(this.tomador.getNomeRazao());
            notaFiscal.setEnderecoTomador(this.tomador.getLogradouro().getNome() + " " + this.tomador.getNumeroPredio());
            notaFiscal.setImTomador(this.tomador.getIm());
            notaFiscal.setIeRgTomador(this.tomador.getIeRg());
            notaFiscal.setEstadoTomador(this.tomador.getLogradouro().getBairro().getCidade().getEstado().getSigla());
            notaFiscal.setCpfCnpjTomador(this.tomador.getCnpjCpf());
            notaFiscal.setEmailTomador(this.tomador.getEmail());


        }
        // dados relátorio
        notaFiscalRelatorio.setDescServico(descServico);
        notaFiscalRelatorio.setServicoPrestado(this.servicoPrestado);
        notaFiscalRelatorio.setValorDeducoes(this.valorDeducoesSubs);
        notaFiscalRelatorio.setValorBaseCalculo(this.valorBaseCalculoSubs);
        notaFiscalRelatorio.setAliquota(this.valorAliquotaSubs);
        notaFiscalRelatorio.setValorIss(this.valorIssSubs);
        notaFiscalRelatorio.setValorCredito(this.valorCreditoSubs);
        notaFiscalRelatorio.setValorNota(this.valorNotaSubs);
        notaFiscalRelatorio.setNaturezaOperacao(this.cobrancaSub);
        notaFiscalRelatorio.setTipoTributacao(this.tipoTributacaoSub);
        notaFiscalRelatorio.setLocalExecucao(this.nomeCidadeExecucao);

        // dados nota
        notaFiscal.setDescServico(descServico);
        notaFiscal.setServicoPrestado(this.servicoPrestado);
        notaFiscal.setValorDeducoes(Moeda.convert(this.valorDeducoesSubs));
        notaFiscal.setValorBaseCalculo(Moeda.convert(this.valorBaseCalculoSubs));
        notaFiscal.setAliquota(Double.parseDouble(this.valorAliquotaSubs));
        notaFiscal.setValorIss(Moeda.convert(this.valorIssSubs));
        notaFiscal.setValorCredito(Moeda.convert(this.valorCreditoSubs));
        notaFiscal.setValorNota(Moeda.convert(this.valorNotaSubs));
        notaFiscal.setNaturezaOperacao(this.cobrancaSub);
        notaFiscal.setTipoTributacao(this.tipoTributacaoSub);
        notaFiscal.setLocalExecucao(this.nomeCidadeExecucao);
        notaFiscal.setValorIssPrefeituara(this.valorIssPrefeituaraSubs);
        notaFiscal.setTipoMovimento(this.tipoMovimentoServicoSubs);
        notaFiscal.setCepTomador(this.tomador.getLogradouro().getCep());
        notaFiscal.setCepPrestador(this.currentUser.getCcm().getLogradouro().getCep());

        if (this.retencaoFonteSubs.equals("TOMADOR")) {
            notaFiscalRelatorio.setRetidoFonte("SIM");
            notaFiscal.setRetidoFonte("SIM");
        } else {
            notaFiscalRelatorio.setRetidoFonte("NÃO");
            notaFiscal.setRetidoFonte("NÃO");
        }

        notaFiscalRelatorio.setQrCode("http://chart.apis.google.com/chart?chs=150x150&cht=qr&chld=L|0&chl=http%3A%2F%2Fissmap.micromap.com.br?cod=" + codigoVerificacao);


        FacesContext facesContext1 = FacesContext.getCurrentInstance();
        ServletContext scontext1 = (ServletContext) facesContext1.getExternalContext().getContext();
        String caminhoImagem = scontext1.getRealPath("/img/logo_iss.gif");

        notaFiscalRelatorio.setCaminhoImagem(caminhoImagem);

        FacesContext facesContext2 = FacesContext.getCurrentInstance();
        ServletContext scontext2 = (ServletContext) facesContext2.getExternalContext().getContext();
        String caminhoLogoPrefeitura = scontext2.getRealPath("/img/logo_scruz.png");

        notaFiscalRelatorio.setCaminhoLogoPrefeitura(caminhoLogoPrefeitura);

        // Informações da substituição da nota

        if (nota != null) {

            //Nota Fiscal

            nota.setCancelada(true);
            nota.setSubstituida(true);

            notaFiscal.setNotaSubstituida(nota);
            try {
                Date dateGrava = new Date(dataf.parse(dataStr).getTime());
                notaFiscal.setDataSubstituicao(dateGrava);
                new DAONFSE().salvar(nota);
            } catch (java.text.ParseException ex) {
                Logger.getLogger(NfseLancaBaking.class.getName()).log(Level.SEVERE, null, ex);
            }

            //Nota Relatório
            notaFiscalRelatorio.setTxtSubstitui("Nota Fiscal substitutiva a nota nº " + nota.getNumeroNota() + " e codigo de verificação " + nota.getCogigoVerifica());
        }



        List<NfseRelatorio> listaNfse = new ArrayList<NfseRelatorio>();

        listaNfse.add(notaFiscalRelatorio);

        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listaNfse);

        HashMap parameters = new HashMap();

        try {

            new DAONFSE().salvar(notaFiscal);
            SolicitaBuscaNFSE solicitaBusca = new SolicitaBuscaNFSE();
            RespNotaEnvio resposta = solicitaBusca.retornaSolicitaBusca(ccm.getCnpjCpf(), codigoVerificacao);


            if (!resposta.getResposta()) {
                AguardandoEnvio aguardandoEnvio = new AguardandoEnvio();
                aguardandoEnvio.setNotaEspera(notaFiscal);
                new DAOAguardaEnvio().salvar(aguardandoEnvio);
            }


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

            Logger.getLogger(NfseLancaBaking.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void buscaCidadePorNome() {

        DAOCidadeIBGE daoIbge = new DAOCidadeIBGE();
        listaCidadesIbge = daoIbge.buscarCidadePorNome(buscaCidadeSubs);
//        for (IBGECidade cidade1 : listaCidadesIbge) {
//            System.out.println(cidade1.getNome());
//        }
    }

    public void alteraCidadeDeExecucao(ActionEvent actionEvent) {

        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg = null;
        boolean loggedIn = false;
        if (listaCidadesIbge.isEmpty()) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro ao buscar", "Efetue uma busca");
        } else {
            if (this.cidadeIbgeSelecionado != null) {
                this.nomeCidadeExecucao = this.cidadeIbgeSelecionado.getNome();
                loggedIn = true;
            } else {
                msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", "Selecione um item da lista");
            }
        }
        if (msg != null) {
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        context.addCallbackParam("loggedIn", loggedIn);
    }

    public void calculaCredito() {

        Double valorIssF = 0.0;
        valorIssF = Moeda.convert(valorIssSubs);

        Double valorCredito = 0.0;
        valorCredito = valorIssF * 0.30;
        this.valorCreditoSubs = moeda.formata(valorCredito);
    }

    public Double getAliquota() {
        return aliquota;
    }

    public void setAliquota(Double aliquota) {
        this.aliquota = aliquota;
    }

    public Double getBaseCalculo() {
        return baseCalculo;
    }

    public void setBaseCalculo(Double baseCalculo) {
        this.baseCalculo = baseCalculo;
    }

    public Boolean getBtAnexa() {
        return btAnexa;
    }

    public void setBtAnexa(Boolean btAnexa) {
        this.btAnexa = btAnexa;
    }

    public String getCartaCorracao() {
        return cartaCorracao;
    }

    public void setCartaCorracao(String cartaCorracao) {
        this.cartaCorracao = cartaCorracao;
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

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
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

    public String getEndTomador() {
        return endTomador;
    }

    public void setEndTomador(String endTomador) {
        this.endTomador = endTomador;
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

    public NFSE getNota() {
        return nota;
    }

    public void setNota(NFSE nota) {
        this.nota = nota;
    }

    public String getNumeroNota() {
        return numeroNota;
    }

    public void setNumeroNota(String numeroNota) {
        this.numeroNota = numeroNota;
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

    public String getBairroTomador() {
        return bairroTomador;
    }

    public void setBairroTomador(String bairroTomador) {
        this.bairroTomador = bairroTomador;
    }

    public Boolean getBotaoCadastraTomador() {
        return botaoCadastraTomador;
    }

    public void setBotaoCadastraTomador(Boolean botaoCadastraTomador) {
        this.botaoCadastraTomador = botaoCadastraTomador;
    }

    public String getCepTomador() {
        return cepTomador;
    }

    public void setCepTomador(String cepTomador) {
        this.cepTomador = cepTomador;
    }

    public String getCidadeTomador() {
        return cidadeTomador;
    }

    public void setCidadeTomador(String cidadeTomador) {
        this.cidadeTomador = cidadeTomador;
    }

    public String getCnpjCpf() {
        return cnpjCpf;
    }

    public void setCnpjCpf(String cnpjCpf) {
        this.cnpjCpf = cnpjCpf;
    }

    public String getComplementoTomador() {
        return complementoTomador;
    }

    public void setComplementoTomador(String complementoTomador) {
        this.complementoTomador = complementoTomador;
    }

    public Usuario getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Usuario currentUser) {
        this.currentUser = currentUser;
    }

    public String getEmailTomador() {
        return emailTomador;
    }

    public void setEmailTomador(String emailTomador) {
        this.emailTomador = emailTomador;
    }

    public String getEnderecoTomador() {
        return enderecoTomador;
    }

    public void setEnderecoTomador(String enderecoTomador) {
        this.enderecoTomador = enderecoTomador;
    }

    public String getEstadoTomador() {
        return estadoTomador;
    }

    public void setEstadoTomador(String estadoTomador) {
        this.estadoTomador = estadoTomador;
    }

    public Boolean getExNfse() {
        return exNfse;
    }

    public void setExNfse(Boolean exNfse) {
        this.exNfse = exNfse;
    }

    public String getFoneFaxTomador() {
        return foneFaxTomador;
    }

    public void setFoneFaxTomador(String foneFaxTomador) {
        this.foneFaxTomador = foneFaxTomador;
    }

    public String getIeRgTomador() {
        return ieRgTomador;
    }

    public void setIeRgTomador(String ieRgTomador) {
        this.ieRgTomador = ieRgTomador;
    }

    public String getInscricaoMunicipalTomador() {
        return inscricaoMunicipalTomador;
    }

    public void setInscricaoMunicipalTomador(String inscricaoMunicipalTomador) {
        this.inscricaoMunicipalTomador = inscricaoMunicipalTomador;
    }

    public String getNomeTomador() {
        return nomeTomador;
    }

    public void setNomeTomador(String nomeTomador) {
        this.nomeTomador = nomeTomador;
    }

    public String getNumeroTomador() {
        return numeroTomador;
    }

    public void setNumeroTomador(String numeroTomador) {
        this.numeroTomador = numeroTomador;
    }

    public String getCpfCnpjTomadorCadastra() {
        return cpfCnpjTomadorCadastra;
    }

    public void setCpfCnpjTomadorCadastra(String cpfCnpjTomadorCadastra) {
        this.cpfCnpjTomadorCadastra = cpfCnpjTomadorCadastra;
    }

    public String getServicoSelecionado() {
        return servicoSelecionado;
    }

    public void setServicoSelecionado(String servicoSelecionado) {
        this.servicoSelecionado = servicoSelecionado;
    }

    public List<IBGECidade> getListaCidadesIbge() {
        return listaCidadesIbge;
    }

    public void setListaCidadesIbge(List<IBGECidade> listaCidadesIbge) {
        this.listaCidadesIbge = listaCidadesIbge;
    }

    public List<Servico> getListaServicos() {
        return listaServicos;
    }

    public void setListaServicos(List<Servico> listaServicos) {
        this.listaServicos = listaServicos;
    }

    public String getRetencaoFonteSubs() {
        return retencaoFonteSubs;
    }

    public void setRetencaoFonteSubs(String retencaoFonteSubs) {
        this.retencaoFonteSubs = retencaoFonteSubs;
    }

    public String getTipoMovimentoServicoSubs() {
        return tipoMovimentoServicoSubs;
    }

    public void setTipoMovimentoServicoSubs(String tipoMovimentoServicoSubs) {
        this.tipoMovimentoServicoSubs = tipoMovimentoServicoSubs;
    }

    public String getValorAliquotaSubs() {
        return valorAliquotaSubs;
    }

    public void setValorAliquotaSubs(String valorAliquotaSubs) {
        this.valorAliquotaSubs = valorAliquotaSubs;
    }

    public String getValorBaseCalculoSubs() {
        return valorBaseCalculoSubs;
    }

    public void setValorBaseCalculoSubs(String valorBaseCalculoSubs) {
        this.valorBaseCalculoSubs = valorBaseCalculoSubs;
    }

    public String getValorCreditoSubs() {
        return valorCreditoSubs;
    }

    public void setValorCreditoSubs(String valorCreditoSubs) {
        this.valorCreditoSubs = valorCreditoSubs;
    }

    public String getValorDeducoesSubs() {
        return valorDeducoesSubs;
    }

    public void setValorDeducoesSubs(String valorDeducoesSubs) {
        this.valorDeducoesSubs = valorDeducoesSubs;
    }

    public Double getValorIssPrefeituaraSubs() {
        return valorIssPrefeituaraSubs;
    }

    public void setValorIssPrefeituaraSubs(Double valorIssPrefeituaraSubs) {
        this.valorIssPrefeituaraSubs = valorIssPrefeituaraSubs;
    }

    public String getValorIssSubs() {
        return valorIssSubs;
    }

    public void setValorIssSubs(String valorIssSubs) {
        this.valorIssSubs = valorIssSubs;
    }

    public String getValorNotaSubs() {
        return valorNotaSubs;
    }

    public void setValorNotaSubs(String valorNotaSubs) {
        this.valorNotaSubs = valorNotaSubs;
    }

    public String getCobrancaSub() {
        return cobrancaSub;
    }

    public void setCobrancaSub(String cobrancaSub) {
        this.cobrancaSub = cobrancaSub;
    }

    public String getTipoTributacaoSub() {
        return tipoTributacaoSub;
    }

    public void setTipoTributacaoSub(String tipoTributacaoSub) {
        this.tipoTributacaoSub = tipoTributacaoSub;
    }

    public String getBuscaCidadeSubs() {
        return buscaCidadeSubs;
    }

    public void setBuscaCidadeSubs(String buscaCidadeSubs) {
        this.buscaCidadeSubs = buscaCidadeSubs;
    }

    public IBGECidade getCidadeIbgeSelecionado() {
        return cidadeIbgeSelecionado;
    }

    public void setCidadeIbgeSelecionado(IBGECidade cidadeIbgeSelecionado) {
        this.cidadeIbgeSelecionado = cidadeIbgeSelecionado;
    }

    public String getNomeCidadeExecucao() {
        return nomeCidadeExecucao;
    }

    public void setNomeCidadeExecucao(String nomeCidadeExecucao) {
        this.nomeCidadeExecucao = nomeCidadeExecucao;
    }
}
