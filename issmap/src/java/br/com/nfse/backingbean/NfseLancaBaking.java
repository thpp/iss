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
import br.com.nfse.jsfuntil.AutenticaUsuario;
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
import br.com.nfse.to.relatorio.NfseRelatorio;
import br.com.nfse.to.Servico;
import br.com.nfse.to.Usuario;
import br.com.nfse.ws.beans.RespNotaEnvio;
import br.com.nfse.ws.cliente.SolicitaBuscaNFSE;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
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
 * @author ThiagoHenrique
 */
@ManagedBean(name = "MBNfseLanca")
@ViewScoped
public class NfseLancaBaking {

    private String cnpjCpf;
    private boolean nfse = false;
    private boolean botaoCadastraTomador = true;
    private String nome;
    private String ieRg;
    private String endereco;
    private String numero;
    private String complemento;
    private String bairro;
    private String foneFax;
    private String cidade;
    private String estado;
    private String cep;
    private String email;
    private String inscricaoMunicipal;
    private Usuario currentUser;
    private Ccm tomador = null;
    private Servico servicoResultado = null;
    private String servicoPrestado = null;
    private String servicoSelecionado;
    private String valorAliquota;
    private String descServico;
    private List<Servico> listaServicos = new ArrayList<Servico>();
    private String valorDeducoes = "0,00";
    private String valorBaseCalculo = "0,00";
    private String valorIss = "0,00";
    private String valorCredito = "0,00";
    private String valorNota = "0,00";
    private Double valorIssPrefeituara = 0.0;
    private String buscaCidade;
    private List<IBGECidade> listaCidadesIbge = new ArrayList<IBGECidade>();
    private IBGECidade cidadeIbgeSelecionado = new IBGECidade();
    private String nomeCidadeExecucao = "Santa Cruz do Rio Pardo";
    private String cobranca;
    private String tipoTributacao;
    private String retencaoFonte;
    private Ccm ccm;
    private String tipoMovimentoServico;
    private String template;
    private String horaLancamrnto;
    Moeda moeda = new Moeda();

    public NfseLancaBaking() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();
        this.currentUser = (Usuario) session.getAttribute("usuario");
        this.ccm = this.currentUser.getCcm();

