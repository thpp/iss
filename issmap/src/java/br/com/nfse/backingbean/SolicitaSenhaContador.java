/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.backingbean;

import br.com.nfse.dao.DAOCcm;
import br.com.nfse.dao.DAOLogradouro;
import br.com.nfse.jsfuntil.CnpjValidator;
import br.com.nfse.jsfuntil.CodigoVerificaNota;
import br.com.nfse.jsfuntil.CpfValidator;
import br.com.nfse.jsfuntil.JSFUtil;
import br.com.nfse.jsfuntil.UsuarioSalt;
import br.com.nfse.to.Ccm;
import br.com.nfse.to.Logradouro;
import br.com.nfse.to.Socio;
import br.com.nfse.to.Usuario;
import br.com.nfse.to.relatorio.DesbloqueioPFRelatorio;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
 * @author Thiago Henrique
 */
@ManagedBean(name = "MBSolicitaSenhaContador")
@ViewScoped
public class SolicitaSenhaContador implements java.io.Serializable {

    private String razaoEmpresa;
    private String cnpjEmpresa;
    private String rg;
    private String cepEmpresa;
    private String logradouroEmpresa;
    private String numeroEmpresa;
    private String bairroEmpresa;
    private String complementoEmpresa;
    private String cidadeEmpresa;
    private String estadoEmpresa;
    private String emailEmpresa;
    private String telefoneEEmpresa;
    private String senha;
    private String confirmaSenha;
    private String dicaSenha;
    private boolean teste = true;
    private ArrayList<SelectItem> listaEstadosPesquisar;
    private boolean confirmaSenhaHidden = false;
    private String testeSenha;
    private String nomeRepresentanteLegal;
    private String cpfRepresentanteegal;
    private ArrayList<Socio> sociosLista = new ArrayList<Socio>();
    private static Logger logger = Logger.getLogger(SolicitaSenhaContador.class.getName());
    private Socio socio = new Socio();
    private Socio socioSelecionado = new Socio();
    private Logradouro logradouro = null;
    private Ccm contribuinte = new Ccm();
    private String crcRepresentanteLegalEscritorio;
    
    // Contador Autonomo
    private String nomeContadorAutonomo;
    private String cpfAutonomo;
    private String rgAutonomo;
    private String cepAutonomo;
    private String logradouroAutonomo;
    private String numeroAutonomo;
    private String bairroAutonomo;
    private String complementoAutonomo;
    private String cidadeAutonomo;
    private String estadoAutonomo;
    private String emailAutonomo;
    private String telefoneAutonomo;
    private String senhaAtonomo;
    private String confirmaSenhaAutonomo;
    private String dicaSenhaAutonomo;
    // novos atributos para contador
    private String crc;
    private String inscricaoMunicipal;

    public SolicitaSenhaContador() {
    }

