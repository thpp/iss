/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.backingbean;

import br.com.nfse.dao.DAOUsuario;
import br.com.nfse.email.CommonsMail;
import br.com.nfse.jsfuntil.CpfValidator;
import br.com.nfse.to.Ccm;
import br.com.nfse.to.Usuario;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.mail.EmailException;

/**
 *
 * @author Thiago Henrique
 */
@ManagedBean(name = "MBUsuario")
@RequestScoped()
public final class UsuarioBaking {

    private Integer id;
    private String nome;
    private String login;
    private String cargo;
    private String email;
    private String telefone;
    private Ccm ccm = null;
    private List<Usuario> listaUsuarios;
    private Usuario usuarioSelecionado = new Usuario();
    DAOUsuario daoUsuario = new DAOUsuario();
    private boolean salvarHabilitado = false;

    public boolean isSalvarHabilitado() {
        return salvarHabilitado;
    }

    public void setSalvarHabilitado(boolean salvarHabilitado) {
        this.salvarHabilitado = salvarHabilitado;
    }
    
    
    public UsuarioBaking() {
        init();
    }

    public void init() {

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();
        Usuario currentUser = (Usuario) session.getAttribute("usuario");
        ccm = new Ccm();
        ccm = currentUser.getCcm();
        listaUsuarios = new ArrayList<Usuario>();
        listaUsuarios = daoUsuario.listaUsuarios(ccm);
        if (listaUsuarios != null) {
            Collections.sort(listaUsuarios);
        }

    }

    public void addUsuario() {

        Usuario usuario = new Usuario();
        usuario.setId(this.id);
        usuario.setNome(this.nome);
        usuario.setCargo(this.cargo);
        usuario.setLogin(this.login);
        usuario.setEmail(this.email);
        usuario.setTelefone(this.telefone);
        usuario.setCcm(this.ccm);
        usuario.setTipoUsuario(2);
        usuario.setSenha("padrao123");
        usuario.setCadastroConfirmado("desativado.png");

        String cpf = usuario.getLogin();
        Pattern p = Pattern.compile("\\/|\\.|-");
        Matcher m = p.matcher(cpf);
        cpf = m.replaceAll("");
        
        usuario.setLogin(cpf);
        
        if (CpfValidator.validaCPF(cpf)) {

            if (usuario.getId() == null) {
                DateFormat dataf = new SimpleDateFormat("dd/mm/yyyy");
                Date data = Calendar.getInstance(Locale.getDefault()).getTime();
                String dataS = dataf.format(data);
                Date dateGrava;

                try {
                    dateGrava = new Date(dataf.parse(dataS).getTime());
                    usuario.setDataInscricao(dateGrava);
                } catch (ParseException ex) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao converter datas ", ""));
                }
            }

            if (!usuario.getNome().equals("") && !usuario.getCargo().equals("") && !usuario.getLogin().equals("") && !usuario.getEmail().equals("") && !usuario.getTelefone().equals("")) {
                if (usuario.getId() == null) {
                    if (this.daoUsuario.buscaPorCnpj(usuario.getLogin()) == null) {
                        salvar(usuario);
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao cadastrar usuario", "CPF ja cadastrado"));
                    }
                } else {
                    salvar(usuario);
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao cadastrar usuario", "Certifique - se que todos os campos estão preenchidos corretamente"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "VERIFIQUE O 'CPF', DADOS  INCORRETO "));
        }
        this.usuarioSelecionado = null;
        this.id = null;
    }

    private void salvar(Usuario usuario) {

        CommonsMail email;

        try {
            email = new CommonsMail();
            email.enviaEmailSimples(usuario.getEmail(), usuario.getNome(), ccm.getFantasia());
            daoUsuario.salvar(usuario);
            atualizaLista();
            novo();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario cadastrado ", "Aguardando validação"));
        } catch (SQLException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar usuario ", ""));
        } catch (EmailException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao enviar e-mail ", ""));
        } catch (MalformedURLException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao enviar e-mail ", ""));
        }

    }

    public void excluir() {

        try {

            Usuario usuario = new Usuario();

            usuario.setId(this.id);

            if (usuario.getId() != null) {
                usuario.setNome(this.nome);
                usuario.setCargo(this.cargo);
                usuario.setLogin(this.login);
                usuario.setEmail(this.email);
                usuario.setTelefone(this.telefone);
                usuario.setCcm(this.ccm);
                usuario.setSenha("Excluido");

                DAOUsuario dao = new DAOUsuario();

                dao.excluir(usuario);
                atualizaLista();
                novo();

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuario excluido com sucesso ", ""));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhum usuario foi selecionado, ", "selecione um usuario da lista"));
            }
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao excluir usuario ", ""));
        }
    }

    public void carregaUsuarioselecionado() {

        this.nome = usuarioSelecionado.getNome();
        this.login = usuarioSelecionado.getLogin();
        this.cargo = usuarioSelecionado.getCargo();
        this.telefone = usuarioSelecionado.getTelefone();
        this.email = usuarioSelecionado.getEmail();
        this.id = usuarioSelecionado.getId();
        this.salvarHabilitado = true;

        System.out.println("USUARIO SELECIONADO ESTA CARREGADO");
        System.out.println("CODIGO SELECIONADO ==>> " + usuarioSelecionado.getId());
    }

    private void atualizaLista() {

        DAOUsuario dao = new DAOUsuario();
        this.listaUsuarios = new ArrayList<Usuario>();
        System.out.println("Atualizando a lista de usuarios..............");
        this.listaUsuarios = dao.listaUsuarios(ccm);
        Collections.sort(this.listaUsuarios);

    }

    public void novo() {

        id = null;
        nome = null;
        login = null;
        cargo = null;
        email = null;
        telefone = null;
        ccm = null;
        usuarioSelecionado = new Usuario();
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public Usuario getUsuarioSelecionado() {
        return usuarioSelecionado;
    }

    public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
        this.usuarioSelecionado = usuarioSelecionado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