        if (ccm.getCodigoEventualGemmap() == null) {
            
            this.listaServicos = this.ccm.getListaServicosPrestados();
            this.servicoResultado = buscaServicoLista(this.listaServicos.get(0).getId());
            this.valorAliquota = this.servicoResultado.getAliquotaJ().toString();

            this.tipoMovimentoServico = this.servicoResultado.getTipoMovimento();

            if (this.servicoResultado.isRetencaoFonte()) {
                this.retencaoFonte = "SIM";
            } else {
                this.retencaoFonte = "NÃO";
            }
            this.servicoPrestado = this.servicoResultado.getItemTabela();

            if (currentUser.getCcm().isCobraca()) {
                cobranca = "Tributável";
            } else {
                cobranca = "Isento";
            }
            if (currentUser.getCcm().isAutonomo()) {
                tipoTributacao = "Autônomo";
            } else if (currentUser.getCcm().isMei()) {
                tipoTributacao = "Empreendedor Individual";
            } else if (currentUser.getCcm().isProfissionalLiberal()) {
                tipoTributacao = "Profissional Liberal";
            } else if (currentUser.getCcm().isSimples()) {
                tipoTributacao = "Simples Nacional";
            } else {
                tipoTributacao = "Normal";
            }
        }else{
            this.servicoResultado = new Servico();
        }
        this.template = AutenticaUsuario.verificaTemplate(this.currentUser);

    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public void buscaTomador() {

        DAOCcm ccmDao = new DAOCcm();

        cnpjCpf = cnpjCpf.replaceAll("\\/", "").replaceAll("\\.", "").replaceAll("-", "");

        if (this.cnpjCpf.length() == 14) {
            if (!CnpjValidator.validaCNPJ(this.cnpjCpf)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "CNPJ/CPF Incorreto", ""));
                this.nfse = false;
                limpaCampoTomador();
                return;
            }
        } else if (this.cnpjCpf.length() == 11) {
            if (!CpfValidator.validaCPF(this.cnpjCpf)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "CNPJ/CPF Incorreto", ""));
                this.nfse = false;
                limpaCampoTomador();
                return;
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "CNPJ/CPF Incorreto", ""));
            this.nfse = false;
            limpaCampoTomador();
            return;
        }
        try {
            this.tomador = ccmDao.buscaPorCnpjCpf(cnpjCpf);

            if (tomador != null) {
                this.botaoCadastraTomador = true;
                this.nfse = true;
                this.nome = tomador.getNomeRazao();
                this.ieRg = tomador.getIeRg();
                this.inscricaoMunicipal = tomador.getIm();
                this.cep = tomador.getLogradouro().getCep();

                this.numero = tomador.getNumeroPredio().toString();
                this.complemento = tomador.getComplemento();
                this.cidade = tomador.getLogradouro().getBairro().getCidade().getNome();
                this.estado = tomador.getLogradouro().getBairro().getCidade().getEstado().getSigla();
                this.foneFax = tomador.getTelefone();
                this.email = tomador.getEmail();

                String rua = tomador.getRua();

                if (rua == null) {
                    this.endereco = tomador.getLogradouro().getNome();
                    this.bairro = tomador.getLogradouro().getBairro().getNome();
                } else {
                    this.endereco = tomador.getRua();
                    this.bairro = tomador.getBairro();
                }
            } else {
                this.botaoCadastraTomador = false;
                this.nfse = true;
                limpaCampoTomador();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "CNPJ/CPF Não Cadastrado.", "Preencha os dados e efetue o cadastro para emissão da Nota Fiscal Eletronica de Serviço"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao buscar tomador", e.getMessage()));
        }

    }

    public void cadastraTomador() {
        tomador = new Ccm();

        tomador.setCnpjCpf(this.cnpjCpf.replaceAll("\\/", "").replaceAll("\\.", "").replaceAll("-", ""));

        if (this.cnpjCpf.length() == 11) {
            tomador.setTipoPessoa(2);
        } else if (this.cnpjCpf.length() == 14) {
            tomador.setTipoPessoa(1);
        }

        if (!this.nome.equals("")) {
            tomador.setNomeRazao(this.nome);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro campos obrigatorios NOME", ""));
            return;
        }

        if (!this.ieRg.equals("")) {
            tomador.setIeRg(this.ieRg.replaceAll("\\.", "").replaceAll("\\-", ""));
        }
        if (!this.inscricaoMunicipal.equals("")) {
            tomador.setIm(this.inscricaoMunicipal.replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("\\/", ""));
        }

        if (!this.cep.equals("")) {
            DAOLogradouro daoLogradouro = new DAOLogradouro();
            Logradouro logra;
            try {
                logra = daoLogradouro.buscaPorNomeCep(this.cep.replace("-", ""));

                tomador.setLogradouro(logra);

                if (logra.getNome().equals("")) {
                    if (!this.endereco.equals("")) {
                        tomador.setRua(this.endereco);
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro campos obrigatorios ENDEREÇO", ""));
                        return;
                    }
                    if (!this.bairro.equals("")) {
                        tomador.setBairro(this.bairro);
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

        if (!this.numero.equals("")) {
            tomador.setNumeroPredio(numero);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro campos obrigatorios NUMERO", ""));
            return;
        }

        if (!this.complemento.equals("")) {
            tomador.setComplemento(this.complemento);
        }

        if (!this.foneFax.equals("")) {
            tomador.setTelefone(this.foneFax.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("-", "").replaceAll(" ", ""));
        }

        if (!this.email.equals("")) {

            Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
            Matcher m = p.matcher(this.email);

            boolean matchFound = m.matches();

            if (!matchFound) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "E-mail Incorreto"));
                return;
            } else {
                tomador.setEmail(this.email);
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
            Logradouro logradouro = daoLogradouro.buscaPorNomeCep(this.cep.replace("-", ""));

            this.endereco = logradouro.getNome();
            this.bairro = logradouro.getBairro().getNome();
            this.cidade = logradouro.getBairro().getCidade().getNome();
            this.estado = logradouro.getBairro().getCidade().getEstado().getSigla();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "CEP INVÁLIDO"));
        }
    }

    private void limpaCampoTomador() {

        this.nome = "";
        this.ieRg = "";
        this.inscricaoMunicipal = "";
        this.cep = "";

        this.numero = "";
        this.complemento = "";
        this.cidade = "";
        this.estado = "";
        this.foneFax = "";
        this.email = "";

        this.endereco = "";
        this.bairro = "";

    }

    public void alteraAliquota() {

        Servico servicoSelecao = new Servico();
        for (Servico serv : this.listaServicos) {
            if (serv.getId().toString().equals(this.servicoSelecionado)) {
                servicoSelecao = serv;
                this.valorAliquota = servicoSelecao.getAliquotaJ().toString();
                this.servicoPrestado = servicoSelecao.getItemTabela();
                if (servicoSelecao.isRetencaoFonte()) {
                    this.retencaoFonte = "SIM";
                } else {
                    this.retencaoFonte = "NÃO";
                }
                this.tipoMovimentoServico = servicoSelecao.getTipoMovimento();
                break;
            }
        }
        calculaISS();
    }

    public void imprimeRelatorio() throws IOException, SQLException, ClassNotFoundException {


        if (descServico.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "Informe a descrição do serviço prestado"));
            return;
        }

        if (valorNota.equals("0,00")) {
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
        Long numeroNota = NotaUtil.proximoNumeroNota(currentUser.getCcm().getCnpjCpf());

        String dataRelatorio = diaf.format(datas) + "/" + mesf.format(datas) + "/" + anof.format(datas) + " " + horaf.format(datas) + ":" + minutof.format(datas) + ":" + segundof.format(datas);
        String codigoVerificacao = currentUser.getCcm().getId().toString() + horaf.format(datas) + minutof.format(datas) + segundof.format(datas);
        codigoVerificacao = CodigoVerificaNota.encode(Long.parseLong(codigoVerificacao));
        codigoVerificacao = codigoVerificacao.toUpperCase();
        Ccm ccm = currentUser.getCcm();

        horaLancamrnto = horaf.format(datas) + ":" + minutof.format(datas) + ":" + segundof.format(datas);

        NFSE notaFiscal = new NFSE();
        NfseRelatorio notaFiscalRelatorio = new NfseRelatorio();

        //itens relatório
        notaFiscalRelatorio.setCogigoVerifica(codigoVerificacao);
        notaFiscalRelatorio.setDataEmissao(dataRelatorio);
        notaFiscalRelatorio.setNumeroNota(NotaUtil.fomrmataNumeroNota(numeroNota));
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
        } catch (ParseException ex) {
            Logger.getLogger(NfseLancaBaking.class.getName()).log(Level.SEVERE, null, ex);
        }
        notaFiscal.setNumeroNota(numeroNota);
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
        notaFiscalRelatorio.setValorDeducoes(this.valorDeducoes);
        notaFiscalRelatorio.setValorBaseCalculo(this.valorBaseCalculo);
        notaFiscalRelatorio.setAliquota(this.valorAliquota);
        notaFiscalRelatorio.setValorIss(this.valorIss);
        notaFiscalRelatorio.setValorCredito(this.valorCredito);
        notaFiscalRelatorio.setValorNota(this.valorNota);
        notaFiscalRelatorio.setNaturezaOperacao(this.cobranca);
        notaFiscalRelatorio.setTipoTributacao(this.tipoTributacao);
        notaFiscalRelatorio.setLocalExecucao(this.nomeCidadeExecucao);

        // dados nota
        notaFiscal.setDescServico(descServico);
        notaFiscal.setServicoPrestado(this.servicoPrestado);
        notaFiscal.setValorDeducoes(Moeda.convert(this.valorDeducoes));
        notaFiscal.setValorBaseCalculo(Moeda.convert(this.valorBaseCalculo));
        notaFiscal.setAliquota(Double.parseDouble(this.valorAliquota));
        notaFiscal.setValorIss(Moeda.convert(this.valorIss));
        notaFiscal.setValorCredito(Moeda.convert(this.valorCredito));
        notaFiscal.setValorNota(Moeda.convert(this.valorNota));
        notaFiscal.setNaturezaOperacao(this.cobranca);
        notaFiscal.setTipoTributacao(this.tipoTributacao);
        notaFiscal.setLocalExecucao(this.nomeCidadeExecucao);
        notaFiscal.setValorIssPrefeituara(this.valorIssPrefeituara);
        notaFiscal.setTipoMovimento(this.tipoMovimentoServico);
        notaFiscal.setCepTomador(this.tomador.getLogradouro().getCep());
        notaFiscal.setCepPrestador(this.currentUser.getCcm().getLogradouro().getCep());

        if (this.retencaoFonte.equals("TOMADOR")) {
            notaFiscalRelatorio.setRetidoFonte("SIM");
            notaFiscal.setRetidoFonte("SIM");
        } else {
            notaFiscalRelatorio.setRetidoFonte("NÃO");
            notaFiscal.setRetidoFonte("NÃO");
        }

        notaFiscalRelatorio.setQrCode("http://chart.apis.google.com/chart?chs=150x150&cht=qr&chld=L|0&chl=http%3A%2F%2F201.43.6.22:9090/IssMap/?cod=" + codigoVerificacao);


        FacesContext facesContext1 = FacesContext.getCurrentInstance();
        ServletContext scontext1 = (ServletContext) facesContext1.getExternalContext().getContext();
        String caminhoImagem = scontext1.getRealPath("/img/logo_iss.gif");

        notaFiscalRelatorio.setCaminhoImagem(caminhoImagem);

        FacesContext facesContext2 = FacesContext.getCurrentInstance();
        ServletContext scontext2 = (ServletContext) facesContext2.getExternalContext().getContext();
        String caminhoLogoPrefeitura = scontext2.getRealPath("/img/logo_scruz.png");

        //texto da nota de substituição
        notaFiscalRelatorio.setTxtSubstitui("");

        notaFiscalRelatorio.setCaminhoLogoPrefeitura(caminhoLogoPrefeitura);

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

    public void calculaBase() {

        Double valorNotaF = 0.0;
        valorNotaF = Moeda.convert(valorNota);

        Double valorDeducoesF = 0.0;
        valorDeducoesF = Moeda.convert(valorDeducoes);

        Double valorBaseF = 0.0;
        valorBaseF = Moeda.convert(valorBaseCalculo);

        valorBaseF = valorNotaF - valorDeducoesF;
        this.valorBaseCalculo = moeda.formata(valorBaseF);
        this.valorNota = moeda.formata(valorNotaF);
        //System.out.println("Valor da nota >>>>>>>>>>>>>>>>>> " + this.valorNota);
        this.valorDeducoes = moeda.formata(valorDeducoesF);

        calculaISS();

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

    public void calculaISS() {

        Double valorBaseF = 0.0;
        valorBaseF = Moeda.convert(valorBaseCalculo);

        Double porcentagem = 0.0;
        porcentagem = Moeda.convertPorcentagem(valorAliquota);
        Double valorIssF = 0.0;
        valorIssF = valorBaseF * porcentagem;
        this.valorIssPrefeituara = valorIssF;
        valorIss = moeda.formata(valorIssF);


        if (ccm.isSimples() || ccm.isMei()) {
            this.valorIss = moeda.formata(0.0);
            return;
        }
        if (!this.tipoMovimentoServico.equals("VARIAVEL")) {
            this.valorIss = moeda.formata(0.0);
            return;
        }

        calculaCredito();
    }

    public void calculaCredito() {

        Double valorIssF = 0.0;
        valorIssF = Moeda.convert(valorIss);

        Double valorCredito = 0.0;
        valorCredito = valorIssF * 0.30;
        this.valorCredito = moeda.formata(valorCredito);
    }

    public void buscaCidadePorNome() {

        DAOCidadeIBGE daoIbge = new DAOCidadeIBGE();
        listaCidadesIbge = daoIbge.buscarCidadePorNome(buscaCidade);
//        for (IBGECidade cidade1 : listaCidadesIbge) {
//            System.out.println(cidade1.getNome());
//        }
    }

    public void alteraCidadeDeExecucao(ActionEvent actionEvent) {

        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg = null;
        boolean loggedIn1 = false;
        if (listaCidadesIbge.isEmpty()) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro ao buscar", "Efetue uma busca");
        } else {
            if (this.cidadeIbgeSelecionado != null) {
                this.nomeCidadeExecucao = this.cidadeIbgeSelecionado.getNome();
                loggedIn1 = true;
            } else {
                msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", "Selecione um item da lista");
            }
        }
        if (msg != null) {
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        context.addCallbackParam("loggedIn1", loggedIn1);
    }

    public String getValorBaseCalculo() {
        return valorBaseCalculo;
    }

    public void setValorBaseCalculo(String valorBaseCalculo) {
        this.valorBaseCalculo = valorBaseCalculo;
    }

    public String getValorCredito() {
        return valorCredito;
    }

    public void setValorCredito(String valorCredito) {
        this.valorCredito = valorCredito;
    }

    public String getValorIss() {
        return valorIss;
    }

    public void setValorIss(String valorIss) {
        this.valorIss = valorIss;
    }

    public String getValorNota() {
        return valorNota;
    }

    public void setValorNota(String valorNota) {
        this.valorNota = valorNota;
    }

    public String getValorDeducoes() {
        return valorDeducoes;
    }

    public void setValorDeducoes(String valorDeducoes) {
        this.valorDeducoes = valorDeducoes;
    }

    public boolean isNfse() {
        return nfse;
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

    public String getCnpjCpf() {
        return cnpjCpf;
    }

    public void setCnpjCpf(String cnpjCpf) {
        this.cnpjCpf = cnpjCpf;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFoneFax() {
        return foneFax;
    }

    public void setFoneFax(String foneFax) {
        this.foneFax = foneFax;
    }

    public String getIeRg() {
        return ieRg;
    }

    public void setIeRg(String ieRg) {
        this.ieRg = ieRg;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getInscricaoMunicipal() {
        return inscricaoMunicipal;
    }

    public void setInscricaoMunicipal(String inscricaoMunicipal) {
        this.inscricaoMunicipal = inscricaoMunicipal;
    }

    public String getServicoSelecionado() {
        return servicoSelecionado;
    }

    public void setServicoSelecionado(String servicoSelecionado) {
        this.servicoSelecionado = servicoSelecionado;
    }

    public List<Servico> getListaServicos() {
        return listaServicos;
    }

    public void setListaServicos(List<Servico> listaServicos) {
        this.listaServicos = listaServicos;
    }

    public String getValorAliquota() {
        return valorAliquota;
    }

    public void setValorAliquota(String valorAliquota) {
        this.valorAliquota = valorAliquota;
    }

    public String getDescServico() {
        return descServico;
    }

    public void setDescServico(String descServico) {
        this.descServico = descServico;
    }

    public List<IBGECidade> getListaCidadesIbge() {
        return listaCidadesIbge;
    }

    public void setListaCidadesIbge(List<IBGECidade> listaCidadesIbge) {
        this.listaCidadesIbge = listaCidadesIbge;
    }

    public String getBuscaCidade() {
        return buscaCidade;
    }

    public void setBuscaCidade(String buscaCidade) {
        this.buscaCidade = buscaCidade;
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

    public String getCobranca() {
        return cobranca;
    }

    public void setCobranca(String cobranca) {
        this.cobranca = cobranca;
    }

    public String getTipoTributacao() {
        return tipoTributacao;
    }

    public void setTipoTributacao(String tipoTributacao) {
        this.tipoTributacao = tipoTributacao;
    }

    public String getRetencaoFonte() {
        return retencaoFonte;
    }

    public void setRetencaoFonte(String retencaoFonte) {
        this.retencaoFonte = retencaoFonte;
    }

    public boolean isBotaoCadastraTomador() {
        return botaoCadastraTomador;
    }

    public String getTipoMovimentoServico() {
        return tipoMovimentoServico;
    }

    public void setTipoMovimentoServico(String tipoMovimentoServico) {
        this.tipoMovimentoServico = tipoMovimentoServico;
    }
}