    public void incluiSocio() {

        if (CnpjValidator.validaCNPJ(cnpjEmpresa.replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("\\/", ""))) {
            if (CpfValidator.validaCPF(socio.getCpf().replaceAll("\\.", "").replaceAll("\\-", "")) && !socio.getNome().equals("")) {

                socio.setCpf(socio.getCpf().replaceAll("\\.", "").replaceAll("\\-", ""));

                sociosLista.add(socio);
                socio = new Socio();

                teste = false;


                for (Socio socio1 : sociosLista) {
                    System.out.println(socio1.getNome());
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "VERIFIQUE 'NOME SÓCIO' OU 'CPF', DADOS  INCORRETO "));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "CNPJ INVÁLIDO"));
        }
    }

    public void carregaSocioSelecionado() {
        System.out.println("Selecionado socio " + socioSelecionado.getNome());
        System.out.println("Selecionado socio cpf " + socioSelecionado.getCpf());

        sociosLista.remove(socioSelecionado);

        if (sociosLista.isEmpty()) {
            teste = true;
        }
    }

    public void verificaCadastro() {

        DAOCcm ccmDao = new DAOCcm();
        Ccm ccm = null;
        try {

            if (this.cnpjEmpresa == null) {
                ccm = ccmDao.buscaCadastroPorCnpjCpf(this.cpfAutonomo.replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("\\/", ""));
                //System.out.print("Entrou no metodo com cnpj empresa vazio");
            } else {
                ccm = ccmDao.buscaCadastroPorCnpjCpf(this.cnpjEmpresa.replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("\\/", ""));
                //System.out.print("Entrou no metodo com cnpj empresa Cheio");
            }

            if (ccm != null) {

                if (ccm.getTipoTipoContribuinte().equals("P")) {

                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "CPF/CNPJ JÁ CADASTRADO"));

                    contribuinte = new Ccm();
                    this.cepEmpresa = "";
                    this.numeroEmpresa = "";
                    this.complementoEmpresa = "";
                    this.cidadeEmpresa = "";
                    this.estadoEmpresa = "";
                    this.emailEmpresa = "";
                    this.telefoneEEmpresa = "";
                    this.logradouroEmpresa = "";
                    this.bairroEmpresa = "";

                } else {

                    contribuinte.setId(ccm.getId());
                    this.cepEmpresa = ccm.getLogradouro().getCep();
                    this.numeroEmpresa = ccm.getNumeroPredio().toString();
                    this.complementoEmpresa = ccm.getComplemento();
                    this.cidadeEmpresa = ccm.getLogradouro().getBairro().getCidade().getNome();
                    this.estadoEmpresa = ccm.getLogradouro().getBairro().getCidade().getEstado().getSigla();
                    this.emailEmpresa = ccm.getEmail();
                    this.telefoneEEmpresa = ccm.getTelefone();


                    if (ccm.getRua() == null) {
                        this.logradouroEmpresa = ccm.getLogradouro().getNome();
                        this.bairroEmpresa = ccm.getLogradouro().getBairro().getNome();
                    } else {
                        this.logradouroEmpresa = ccm.getRua();
                        this.bairroEmpresa = ccm.getBairro();
                    }

                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Seu CNPJ ja foi cadastrado anteriormente em alguma nota fiscal emitida a você, confirme os dados pré cadatsrados", ""));
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(CnpjValidator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void carregaValorSenha() {
//        System.out.println("Entrou no metodo carrega valor senha!!");
//
//        System.out.println("Senha " + senha);
//        System.out.println("confima senha " + confirmaSenha);

        if (senha == null) {

            if (confirmaSenhaAutonomo != null) {
                System.out.println("chama validação");
                validaSenhas();
            }

        } else {
            if (confirmaSenha != null) {
                System.out.println("chama validação");
                validaSenhas();
            }
        }
    }

    public void validaSenhas() {

        if (senha != null) {
            if (!senha.equals(confirmaSenha)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "Senhas Incorretas"));
                confirmaSenhaHidden = true;
            } else {
                confirmaSenhaHidden = false;
            }
        } else if (senhaAtonomo != null) {
            if (!senhaAtonomo.equals(confirmaSenhaAutonomo)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "Senhas Incorretas"));
                confirmaSenhaHidden = true;
            } else {
                confirmaSenhaHidden = false;
            }
        }
    }

    public String onFlowProcess(FlowEvent event) {
        logger.info("Current wizard step:" + event.getOldStep());
        logger.info("Next step:" + event.getNewStep());

        if (event.getNewStep().equals("localizacao") && event.getOldStep().equals("empresa")) {
            if (sociosLista.isEmpty()) {
                incluiSocio();
            } else if (!socio.getCpf().equals("") && !socio.getNome().equals("")) {
                incluiSocio();
            }
        }

        return event.getNewStep();
    }

    public String onFlowProcessAutonomo(FlowEvent event) {
        logger.info("Current wizard step:" + event.getOldStep());
        logger.info("Next step:" + event.getNewStep());

//        if (event.getNewStep().equals("localizacao") && event.getOldStep().equals("empresa")) {
//            if (sociosLista.isEmpty()) {
//                incluiSocio();
//            } else if (!socio.getCpf().equals("") && !socio.getNome().equals("")) {
//                incluiSocio();
//            }
//        }

        return event.getNewStep();
    }

    public void buscaCep() {

        DAOLogradouro daoLogradouro = new DAOLogradouro();
        try {

            if (this.cepEmpresa == null) {
                logradouro = daoLogradouro.buscaPorNomeCep(cepAutonomo.replace("-", ""));

                if (logradouro.getNome().equals("")) {

                    cidadeAutonomo = "";
                    estadoAutonomo = "";
                    logradouroAutonomo = "";
                    bairroAutonomo = "";

                    cidadeAutonomo = logradouro.getBairro().getCidade().getNome();
                    estadoAutonomo = logradouro.getBairro().getCidade().getEstado().getNome();
                } else {
                    cidadeAutonomo = logradouro.getBairro().getCidade().getNome();
                    estadoAutonomo = logradouro.getBairro().getCidade().getEstado().getNome();
                    logradouroAutonomo = logradouro.getNome();
                    bairroAutonomo = logradouro.getBairro().getNome();
                }
            } else {
                logradouro = daoLogradouro.buscaPorNomeCep(cepEmpresa.replace("-", ""));

                if (logradouro.getNome().equals("")) {

                    cidadeEmpresa = "";
                    estadoEmpresa = "";
                    logradouroEmpresa = "";
                    bairroEmpresa = "";

                    cidadeEmpresa = logradouro.getBairro().getCidade().getNome();
                    estadoEmpresa = logradouro.getBairro().getCidade().getEstado().getNome();
                } else {
                    cidadeEmpresa = logradouro.getBairro().getCidade().getNome();
                    estadoEmpresa = logradouro.getBairro().getCidade().getEstado().getNome();
                    logradouroEmpresa = logradouro.getNome();
                    bairroEmpresa = logradouro.getBairro().getNome();
                }
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "CEP INVÁLIDO"));
        }
    }

    public void formularioDesbloqueioEscritorioContabilidade() {

        String codigoVerificacao = null;
        Ccm ccmPrevio = null;

        try {

            ccmPrevio = new DAOCcm().buscaCadastroPorCnpjCpf(this.cnpjEmpresa.replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("\\/", ""));

            if (ccmPrevio == null || ccmPrevio.getTipoTipoContribuinte().equals("T")) {

                DateFormat diafa = new SimpleDateFormat("d");
                DateFormat mesfa = new SimpleDateFormat("MM");
                DateFormat anofa = new SimpleDateFormat("yy");
                DateFormat horaf = new SimpleDateFormat("HH");
                DateFormat minutof = new SimpleDateFormat("mm");
                DateFormat segundof = new SimpleDateFormat("ss");
                Date datas = Calendar.getInstance(Locale.getDefault()).getTime();

                codigoVerificacao = diafa.format(datas) + mesfa.format(datas) + anofa.format(datas) + horaf.format(datas) + minutof.format(datas) + segundof.format(datas);
                codigoVerificacao = CodigoVerificaNota.encode(Long.parseLong(codigoVerificacao));
                codigoVerificacao = codigoVerificacao.toUpperCase();

                try {
                    inserePedidoEscritorioContabilidade(codigoVerificacao);

                    DesbloqueioPFRelatorio desbloqueio = new DesbloqueioPFRelatorio();
                    List<DesbloqueioPFRelatorio> listaDesbloqueio = new ArrayList<DesbloqueioPFRelatorio>();

                    FacesContext facesContext = FacesContext.getCurrentInstance();
                    ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
                    String arquivo = scontext.getRealPath("/img/logo_pequeno_scruz.png");

                    desbloqueio.setCaminhoLogo(arquivo);

                    DateFormat diaf = new SimpleDateFormat("d");
                    DateFormat mesf = new SimpleDateFormat("MMMM");
                    DateFormat anof = new SimpleDateFormat("yyyy");
                    Date data = Calendar.getInstance(Locale.getDefault()).getTime();



                    desbloqueio.setCoderificacao(codigoVerificacao);
                    desbloqueio.setCnpj(this.cnpjEmpresa);
                    desbloqueio.setRazaoSocial(this.razaoEmpresa);
                    desbloqueio.setCpfRepLegal(this.cpfRepresentanteegal);
                    desbloqueio.setNomeRepLegal(this.nomeRepresentanteLegal);

                    desbloqueio.setData(diaf.format(data) + " de " + mesf.format(data) + " de " + anof.format(data));

                    listaDesbloqueio.add(desbloqueio);

                    JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listaDesbloqueio);

                    HashMap parameters = new HashMap();

                    try {

                        facesContext.responseComplete();
                        //  ServletContext scontext = (ServletContext) facesContext1.getExternalContext().getContext();
                        JasperPrint jasperPrint = JasperFillManager.fillReport(scontext.getRealPath("/WEB-INF/report/desbloqueia.jasper"), parameters, ds);
                        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();


                        JRPdfExporter exporter = new JRPdfExporter();
                        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos1);
                        exporter.exportReport();
                        byte[] bytes = baos1.toByteArray();

                        if (bytes != null && bytes.length > 0) {

                            HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
                            response.setContentType("application/pdf");
                            //gera arquivo .pdf
                            //JasperExportManager.exportReportToPdfFile(jasperPrint, "C:/Users/Usuario/Desktop/relatorio.pdf");
                            response.setHeader("Content-disposition", "inline; filename=\"desbloqueia1.pdf\"");
                            response.setContentLength(bytes.length);
                            ServletOutputStream outputStream = response.getOutputStream();
                            outputStream.write(bytes, 0, bytes.length);
                            outputStream.flush();
                            outputStream.close();
                        }

                        try {

                            HttpServletResponse resp = JSFUtil.getResponse();
                            resp.setContentType("Application/pdf");
                            resp.setHeader("Content-disposition", "attachment;filename=SOLICITAÇÃO DE DESBLOQUEIO DA SENHA WEB.pdf");
                            resp.setContentLength(baos1.size());
                            ServletOutputStream saida;
                            saida = resp.getOutputStream();
                            baos1.writeTo(saida);
                            saida.flush();
                            JSFUtil.responseComplete();
                        } catch (IOException ex) {
                            Logger.getLogger(SolicitaSenhaContador.class.getName()).log(Level.SEVERE, null, ex);
                        }


                    } catch (Exception e) {
                        System.out.print(e.getMessage());
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não foi possível efetuar o cadastro o CPF usado já consta em nossos registros. ", ""));
                return;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void inserePedidoEscritorioContabilidade(String codigoVerificacao) {

        DAOCcm ccmDao = new DAOCcm();

        contribuinte.setNomeRazao(this.razaoEmpresa);
        contribuinte.setCnpjCpf(this.cnpjEmpresa.replaceAll("\\/", "").replaceAll("\\.", "").replaceAll("-", ""));
        contribuinte.setRepresentanteLegal(this.nomeRepresentanteLegal);
        contribuinte.setCodigoLiberacaoSenhaWeb(codigoVerificacao);

        if (logradouro == null) {
            buscaCep();
        }

        if (logradouro.getNome().equals("")) {
            contribuinte.setRua(this.logradouroEmpresa);
            contribuinte.setBairro(this.bairroEmpresa);
        }
        contribuinte.setNumeroPredio(numeroEmpresa);
        contribuinte.setComplemento(this.complementoEmpresa);
        contribuinte.setTelefone(this.telefoneEEmpresa.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("-", "").replaceAll(" ", ""));
        contribuinte.setEmail(this.emailEmpresa);
        contribuinte.setLogradouro(this.logradouro);
        contribuinte.setTipoPessoa(1);
        contribuinte.setTipoTipoContribuinte("P");
        contribuinte.setBloqueio(true);
        contribuinte.setCrc(this.crc);
        contribuinte.setContadorProfissional(true);

        Usuario usuario = new Usuario();

        usuario.setLogin(this.cnpjEmpresa.replaceAll("\\/", "").replaceAll("\\.", "").replaceAll("-", ""));
        usuario.setSenha(this.senha);
        usuario.setDicaSenha(this.dicaSenha);
        usuario.setNome("Administrador");
        usuario.setTipoUsuario(1);

        DateFormat dataf = new SimpleDateFormat("dd/mm/yyyy");
        Date data = Calendar.getInstance(Locale.getDefault()).getTime();
        String dataS = dataf.format(data);
        usuario.setCcm(contribuinte);

        try {
            Date dateGrava = new Date(dataf.parse(dataS).getTime());
            usuario.setDataInscricao(dateGrava);
        } catch (ParseException ex) {
            System.out.println(ex.getStackTrace());
        }
        UsuarioSalt criptografia = new UsuarioSalt();

        List<Usuario> usuarios = new ArrayList<Usuario>();
        try {
            criptografia.criarLoginSenha(usuario);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SolicitaSenhaContador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SolicitaSenhaContador.class.getName()).log(Level.SEVERE, null, ex);
        }
        usuarios.add(usuario);

        contribuinte.setUsuarios(usuarios);

        for (Socio sociol : sociosLista) {
            sociol.setCcm(contribuinte);
        }
        contribuinte.setNotificacaoGemmap(false);
        contribuinte.setSocios(this.sociosLista);

        try {
            ccmDao.salvar(contribuinte);
        } catch (SQLException e) {
            System.out.println(e.getNextException());
        }
    }

    public void formularioDesbloqueioContadorAutonomo() {

        String codigoVerificacao = null;
        Ccm ccmPrevio = null;

        try {

            ccmPrevio = new DAOCcm().buscaCadastroPorCnpjCpf(this.cpfAutonomo.replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("\\/", ""));

            if (ccmPrevio == null || ccmPrevio.getTipoTipoContribuinte().equals("T")) {

                DateFormat diafa = new SimpleDateFormat("d");
                DateFormat mesfa = new SimpleDateFormat("MM");
                DateFormat anofa = new SimpleDateFormat("yy");
                DateFormat horaf = new SimpleDateFormat("HH");
                DateFormat minutof = new SimpleDateFormat("mm");
                DateFormat segundof = new SimpleDateFormat("ss");
                Date datas = Calendar.getInstance(Locale.getDefault()).getTime();

                codigoVerificacao = diafa.format(datas) + mesfa.format(datas) + anofa.format(datas) + horaf.format(datas) + minutof.format(datas) + segundof.format(datas);
                codigoVerificacao = CodigoVerificaNota.encode(Long.parseLong(codigoVerificacao));
                codigoVerificacao = codigoVerificacao.toUpperCase();

                try {
                    inserePedidoContadorAutonomo(codigoVerificacao);

                    DesbloqueioPFRelatorio desbloqueio = new DesbloqueioPFRelatorio();
                    List<DesbloqueioPFRelatorio> listaDesbloqueio = new ArrayList<DesbloqueioPFRelatorio>();

                    FacesContext facesContext = FacesContext.getCurrentInstance();
                    ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
                    String arquivo = scontext.getRealPath("/img/logo_pequeno_scruz.png");

                    desbloqueio.setCaminhoLogo(arquivo);

                    DateFormat diaf = new SimpleDateFormat("d");
                    DateFormat mesf = new SimpleDateFormat("MMMM");
                    DateFormat anof = new SimpleDateFormat("yyyy");
                    Date data = Calendar.getInstance(Locale.getDefault()).getTime();

                    desbloqueio.setCoderificacao(codigoVerificacao);
                    desbloqueio.setCpfRepLegal(this.cpfAutonomo);
                    desbloqueio.setNomeRepLegal(this.nomeContadorAutonomo);
                    desbloqueio.setData(diaf.format(data) + " de " + mesf.format(data) + " de " + anof.format(data));

                    listaDesbloqueio.add(desbloqueio);

                    JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listaDesbloqueio);

                    HashMap parameters = new HashMap();

                    try {

                        facesContext.responseComplete();
                        //  ServletContext scontext = (ServletContext) facesContext1.getExternalContext().getContext();
                        JasperPrint jasperPrint = JasperFillManager.fillReport(scontext.getRealPath("/WEB-INF/report/desbloqueiaPF.jasper"), parameters, ds);
                        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();


                        JRPdfExporter exporter = new JRPdfExporter();
                        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos1);
                        exporter.exportReport();


                        byte[] bytes = baos1.toByteArray();

                        if (bytes != null && bytes.length > 0) {

                            HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
                            response.setContentType("application/pdf");
                            //gera arquivo .pdf
                            //JasperExportManager.exportReportToPdfFile(jasperPrint, "C:/Users/Usuario/Desktop/relatorio.pdf");
                            response.setHeader("Content-disposition", "inline; filename=\"desbloqueia1.pdf\"");
                            response.setContentLength(bytes.length);
                            ServletOutputStream outputStream = response.getOutputStream();
                            outputStream.write(bytes, 0, bytes.length);
                            outputStream.flush();
                            outputStream.close();
                        }

                        try {

                            HttpServletResponse resp = JSFUtil.getResponse();
                            resp.setContentType("Application/pdf");
                            resp.setHeader("Content-disposition", "attachment;filename=SOLICITAÇÃO DE DESBLOQUEIO DA SENHA WEB.pdf");
                            resp.setContentLength(baos1.size());
                            ServletOutputStream saida;
                            saida = resp.getOutputStream();
                            baos1.writeTo(saida);
                            saida.flush();
                            JSFUtil.responseComplete();
                        } catch (IOException ex) {
                            Logger.getLogger(SolicitaSenha.class.getName()).log(Level.SEVERE, null, ex);
                        }


                    } catch (Exception e) {
                        System.out.print(e.getMessage());
                        return;
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return;
                }

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não foi possível efetuar o cadastro o CPF usado já consta em nossos registros. ", ""));
                return;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    private void inserePedidoContadorAutonomo(String codigoVerificacao) throws Exception {


        DAOCcm ccmDao = new DAOCcm();

        contribuinte.setNomeRazao(this.nomeContadorAutonomo);
        contribuinte.setCnpjCpf(this.cpfAutonomo.replaceAll("\\/", "").replaceAll("\\.", "").replaceAll("-", ""));
        contribuinte.setCodigoLiberacaoSenhaWeb(codigoVerificacao);

        if (logradouro == null) {
            buscaCep();
        }

        if (logradouro.getNome().equals("")) {
            contribuinte.setRua(this.logradouroAutonomo);
            contribuinte.setBairro(this.bairroAutonomo);
        }
        contribuinte.setNumeroPredio(numeroAutonomo);
        contribuinte.setComplemento(this.complementoAutonomo);
        contribuinte.setTelefone(this.telefoneAutonomo.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("-", "").replaceAll(" ", ""));
        contribuinte.setEmail(this.emailAutonomo);
        contribuinte.setLogradouro(this.logradouro);
        contribuinte.setIm(this.inscricaoMunicipal);
        contribuinte.setTipoPessoa(2);
        contribuinte.setTipoTipoContribuinte("P");
        contribuinte.setCrc(this.crc);
        contribuinte.setContadorProfissional(true);

        Usuario usuario = new Usuario();

        usuario.setLogin(this.cpfAutonomo.replaceAll("\\/", "").replaceAll("\\.", "").replaceAll("-", ""));
        usuario.setSenha(this.senhaAtonomo);
        usuario.setDicaSenha(this.dicaSenhaAutonomo);
        usuario.setNome("Administrador");
        usuario.setTipoUsuario(1);

        DateFormat dataf = new SimpleDateFormat("dd/mm/yyyy");
        Date data = Calendar.getInstance(Locale.getDefault()).getTime();
        String dataS = dataf.format(data);
        usuario.setCcm(contribuinte);

        try {
            Date dateGrava = new Date(dataf.parse(dataS).getTime());
            usuario.setDataInscricao(dateGrava);
        } catch (ParseException ex) {
            System.out.println(ex.getStackTrace());
        }
        UsuarioSalt criptografia = new UsuarioSalt();

        List<Usuario> usuarios = new ArrayList<Usuario>();
        try {
            criptografia.criarLoginSenha(usuario);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SolicitaSenhaFisico.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SolicitaSenhaFisico.class.getName()).log(Level.SEVERE, null, ex);
        }
        usuarios.add(usuario);

        contribuinte.setUsuarios(usuarios);

        ccmDao.salvar(contribuinte);

    }

    public void onRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Car Selected", ((Socio) event.getObject()).getCpf().toString());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowUnselect(UnselectEvent event) {
        FacesMessage msg = new FacesMessage("Car Unselected", ((Socio) event.getObject()).getCpf().toString());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public String getBairroEmpresa() {
        return bairroEmpresa;
    }

    public void setBairroEmpresa(String bairroEmpresa) {
        this.bairroEmpresa = bairroEmpresa;
    }

    public String getCepEmpresa() {
        return cepEmpresa;
    }

    public void setCepEmpresa(String cepEmpresa) {
        this.cepEmpresa = cepEmpresa;
    }

    public String getCidadeEmpresa() {
        return cidadeEmpresa;
    }

    public void setCidadeEmpresa(String cidadeEmpresa) {
        this.cidadeEmpresa = cidadeEmpresa;
    }

    public String getCnpjEmpresa() {
        return cnpjEmpresa;
    }

    public void setCnpjEmpresa(String cnpjEmpresa) {
        this.cnpjEmpresa = cnpjEmpresa;
    }

    public String getComplementoEmpresa() {
        return complementoEmpresa;
    }

    public void setComplementoEmpresa(String complementoEmpresa) {
        this.complementoEmpresa = complementoEmpresa;
    }

    public String getConfirmaSenha() {
        return confirmaSenha;
    }

    public void setConfirmaSenha(String confirmaSenha) {
        this.confirmaSenha = confirmaSenha;
    }

    public String getDicaSenha() {
        return dicaSenha;
    }

    public void setDicaSenha(String dicaSenha) {
        this.dicaSenha = dicaSenha;
    }

    public String getEmailEmpresa() {
        return emailEmpresa;
    }

    public void setEmailEmpresa(String emailEmpresa) {
        this.emailEmpresa = emailEmpresa;
    }

    public String getEstadoEmpresa() {
        return estadoEmpresa;
    }

    public void setEstadoEmpresa(String estadoEmpresa) {
        this.estadoEmpresa = estadoEmpresa;
    }

    public String getLogradouroEmpresa() {
        return logradouroEmpresa;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public void setLogradouroEmpresa(String logradouroEmpresa) {
        this.logradouroEmpresa = logradouroEmpresa;
    }

    public String getNumeroEmpresa() {
        return numeroEmpresa;
    }

    public void setNumeroEmpresa(String numeroEmpresa) {
        this.numeroEmpresa = numeroEmpresa;
    }

    public String getRazaoEmpresa() {
        return razaoEmpresa;
    }

    public void setRazaoEmpresa(String razaoEmpresa) {
        this.razaoEmpresa = razaoEmpresa;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public ArrayList<Socio> getSociosLista() {
        return sociosLista;
    }

    public void setSociosLista(ArrayList<Socio> sociosLista) {
        this.sociosLista = sociosLista;
    }

    public String getTelefoneEEmpresa() {
        return telefoneEEmpresa;
    }

    public void setTelefoneEEmpresa(String telefoneEEmpresa) {
        this.telefoneEEmpresa = telefoneEEmpresa;
    }

    public Socio getSocioSelecionado() {
        return socioSelecionado;
    }

    public void setSocioSelecionado(Socio socioSelecionado) {
        this.socioSelecionado = socioSelecionado;

    }

    public boolean isTeste() {
        return teste;
    }

    public void setTeste(boolean teste) {
        this.teste = teste;
    }

    public ArrayList<SelectItem> getListaEstadosPesquisar() {
        return listaEstadosPesquisar;
    }

    public void setListaEstadosPesquisar(ArrayList<SelectItem> listaEstadosPesquisar) {
        this.listaEstadosPesquisar = listaEstadosPesquisar;
    }

    public boolean isConfirmaSenhaHidden() {
        return confirmaSenhaHidden;
    }

    public void setConfirmaSenhaHidden(boolean confirmaSenhaHidden) {
        this.confirmaSenhaHidden = confirmaSenhaHidden;
    }

    public String getTesteSenha() {
        return testeSenha;
    }

    public void setTesteSenha(String testeSenha) {
        this.testeSenha = testeSenha;
    }

    public String getCpfRepresentanteegal() {
        return cpfRepresentanteegal;
    }

    public void setCpfRepresentanteegal(String cpfRepresentanteegal) {
        this.cpfRepresentanteegal = cpfRepresentanteegal;
    }

    public String getNomeRepresentanteLegal() {
        return nomeRepresentanteLegal;
    }

    public void setNomeRepresentanteLegal(String nomeRepresentanteLegal) {
        this.nomeRepresentanteLegal = nomeRepresentanteLegal;
    }

    public Socio getSocio() {
        return socio;
    }

    public void setSocio(Socio socio) {
        this.socio = socio;
    }

    public String getInscricaoMunicipal() {
        return inscricaoMunicipal;
    }

    public void setInscricaoMunicipal(String inscricaoMunicipal) {
        this.inscricaoMunicipal = inscricaoMunicipal;
    }

    public String getCrc() {
        return crc;
    }

    public void setCrc(String crc) {
        this.crc = crc;
    }

    public String getBairroAutonomo() {
        return bairroAutonomo;
    }

    public void setBairroAutonomo(String bairroAutonomo) {
        this.bairroAutonomo = bairroAutonomo;
    }

    public String getCepAutonomo() {
        return cepAutonomo;
    }

    public void setCepAutonomo(String cepAutonomo) {
        this.cepAutonomo = cepAutonomo;
    }

    public String getCidadeAutonomo() {
        return cidadeAutonomo;
    }

    public void setCidadeAutonomo(String cidadeAutonomo) {
        this.cidadeAutonomo = cidadeAutonomo;
    }

    public String getComplementoAutonomo() {
        return complementoAutonomo;
    }

    public void setComplementoAutonomo(String complementoAutonomo) {
        this.complementoAutonomo = complementoAutonomo;
    }

    public String getConfirmaSenhaAutonomo() {
        return confirmaSenhaAutonomo;
    }

    public void setConfirmaSenhaAutonomo(String confirmaSenhaAutonomo) {
        this.confirmaSenhaAutonomo = confirmaSenhaAutonomo;
    }

    public String getCpfAutonomo() {
        return cpfAutonomo;
    }

    public void setCpfAutonomo(String cpfAutonomo) {
        this.cpfAutonomo = cpfAutonomo;
    }

    public String getDicaSenhaAutonomo() {
        return dicaSenhaAutonomo;
    }

    public void setDicaSenhaAutonomo(String dicaSenhaAutonomo) {
        this.dicaSenhaAutonomo = dicaSenhaAutonomo;
    }

    public String getEmailAutonomo() {
        return emailAutonomo;
    }

    public void setEmailAutonomo(String emailAutonomo) {
        this.emailAutonomo = emailAutonomo;
    }

    public String getEstadoAutonomo() {
        return estadoAutonomo;
    }

    public void setEstadoAutonomo(String estadoAutonomo) {
        this.estadoAutonomo = estadoAutonomo;
    }

    public String getLogradouroAutonomo() {
        return logradouroAutonomo;
    }

    public void setLogradouroAutonomo(String logradouroAutonomo) {
        this.logradouroAutonomo = logradouroAutonomo;
    }

    public String getNomeContadorAutonomo() {
        return nomeContadorAutonomo;
    }

    public void setNomeContadorAutonomo(String nomeContadorAutonomo) {
        this.nomeContadorAutonomo = nomeContadorAutonomo;
    }

    public String getNumeroAutonomo() {
        return numeroAutonomo;
    }

    public void setNumeroAutonomo(String numeroAutonomo) {
        this.numeroAutonomo = numeroAutonomo;
    }

    public String getRgAutonomo() {
        return rgAutonomo;
    }

    public void setRgAutonomo(String rgAutonomo) {
        this.rgAutonomo = rgAutonomo;
    }

    public String getSenhaAtonomo() {
        return senhaAtonomo;
    }

    public void setSenhaAtonomo(String senhaAtonomo) {
        this.senhaAtonomo = senhaAtonomo;
    }

    public String getTelefoneAutonomo() {
        return telefoneAutonomo;
    }

    public void setTelefoneAutonomo(String telefoneAutonomo) {
        this.telefoneAutonomo = telefoneAutonomo;
    }

    public String getCrcRepresentanteLegalEscritorio() {
        return crcRepresentanteLegalEscritorio;
    }

    public void setCrcRepresentanteLegalEscritorio(String crcRepresentanteLegalEscritorio) {
        this.crcRepresentanteLegalEscritorio = crcRepresentanteLegalEscritorio;
    }
}
