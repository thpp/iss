/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.backingbean;

import br.com.nfse.to.Contato;
import br.com.nfse.to.GeneroAtividade;
import br.com.nfse.to.Servico;
import br.com.nfse.to.Socio;
import br.com.nfse.to.Usuario;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Thiago Henrique
 */
@ManagedBean(name = "MBPerfil")
@ViewScoped
public class PerfilBaking implements java.io.Serializable {

    private String razaoEmpresa;
    private String cnpjEmpresa;
    private String cepEmpresa ;
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
    private boolean confirmaSenhaHidden = false;
    private String testeSenha;
    private String nomeRepresentanteLegal;
    private String cpfRepresentanteegal;
    private ArrayList<Contato> contatoLista = new ArrayList<Contato>();
    private static Logger logger = Logger.getLogger(PerfilBaking.class.getName());
    private Contato contato = new Contato();
    private Contato contatoSelecionado = new Contato();
    private List<Servico> listaServico = new ArrayList<Servico>();
    private String inscrecaoMunicipal;
    private String inscricaoEstadual;
    private String cobranca;
    private String tipoTributacao;
    private GeneroAtividade genero = new GeneroAtividade();

    public PerfilBaking() {                
        init();
    }
    
    public void init(){
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();
        Usuario currentUser = (Usuario) session.getAttribute("usuario");

        System.out.print(currentUser.getCcm().getLogradouro().getCep());

        this.razaoEmpresa = currentUser.getCcm().getNomeRazao();
        this.emailEmpresa = currentUser.getCcm().getEmail();
        this.nomeRepresentanteLegal = currentUser.getCcm().getRepresentanteLegal();
        this.cepEmpresa = currentUser.getCcm().getLogradouro().getCep();
        this.estadoEmpresa = currentUser.getCcm().getLogradouro().getBairro().getCidade().getEstado().getNome();
        this.cidadeEmpresa = currentUser.getCcm().getLogradouro().getBairro().getCidade().getNome();
        this.numeroEmpresa = currentUser.getCcm().getNumeroPredio().toString();
        this.complementoEmpresa = currentUser.getCcm().getComplemento();
        this.telefoneEEmpresa = currentUser.getCcm().getTelefone();
        this.inscrecaoMunicipal = currentUser.getCcm().getIm();
        this.inscricaoEstadual = currentUser.getCcm().getIeRg(); 

        String rua = currentUser.getCcm().getRua();

        if(!currentUser.getCcm().getContatos().isEmpty()){
            this.contatoLista = (ArrayList<Contato>) currentUser.getCcm().getContatos();
        }

        if(rua==null){
            this.logradouroEmpresa = currentUser.getCcm().getLogradouro().getNome();
            this.bairroEmpresa = currentUser.getCcm().getLogradouro().getBairro().getNome();
        }else{
            this.logradouroEmpresa = currentUser.getCcm().getRua();
            this.bairroEmpresa = currentUser.getCcm().getBairro();
        }
        genero = currentUser.getCcm().getGeneroAtividade();
        listaServico = currentUser.getCcm().getListaServicosPrestados();
        
        if(currentUser.getCcm().isCobraca()){
            cobranca = "Tributável";
        }else{
            cobranca = "Isento";
        }
        if(currentUser.getCcm().isAutonomo()){
            tipoTributacao = "Autônomo";            
        }else if(currentUser.getCcm().isMei()){
            tipoTributacao = "Empreendedor Individual";
        }else if(currentUser.getCcm().isProfissionalLiberal()){
            tipoTributacao = "Profissional Liberal";
        }else if(currentUser.getCcm().isSimples()){
            tipoTributacao = "Simples Nacional";
        }else{
            tipoTributacao = "Normal";
        }
        
    }

    public void incluiContato() {

        String enteredEmail = contato.getEmail();
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher m = p.matcher(enteredEmail);

        boolean matchFound = m.matches();

        if (!matchFound) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "E-mail Incorreto"));
        } else {
            contatoLista.add(contato);
            contato = new Contato();
        }
    }

    public void deletaContatoSelecionado() {

        contatoLista.remove(contatoSelecionado);

//        if (sociosLista.isEmpty()) {
//            teste = true;
//        }
    }


    public void carregaValorSenha() {
        System.out.println("Entrou no metodo carrega valor senha!!");

        System.out.println("Senha " + senha);
        System.out.println("confima senha " + confirmaSenha);

        if (confirmaSenha != null) {
            System.out.println("chama validação");
            validaSenhas();
        }
    }

    public void validaSenhas() {

        System.out.println("confima senha " + confirmaSenha);

        System.out.println("Senha " + senha);

        if (senha != null) {
            if (!senha.equals(confirmaSenha)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ", "Senhas Incorretas"));
                System.out.println("Campo requerido ,true,");
                confirmaSenhaHidden = true;
            } else {
                System.out.println("Campo não requerido ,false,");
                confirmaSenhaHidden = false;
            }
        }
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

    public ArrayList<Contato> getContatoLista() {
        return contatoLista;
    }

    public void setContatoLista(ArrayList<Contato> contatoLista) {
        this.contatoLista = contatoLista;
    }

    public String getTelefoneEEmpresa() {
        return telefoneEEmpresa;
    }

    public void setTelefoneEEmpresa(String telefoneEEmpresa) {
        this.telefoneEEmpresa = telefoneEEmpresa;
    }

    public Contato getContato() {
        return contato;
    }

    public void setContato(Contato contato) {
        this.contato = contato;
    }

    public Contato getContatoSelecionado() {
        return contatoSelecionado;
    }

    public void setContatoSelecionado(Contato contatoSelecionado) {
        this.contatoSelecionado = contatoSelecionado;
    }

    public boolean isTeste() {
        return teste;
    }

    public void setTeste(boolean teste) {
        this.teste = teste;
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

    public List<Servico> getListaServico() {
        return listaServico;
    }

    public void setListaServico(List<Servico> listaServico) {
        this.listaServico = listaServico;
    }

    public String getCobranca() {
        return cobranca;
    }

    public void setCobranca(String cobranca) {
        this.cobranca = cobranca;
    }

    public String getInscrecaoMunicipal() {
        return inscrecaoMunicipal;
    }

    public void setInscrecaoMunicipal(String inscrecaoMunicipal) {
        this.inscrecaoMunicipal = inscrecaoMunicipal;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public String getTipoTributacao() {
        return tipoTributacao;
    }

    public void setTipoTributacao(String tipoTributacao) {
        this.tipoTributacao = tipoTributacao;
    }

    public GeneroAtividade getGenero() {
        return genero;
    }

    public void setGenero(GeneroAtividade genero) {
        this.genero = genero;
    }
}
