/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.backingbean;

import br.com.nfse.dao.DAOAguardaEnvio;
import br.com.nfse.dao.DAOCcm;
import br.com.nfse.dao.DAOGeneroAtividade;
import br.com.nfse.dao.DAOHistoricoBloqueio;
import br.com.nfse.jsfuntil.UsuarioSalt;
import br.com.nfse.to.AguardandoEnvio;
import br.com.nfse.to.Ccm;
import br.com.nfse.to.HistoricoBloqueio;
import br.com.nfse.to.Usuario;
import br.com.nfse.ws.beans.Mobiliario;
import br.com.nfse.ws.beans.RespNotaEnvio;
import br.com.nfse.ws.cliente.BuscaMobiliarioGemmap;
import br.com.nfse.ws.cliente.SolicitaBuscaNFSE;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Thiago Henrique
 */
@ManagedBean(name = "MBlogin")
@ViewScoped
public class LoginBaking implements java.io.Serializable {

    private String cnpjCpf;
    private String senha;
    private UsuarioSalt salt = new UsuarioSalt();
    private Usuario usuario = null;
    private String nomeUsuario = null;
    private String nomeEmpresa = null;
    private String tipoTributacao = null;

    public String login() {

        Pattern p = Pattern.compile("\\/|\\.|-");
        Matcher m = p.matcher(cnpjCpf);
        cnpjCpf = m.replaceAll("");

        String retorno = null;

        try {
            // Busca usuario no banco da nota
            this.usuario = this.salt.autenticaUsuario(this.cnpjCpf, this.senha);
        } catch (SQLException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "Usuario ou Senha Incorreto"));
            //System.out.println(ex.getNextException());
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UsuarioSalt.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Verifica se o ususario esta cadastrado
        if (usuario != null) {
            //System.out.println("Autenticado");

            //Verifica se os dados adicionais do mobiliario ja foi importado do gemmap
            if (usuario.getCcm().getDataLiberacaoGemmap() == null) {
                //Busca os dados no gemmap pelo WS
                Ccm ccmGemmap = buscaDadosGemmap();

                if (ccmGemmap == null) {
                    return retorno = "loginAguardandoDesbloqueio";
                } else {
                    usuario.setCcm(ccmGemmap);

                    try {

                        new DAOCcm().salvarMerge(usuario.getCcm());
                        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                        HttpSession session = request.getSession();
                        session.setAttribute("usuario", this.usuario);

                        retorno = "loginOk";

                    } catch (SQLException ex) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "Cadastramento contem erros"));
                    }
                }
            } else {

                //busca mobiliario no gemmap
                BuscaMobiliarioGemmap wsBuscaMobiliario = new BuscaMobiliarioGemmap();
                Mobiliario mobiliario = null;

                if (this.usuario.getTipoUsuario() == 1) {
                    mobiliario = wsBuscaMobiliario.retornaMobiliario(this.cnpjCpf);
                } else if (this.usuario.getTipoUsuario() == 2) {
                    mobiliario = wsBuscaMobiliario.retornaMobiliario(this.usuario.getCcm().getCnpjCpf());
                }

                if (mobiliario == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "Comunicação com a Prefeitura."));
                } else {
                    //verifica se o mobiliario foio bloquiado pela prefeitura
                    if (mobiliario.getFlagIssEletronico().equals("N")) {

                        retorno = "loginBloqueado";

                        ////verifica se o mobiliario não foio bloquiado pela prefeitura 
                    } else if (mobiliario.getFlagIssEletronico().equals("S")) {
                        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                        HttpSession session = request.getSession();
                        session.setAttribute("usuario", this.usuario);

                        retorno = "loginOk";
                    }
                }
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "Usuario ou Senha Incorreto"));
        }
        //verificaNotaEspera();
        // System.out.println("Retorno do metodo >>>>>>> " + retorno);
        return retorno;
    }

    private void verificaNotaEspera() {

        //System.out.println("Verifica se existe notas a serem enviadas.........");
        DAOAguardaEnvio daoAguardaEnvio = new DAOAguardaEnvio();
        List<AguardandoEnvio> lista = new ArrayList<AguardandoEnvio>();
        lista = daoAguardaEnvio.bustaNotasEmEspera(cnpjCpf);
        SolicitaBuscaNFSE solicitaBusca = new SolicitaBuscaNFSE();
        if (lista != null) {
            //System.out.println("Notas a serem enviadas");
            for (AguardandoEnvio aguarda : lista) {
                RespNotaEnvio resposta = solicitaBusca.retornaSolicitaBusca(aguarda.getNotaEspera().getCpfCnpjPrestador(), aguarda.getNotaEspera().getCogigoVerifica());
                if (resposta.getResposta()) {
                    try {
                        daoAguardaEnvio.excluir(aguarda);
                        //System.out.println("Enviado e excluido!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    } catch (Exception ex) {
                        //System.out.println();
                        Logger.getLogger(LoginBaking.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //System.out.println(aguarda.getNotaEspera().getCpfCnpjTomador());
            }
        } else {
            //System.out.println("Nenhuma nota");
        }

    }

    private Ccm buscaDadosGemmap() {
        BuscaMobiliarioGemmap wsBuscaMobiliario = new BuscaMobiliarioGemmap();

        Mobiliario mobiliario = null;

        if (usuario.getTipoUsuario() == 1) {
            mobiliario = wsBuscaMobiliario.retornaMobiliario(this.cnpjCpf);
        } else {
            mobiliario = wsBuscaMobiliario.retornaMobiliario(usuario.getCcm().getCnpjCpf());
        }

        Ccm ccm = new Ccm();
        ccm = usuario.getCcm();

        if (mobiliario.getNome() == null) {
            return null;
        }

//        if(mobiliario.getCodEventual() != null){
//            System.out.println("Data desbloqueio " +mobiliario.getDataDesbloqueio());
//            System.out.println("codISS "+ mobiliario.getCodIss());
//            System.out.println("Codigo eventual "+mobiliario.getCodEventual());
//            return null;
//        }

        if (mobiliario.getFlagIssEletronico().equals("S")) {
            ccm.setBloqueio(false);
        } else if (mobiliario.getFlagIssEletronico().equals("N")) {
            ccm.setBloqueio(true);
        }

        if (!ccm.isBloqueio()) {
            if (mobiliario.getFlagPessoa().equals("J")) {
                ccm.setTipoPessoa(1);
            } else if (mobiliario.getFlagPessoa().equals("M")) {
                ccm.setTipoPessoa(2);
            }
            ccm.setIm(mobiliario.getInscricaoMunicipal());
            ccm.setPesNro(Integer.parseInt(mobiliario.getNumeroPessoa()));
            ccm.setCodISSGemmap(Integer.parseInt(mobiliario.getCodIss()));
            Date dataAbertura = null;
            Date dataLiberacao = null;

            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            try {
                dataAbertura = (java.util.Date) formatter.parse(mobiliario.getDataAbertura());
                dataLiberacao = (java.util.Date) formatter.parse(mobiliario.getDataLiberacao());
            } catch (ParseException ex) {
                Logger.getLogger(LoginBaking.class.getName()).log(Level.SEVERE, null, ex);
            }

            ccm.setDataLiberacaoGemmap(dataLiberacao);
            ccm.setDataAbertura(dataAbertura);
            ccm.setFantasia(mobiliario.getFantasia());
            ccm.setNomeRazao(mobiliario.getNome());
            ccm.setServicosPrestados(mobiliario.getServicos() + ";");
            ccm.setGeneroAtividade(new DAOGeneroAtividade().buscarProCodigo(Integer.parseInt(mobiliario.getNumeroGenero())));

            //reseta os valores 
            ccm.setAutonomo(false);
            ccm.setSimples(false);
            ccm.setMei(false);
            ccm.setProfissionalLiberal(false);

            if (mobiliario.getFlagSimples().equals("S")) {
                ccm.setSimples(true);

                if (mobiliario.getDataSimples() != null) {
                    Date dataSimples = null;
                    try {
                        dataSimples = (java.util.Date) formatter.parse(mobiliario.getDataSimples());
                        ccm.setDataMei(dataSimples);
                    } catch (ParseException ex) {
                        Logger.getLogger(LoginBaking.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            } else if (mobiliario.getFlagProfissionalLiberal().equals("S")) {
                ccm.setProfissionalLiberal(true);

                if (mobiliario.getDocumentoProfissionalLiberal() != null) {
                    ccm.setRegistroPl(mobiliario.getDocumentoProfissionalLiberal());
                }

            } else if (mobiliario.getFlagMEI().equals("S")) {
                ccm.setMei(true);

                if (mobiliario.getDataMEI() != null) {
                    Date dataMei = null;
                    try {
                        dataMei = (java.util.Date) formatter.parse(mobiliario.getDataMEI());
                        ccm.setDataMei(dataMei);
                    } catch (ParseException ex) {
                        Logger.getLogger(LoginBaking.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            } else if (mobiliario.getFlagAutonomo().equals("S")) {
                ccm.setAutonomo(true);
            }

            if (mobiliario.getFlagCobranca().equals("T")) {
                ccm.setCobraca(true);
            } else if (mobiliario.getFlagCobranca().equals("N")) {
                ccm.setCobraca(false);
            }

            if (mobiliario.getIeRg() != null) {
                ccm.setIeRg(mobiliario.getIeRg().replaceAll("\\.", "").replaceAll("-", ""));
            }
            if (mobiliario.getCodEventual() != null) {
                ccm.setCodigoEventualGemmap(Integer.parseInt(mobiliario.getCodEventual()));
            }

        } else {
            ccm = null;
        }
        return ccm;

    }

    public String logout() {

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();
        session.removeAttribute("usuario");
        this.usuario = new Usuario();
        this.cnpjCpf = null;
        this.senha = null;
        this.nomeUsuario = null;

        return "redirect";

    }

    public void encerraSessao() {
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) ctx.getExternalContext().getSession(false);
            session.setAttribute("usuario", null);
            ctx.getExternalContext().redirect(ctx.getExternalContext().getRequestContextPath() + "/login.xhtml");
            session.invalidate();
        } catch (Exception e) {
        }
    }

    public String getCnpjCpf() {
        return cnpjCpf;
    }

    public void setCnpjCpf(String cnpjCpf) {
        this.cnpjCpf = cnpjCpf;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNomeUsuario() {

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();
        Usuario currentUser = (Usuario) session.getAttribute("usuario");

        this.nomeUsuario = currentUser.getNome();

        return nomeUsuario;
    }

    public String getNomeEmpresa() {

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();
        Usuario currentUser = (Usuario) session.getAttribute("usuario");

        this.nomeEmpresa = currentUser.getCcm().getNomeRazao() + " " + mascara(currentUser.getLogin());

        return nomeEmpresa;
    }

    public String mascara(String texto) {

        if (texto.length() > 11) {
            StringBuilder textoMascara = new StringBuilder(texto);
            textoMascara.insert(2, ".");
            textoMascara.insert(6, ".");
            textoMascara.insert(10, "/");
            textoMascara.insert(15, "-");
            texto = textoMascara.toString();
        } else {
            StringBuilder textoMascara = new StringBuilder(texto);
            textoMascara.insert(3, ".");
            textoMascara.insert(7, ".");
            textoMascara.insert(11, "-");
            texto = textoMascara.toString();
        }

        return texto;
    }

    public String numeroValidacao() {

        String valores = "abcdefghijkmnopqrstuvwxyz0123456789";
        Random random = new Random(System.currentTimeMillis());
        int i = 0;
        String validador = "";

        while (i <= 7) {
            int num = random.nextInt() % 35;
            if (num < 0) {
                num *= -1;
            }
            String tmp = valores.substring(num, num + 1);
            if (num % 2 == 0) {
                tmp = tmp.toUpperCase();
            }
            validador = validador + tmp;
            i++;
        }

        return validador;

    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getTipoTributacao() {

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();
        Usuario currentUser = (Usuario) session.getAttribute("usuario");

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

        if (currentUser.getCcm().getCodigoEventualGemmap() != null) {
            tipoTributacao = "EVENTUAL";
        }

        return tipoTributacao;
    }

    public void setTipoTributacao(String tipoTributacao) {
        this.tipoTributacao = tipoTributacao;
    }
}
