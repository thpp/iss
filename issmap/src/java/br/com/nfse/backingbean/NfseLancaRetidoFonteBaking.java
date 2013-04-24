/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.backingbean;

import br.com.nfse.dao.DAOAguardaEnvio;
import br.com.nfse.dao.DAOCcm;
import br.com.nfse.dao.DAOLogradouro;
import br.com.nfse.jsfuntil.CnpjValidator;
import br.com.nfse.jsfuntil.CpfValidator;
import br.com.nfse.to.Ccm;
import br.com.nfse.to.Logradouro;
import br.com.nfse.ws.beans.RespNotaEnvio;
import br.com.nfse.ws.cliente.SolicitaBuscaNFSE;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import br.com.nfse.dao.DAOBoleto;
import br.com.nfse.dao.DAONFSE;
import br.com.nfse.jsfuntil.AutenticaUsuario;
import br.com.nfse.jsfuntil.ConfiguracaoSyst;
import br.com.nfse.jsfuntil.Moeda;
import br.com.nfse.jsfuntil.NotaUtil;
import br.com.nfse.to.AguardandoEnvio;
import br.com.nfse.to.BoletoIssMap;
import br.com.nfse.to.HistoricoNotaRrtidoFonte;
import br.com.nfse.to.NFSE;
import br.com.nfse.to.TesteCampoLivre;
import br.com.nfse.to.Usuario;
import br.com.nfse.ws.beans.DadosBoletoWS;
import br.com.nfse.ws.cliente.BuscaDadosBoletoGemmap;
import com.a.a.a.g.m.s;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.faces.application.FacesMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
 * @author ThiagoHenrique
 */
@ManagedBean(name = "MBNfseLancaRetidoFonte")
@ViewScoped
public class NfseLancaRetidoFonteBaking {

    private String cnpjCpf;
    private boolean nfse = false;
    private boolean botaoCadastraPrestador = true;
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
    private Ccm prestador = null;
    private String valorDeducoes = "0,00";
    private String valorBaseCalculo = "0,00";
    private String valorIss = "0,00";
    private String valorNota = "0,00";
    private Double valorIssPrefeituara = 0.0;
    private String nomeCidadeExecucao = "Santa Cruz do Rio Pardo";
    private String template;
    private Ccm ccm;
    private String numeroNota;
    private String dataHora;
    private String codigoVerificacao;
    private String codigoAtividade;
    private String atividade;
    private String aliquota = "0";
    private String descricaoServico;
    private String enderecoValidacao;
    Moeda moeda = new Moeda();
    private Date dataEmissaoNota;
    private String horaEmissaoNota;
    private Date dataLancamentoNota;
    private String horaLancamentoNota;
    private String dataStr = "";
    private DateFormat dataf = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat horaf = new SimpleDateFormat("HH:mm");
    private Boolean btLancaNota = false;
    private Boolean btGuia = true;
    private Date data;
    private String escolhePDF;
    private String dataEmicaoStr;
    NFSE notaFiscal = null;
    private Date dataHoje;

    public NfseLancaRetidoFonteBaking() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();
        this.currentUser = (Usuario) session.getAttribute("usuario");
        this.ccm = this.currentUser.getCcm();

        data = Calendar.getInstance(Locale.getDefault()).getTime();
        dataStr = dataf.format(data);
        horaLancamentoNota = horaf.format(data);
        this.template = AutenticaUsuario.verificaTemplate(this.currentUser);

    }

    public void lancaNotaRetidoFonte() {

        DAONFSE daoNota = new DAONFSE();
        try {
            NFSE nf = daoNota.buscaNota(prestador.getCnpjCpf(), codigoVerificacao);
            if (nf != null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nota já cadastrada! ", "Verifique se os dados da nota estão corretos!"));
                return;
            }
        } catch (Exception ex) {
            Logger.getLogger(NfseLancaRetidoFonteBaking.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (("".equals(codigoVerificacao) || "".equals(numeroNota) || "".equals(codigoAtividade) || "".equals(atividade) || "".equals(aliquota) || "".equals(descricaoServico))) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falta de Informações! ", "Verifique se os dados da nota estão corretos!"));
            return;
        }

        notaFiscal = new NFSE();


        try {
            dataHoje = new Date(dataf.parse(dataStr).getTime());
            //System.out.println(dateGrava);
            notaFiscal.setDataEmissao(dataEmissaoNota);
            notaFiscal.setDataVencimento(dataHoje);

            //so é gravado quando a nota é de fora retido na fonte (terceiro)
            notaFiscal.setDataLancamentoNotaTerceiro(dataHoje);
            notaFiscal.setHoraLancamentoNotaTerceiro(horaf.format(Calendar.getInstance(Locale.getDefault()).getTime()));
        } catch (ParseException ex) {
            Logger.getLogger(NfseLancaBaking.class.getName()).log(Level.SEVERE, null, ex);
        }


        if (!"".equals(dataEmicaoStr)) {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            df.setLenient(false);
            try {
                dataEmissaoNota = df.parse(dataEmicaoStr);
                // data válida  

                if (dataEmissaoNota.after(dataHoje)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Data Emissão Nota! ", "Verifique a data de emissão informada!"));
                    return;
                }

                Calendar c = Calendar.getInstance();
                c.add(Calendar.YEAR, -5);

                if (dataEmissaoNota.before(c.getTime())) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Data Emissão Nota! ", "Verifique a data de emissão informada!"));
                    return;
                }

            } catch (ParseException ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Data Emissão Nota! ", "Verifique a data de emissão informada!"));
                return;
            }
        }



        notaFiscal.setCidadeTomador(ccm.getLogradouro().getBairro().getCidade().getNome());
        notaFiscal.setNomeRazaoTomador(ccm.getNomeRazao());

        if (ccm.getRua() == null) {
            notaFiscal.setEnderecoTomador(ccm.getLogradouro().getNome() + " " + ccm.getNumeroPredio());
        } else {
            notaFiscal.setEnderecoTomador(ccm.getRua() + " " + ccm.getNumeroPredio());
        }

        notaFiscal.setImTomador(ccm.getIm());
        notaFiscal.setEstadoTomador(ccm.getLogradouro().getBairro().getCidade().getEstado().getSigla());
        notaFiscal.setCpfCnpjTomador(ccm.getCnpjCpf());
        notaFiscal.setIeRgTomador(ccm.getIeRg());
        notaFiscal.setEmailTomador(ccm.getEmail());
        notaFiscal.setEnderecoValidacaoNotaTerceiros(enderecoValidacao);

        if (prestador != null) {
            //itens nota
            notaFiscal.setCidadePrestador(this.prestador.getLogradouro().getBairro().getCidade().getNome());
            notaFiscal.setNomeRazaoPrestador(this.prestador.getNomeRazao());

            if (prestador.getRua() == null) {
                notaFiscal.setEnderecoPrestador(this.prestador.getLogradouro().getNome() + " " + this.prestador.getNumeroPredio());
            } else {
                notaFiscal.setEnderecoPrestador(this.prestador.getRua() + " " + this.prestador.getNumeroPredio());
            }

            notaFiscal.setImPrestador(this.prestador.getIm());
            notaFiscal.setIeRgPrestador(this.prestador.getIeRg());
            notaFiscal.setEstadoPrestador(this.prestador.getLogradouro().getBairro().getCidade().getEstado().getSigla());
            notaFiscal.setCpfCnpjPrestador(this.prestador.getCnpjCpf());
            notaFiscal.setEmailPrestador(this.prestador.getEmail());
        }

        // dados nota
        notaFiscal.setCogigoVerifica(codigoVerificacao);
        notaFiscal.setHora(horaEmissaoNota);
        notaFiscal.setRetidoFonte("Sim");
        notaFiscal.setNotaTerceiro(true);
        notaFiscal.setDescServico(descricaoServico);
        notaFiscal.setServicoPrestado(codigoAtividade + " - " + atividade);

        try {
            notaFiscal.setNumeroNota(Long.parseLong(numeroNota));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "NUMERO NOTA! ", "Disconsidere série ou qualquer valor não numérico. INFORME NA DESCRIÇÃO DA NOTA."));
            return;
        }


        try {

            double ali = Moeda.convertPorcentagemMelhorado(aliquota);
            double base = Moeda.convert(this.valorBaseCalculo);

            double totalIss = base * ali;

            notaFiscal.setAliquota(Double.parseDouble(aliquota.replaceAll(",", ".")));
            notaFiscal.setValorDeducoes(Moeda.convert(this.valorDeducoes));
            notaFiscal.setValorBaseCalculo(Moeda.convert(this.valorBaseCalculo));

            notaFiscal.setValorIss(totalIss);

            notaFiscal.setValorNota(Moeda.convert(this.valorNota));

        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO VALORES! ", "Verifique os Valores Informados!"));
            return;
        }


        if (prestador.isCobraca()) {
            notaFiscal.setNaturezaOperacao("Tributável");
        } else {
            if (prestador.getDataLiberacaoGemmap() == null) {
                notaFiscal.setNaturezaOperacao("--");
            } else {
                notaFiscal.setNaturezaOperacao("isento");
            }
        }

        if (prestador.isAutonomo()) {
            notaFiscal.setTipoTributacao("Autônomo");
        } else if (prestador.isMei()) {
            notaFiscal.setTipoTributacao("Empreendedor Individual");
        } else if (prestador.isProfissionalLiberal()) {
            notaFiscal.setTipoTributacao("Profissional Liberal");
        } else if (prestador.isSimples()) {
            notaFiscal.setTipoTributacao("Simples Nacional");
        } else if (prestador.getDataLiberacaoGemmap() == null) {
            notaFiscal.setTipoTributacao("--");
        } else {
            notaFiscal.setTipoTributacao("Normal");
        }

        notaFiscal.setLocalExecucao(this.nomeCidadeExecucao);
        notaFiscal.setValorIssPrefeituara(Moeda.convert(valorIss));
        notaFiscal.setCepTomador(this.ccm.getLogradouro().getCep());
        notaFiscal.setCepPrestador(this.prestador.getLogradouro().getCep());
        notaFiscal.setValorCredito(Moeda.convert(valorIss));

        try {
            daoNota.salvar(notaFiscal);
            SolicitaBuscaNFSE solicitaBusca = new SolicitaBuscaNFSE();
            RespNotaEnvio resposta = solicitaBusca.retornaSolicitaBusca(prestador.getCnpjCpf(), codigoVerificacao);


            if (!resposta.getResposta()) {
                AguardandoEnvio aguardandoEnvio = new AguardandoEnvio();
                aguardandoEnvio.setNotaEspera(notaFiscal);
                new DAOAguardaEnvio().salvar(aguardandoEnvio);
            }
            btLancaNota = true;
            btGuia = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Nota lançada com sucesso! ", ""));
        } catch (SQLException ex) {
            Logger.getLogger(NfseLancaRetidoFonteBaking.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void acertaAliquota() {

        aliquota = aliquota.replaceAll(",", ".");

    }

    public void calculaISS() {

        Double valorBaseF = 0.0;
        valorBaseF = Moeda.convert(valorBaseCalculo);

        Double porcentagem = 0.0;
        porcentagem = Moeda.convertPorcentagem(aliquota);
        Double valorIssF = 0.0;
        valorIssF = valorBaseF * porcentagem;
        this.valorIssPrefeituara = valorIssF;
        valorIss = moeda.formata(valorIssF);

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

    public void imprimeBoleto() {

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


        dadosBoletoWs = new BuscaDadosBoletoGemmap().retornaMobiliario(ConfiguracaoSyst.anoEx(), ConfiguracaoSyst.CODIGO_CEDENTE, ConfiguracaoSyst.FICHA_RETIDO_FONTE, notaFiscal.getValorIss().toString(), mes, currentUser.getCcm().getCodISSGemmap().toString(), "0", "0", currentUser.getCcm().getCnpjCpf(), "rf", notaFiscal.getIdBanco().toString());

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

        titulo.setValor(BigDecimal.valueOf(notaFiscal.getValorIss()));
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

        boleto.addTextosExtras("txtRsCompetencia", "NFS-e nº " + notaFiscal.getNumeroNota());
        //boleto.addTextosExtras("txtRsAliquota", aliquota);
        boleto.addTextosExtras("txtRsMovimentoMensal", "R$ " + moeda.formata(notaFiscal.getValorIss()));
        boleto.addTextosExtras("txtRsIM", currentUser.getCcm().getIm());

        boleto.addTextosExtras("txtFcCompetencia", "NFS-e nº " + notaFiscal.getNumeroNota());
        //boleto.addTextosExtras("txtFcAliquota", aliquota);
        boleto.addTextosExtras("txtFcMovimentoMensal", "R$ " + moeda.formata(notaFiscal.getValorIss()));

        boleto.addTextosExtras("txtAnoEx", ConfiguracaoSyst.anoEx());
        boleto.addTextosExtras("txtFicha", ConfiguracaoSyst.FICHA_RETIDO_FONTE);
        boleto.addTextosExtras("txtNro", dadosBoletoWs.getNumeroDocumento());

        boleto.addTextosExtras("txtFcDescontoAbatimento", "R$ ");
        boleto.addTextosExtras("txtFcOutraDeducao", "R$ ");
        boleto.addTextosExtras("txtFcMoraMulta", "R$ ");
        boleto.addTextosExtras("txtFcOutroAcrescimo", "R$ ");
        boleto.addTextosExtras("txtFcValorCobrado", "R$ ");

        HistoricoNotaRrtidoFonte historicoDespesa = new HistoricoNotaRrtidoFonte();

        historicoDespesa.add(dataF.format(notaFiscal.getDataEmissao()), notaFiscal.getNomeRazaoPrestador() + " / " + notaFiscal.getCpfCnpjPrestador(), "R$ " + moeda.formata(notaFiscal.getValorBaseCalculo()), NotaUtil.fomrmataNumeroNota(notaFiscal.getNumeroNota()), notaFiscal.getAliquota().toString(), notaFiscal.getCidadePrestador(), "R$ " + moeda.formata(notaFiscal.getValorIss()));


        historicoDespesa.add("", "", "", "", "", "", "");
        historicoDespesa.add("", "", "", "", "", "", "TOTAL:  R$" + moeda.formata(notaFiscal.getValorIss()));

        boleto.addTextosExtras("txtRsHistoricoDespesaData", historicoDespesa.getDetalhamentoData());
        boleto.addTextosExtras("txtRsHistoricoDespesaDescricao", historicoDespesa.getDetalhamentoDescricao());
        boleto.addTextosExtras("txtRsHistoricoDespesaValor", historicoDespesa.getDetalhamentoValor());
        boleto.addTextosExtras("txtRsHistoricoDespesaNumeroNota", historicoDespesa.getDetalhamentoNumeroNota());
        boleto.addTextosExtras("txtRsHistoricoDespesaAliquota", historicoDespesa.getDetalhamentoAliquota());
        boleto.addTextosExtras("txtRsHistoricoDespesaCidade", historicoDespesa.getDetalhamentoCidade());
        boleto.addTextosExtras("txtRsHistoricoDespesaIss", historicoDespesa.getDetalhamentoValorIss());

        escolhePDF = "BoletoPersonalizadoDetalhesRF.pdf";


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

        boletoIssMap.setFicha(ConfiguracaoSyst.FICHA_RETIDO_FONTE);
        boletoIssMap.setSacado(currentUser.getCcm());
        boletoIssMap.setValorDocumento(notaFiscal.getValorIss());
        boletoIssMap.setAliquota(Moeda.convertPorcentagem(aliquota));
        boletoIssMap.setCampoLivre(campoLivreStr);

        boletoIssMap.setDataInicioCompetencia(notaFiscal.getDataLancamentoNotaTerceiro());
        boletoIssMap.setDataFimCompetencia(notaFiscal.getDataLancamentoNotaTerceiro());
        boletoIssMap.setDataDocumento(new Date());
        boletoIssMap.setMovimento(notaFiscal.getValorNota());
        boletoIssMap.setNossoNumero(nossoNumero);

        boletoIssMap.setNumeroDocumento(dadosBoletoWs.getNumeroDocumento());

        List<NFSE> notas = new ArrayList<NFSE>();
        notas.add(notaFiscal);
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
                for (NFSE nfseBoleto : notas) {
                    nfseBoleto.setBoletoPagamento(boletoIssMap);
                    new DAONFSE().salvar(nfseBoleto);
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

    public void novo() {

        prestador = new Ccm();
        limpaCampoPrestador();

        data = Calendar.getInstance(Locale.getDefault()).getTime();
        dataStr = dataf.format(data);
        horaLancamentoNota = horaf.format(data);
        codigoVerificacao = "";
        numeroNota = "";
        dataEmissaoNota = new Date();
        horaEmissaoNota = "";
        descricaoServico = "";
        codigoAtividade = "";
        atividade = "";
        aliquota = "0";
        valorDeducoes = "0,00";
        valorBaseCalculo = "0,00";
        valorIss = "0,00";
        valorNota = "0,00";
        enderecoValidacao = "";
        btLancaNota = false;
        btGuia = true;
        nfse = false;
        botaoCadastraPrestador = true;
        cnpjCpf = "";

    }

    public void buscaPrestador() {

        DAOCcm ccmDao = new DAOCcm();

        cnpjCpf = cnpjCpf.replaceAll("\\/", "").replaceAll("\\.", "").replaceAll("-", "");

        if (this.cnpjCpf.length() == 14) {
            if (!CnpjValidator.validaCNPJ(this.cnpjCpf)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "CNPJ/CPF Incorreto", ""));
                this.nfse = false;
                limpaCampoPrestador();
                return;
            }
        } else if (this.cnpjCpf.length() == 11) {
            if (!CpfValidator.validaCPF(this.cnpjCpf)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "CNPJ/CPF Incorreto", ""));
                this.nfse = false;
                limpaCampoPrestador();
                return;
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "CNPJ/CPF Incorreto", ""));
            this.nfse = false;
            limpaCampoPrestador();
            return;
        }
        try {
            this.prestador = ccmDao.buscaPorCnpjCpf(cnpjCpf);

            if (prestador != null) {
                this.botaoCadastraPrestador = true;
                this.nfse = true;
                this.nome = prestador.getNomeRazao();
                this.ieRg = prestador.getIeRg();
                this.inscricaoMunicipal = prestador.getIm();
                this.cep = prestador.getLogradouro().getCep();

                this.numero = prestador.getNumeroPredio().toString();
                this.complemento = prestador.getComplemento();
                this.cidade = prestador.getLogradouro().getBairro().getCidade().getNome();
                this.estado = prestador.getLogradouro().getBairro().getCidade().getEstado().getSigla();
                this.foneFax = prestador.getTelefone();
                this.email = prestador.getEmail();

                String rua = prestador.getRua();

                if (rua == null) {
                    this.endereco = prestador.getLogradouro().getNome();
                    this.bairro = prestador.getLogradouro().getBairro().getNome();
                } else {
                    this.endereco = prestador.getRua();
                    this.bairro = prestador.getBairro();
                }
            } else {
                this.botaoCadastraPrestador = false;
                this.nfse = true;
                limpaCampoPrestador();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "CNPJ/CPF Não Cadastrado.", "Preencha os dados e efetue o cadastro para emissão da Nota Fiscal Eletronica de Serviço"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao buscar prestador", e.getMessage()));
        }

    }

    public void cadastraPestador() {
        prestador = new Ccm();

        prestador.setCnpjCpf(this.cnpjCpf.replaceAll("\\/", "").replaceAll("\\.", "").replaceAll("-", ""));

        if (this.cnpjCpf.length() == 11) {
            prestador.setTipoPessoa(2);
        } else if (this.cnpjCpf.length() == 14) {
            prestador.setTipoPessoa(1);
        }

        if (!this.nome.equals("")) {
            prestador.setNomeRazao(this.nome);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro campos obrigatorios NOME", ""));
            return;
        }

        if (!this.ieRg.equals("")) {
            prestador.setIeRg(this.ieRg.replaceAll("\\.", "").replaceAll("\\-", ""));
        }
        if (!this.inscricaoMunicipal.equals("")) {
            prestador.setIm(this.inscricaoMunicipal.replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("\\/", ""));
        }

        if (!this.cep.equals("")) {
            DAOLogradouro daoLogradouro = new DAOLogradouro();
            Logradouro logra;
            try {
                logra = daoLogradouro.buscaPorNomeCep(this.cep.replace("-", ""));

                prestador.setLogradouro(logra);

                if (logra.getNome().equals("")) {
                    if (!this.endereco.equals("")) {
                        prestador.setRua(this.endereco);
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro campos obrigatorios ENDEREÇO", ""));
                        return;
                    }
                    if (!this.bairro.equals("")) {
                        prestador.setBairro(this.bairro);
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro campos obrigatorios BAIRRO", ""));
                        return;


                    }
                }

            } catch (Exception ex) {
                Logger.getLogger(NfseLancaRetidoFonteBaking.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro campos obrigatorios CEP", ""));
            return;
        }

        if (!this.numero.equals("")) {
            prestador.setNumeroPredio(numero);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro campos obrigatorios NUMERO", ""));
            return;
        }

        if (!this.complemento.equals("")) {
            prestador.setComplemento(this.complemento);
        }

        if (!this.foneFax.equals("")) {
            prestador.setTelefone(this.foneFax.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("-", "").replaceAll(" ", ""));
        }

        if (!this.email.equals("")) {

            Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
            Matcher m = p.matcher(this.email);

            boolean matchFound = m.matches();

            if (!matchFound) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "E-mail Incorreto"));
                return;
            } else {
                prestador.setEmail(this.email);
            }
        }
        prestador.setTipoTipoContribuinte("P");
        DAOCcm ccmDao = new DAOCcm();
        try {
            ccmDao.salvar(prestador);
            this.botaoCadastraPrestador = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cadastro de Prestador Efetuado com Sucesso", ""));
        } catch (SQLException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Errp ao Inserir Prestador " + ex.getMessage(), ""));
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

    private void limpaCampoPrestador() {

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

    public String getValorBaseCalculo() {
        return valorBaseCalculo;
    }

    public void setValorBaseCalculo(String valorBaseCalculo) {
        this.valorBaseCalculo = valorBaseCalculo;
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

    public String getNomeCidadeExecucao() {
        return nomeCidadeExecucao;
    }

    public void setNomeCidadeExecucao(String nomeCidadeExecucao) {
        this.nomeCidadeExecucao = nomeCidadeExecucao;
    }

    public boolean isBotaoCadastraPrestador() {
        return botaoCadastraPrestador;
    }

    public void setBotaoCadastraPrestador(boolean botaoCadastraPrestador) {
        this.botaoCadastraPrestador = botaoCadastraPrestador;
    }

    public String getAliquota() {
        return aliquota;
    }

    public void setAliquota(String aliquota) {
        this.aliquota = aliquota;
    }

    public String getAtividade() {
        return atividade;
    }

    public void setAtividade(String atividade) {
        this.atividade = atividade;
    }

    public String getCodigoAtividade() {
        return codigoAtividade;
    }

    public void setCodigoAtividade(String codigoAtividade) {
        this.codigoAtividade = codigoAtividade;
    }

    public String getCodigoVerificacao() {
        return codigoVerificacao;
    }

    public void setCodigoVerificacao(String codigoVerificacao) {
        this.codigoVerificacao = codigoVerificacao;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    public String getDescricaoServico() {
        return descricaoServico;
    }

    public void setDescricaoServico(String descricaoServico) {
        this.descricaoServico = descricaoServico;
    }

    public String getEnderecoValidacao() {
        return enderecoValidacao;
    }

    public void setEnderecoValidacao(String enderecoValidacao) {
        this.enderecoValidacao = enderecoValidacao;
    }

    public String getNumeroNota() {
        return numeroNota;
    }

    public void setNumeroNota(String numeroNota) {
        this.numeroNota = numeroNota;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Date getDataEmissaoNota() {
        return dataEmissaoNota;
    }

    public void setDataEmissaoNota(Date dataEmissaoNota) {
        this.dataEmissaoNota = dataEmissaoNota;
    }

    public Date getDataLancamentoNota() {
        return dataLancamentoNota;
    }

    public void setDataLancamentoNota(Date dataLancamentoNota) {
        this.dataLancamentoNota = dataLancamentoNota;
    }

    public String getHoraEmissaoNota() {
        return horaEmissaoNota;
    }

    public void setHoraEmissaoNota(String horaEmissaoNota) {
        this.horaEmissaoNota = horaEmissaoNota;
    }

    public String getHoraLancamentoNota() {
        return horaLancamentoNota;
    }

    public void setHoraLancamentoNota(String horaLancamentoNota) {
        this.horaLancamentoNota = horaLancamentoNota;
    }

    public String getDataStr() {
        return dataStr;
    }

    public void setDataStr(String dataStr) {
        this.dataStr = dataStr;
    }

    public Boolean getBtGuia() {
        return btGuia;
    }

    public void setBtGuia(Boolean btGuia) {
        this.btGuia = btGuia;
    }

    public Boolean getBtLancaNota() {
        return btLancaNota;
    }

    public void setBtLancaNota(Boolean btLancaNota) {
        this.btLancaNota = btLancaNota;
    }

    public String getDataEmicaoStr() {
        return dataEmicaoStr;
    }

    public void setDataEmicaoStr(String dataEmicaoStr) {
        this.dataEmicaoStr = dataEmicaoStr;
    }
}
