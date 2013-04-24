/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.nfse.to;

import br.com.nfse.dao.DAOServico;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 *
 * @author Thiago Henrique
 */
@Entity
public class Ccm implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ccm_lancamentos_sequence_generator")
    @SequenceGenerator(name = "ccm_lancamentos_sequence_generator", sequenceName = "ccm_lancamentos_sequence_id", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    private Logradouro logradouro;

    @Column(name="nome_razao", length=100)
    private String nomeRazao;
    @Column(name="tipo_pessoa", length=100)
    private int tipoPessoa;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    private Ccm contador;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    private GeneroAtividade generoAtividade;
    
    @Column(length=100)
    private String fantasia;
    @Column(name="cnpj_cpf")
    private String cnpjCpf;
    @Column(name="ie_rg")
    private String ieRg;
    @Column(name="numero_predio", length=30)
    private String numeroPredio;
    @Column(length=100)
    private String complemento;
    @Column(name="site", length=45)
    private String site;
    @Column(name="email", length=45)
    private String email;
    @Column(name="inscricao_municipal", length=14)
    private String im;
    @Column(name="flg_simples",length=1)
    private boolean simples;
    @Column(name="flg_pl",length=1)
    private boolean profissionalLiberal;
    @Column(name="flg_mei",length=1)
    private boolean mei;
    @Column(name="flg_autonomo",length=1)
    private boolean  autonomo;
    @Column(name="flg_registro_pl",length=1)
    private int tipoRegistro;
    @Column(name="registro_pl",length=30)
    private String registroPl;
    @Column(name="flg_bloqueio", length=1, nullable=true)
    private boolean bloqueio;
    @Column(name="isencao_fiscal", length=1)
    private boolean isencaoFiscal;
    @Column(name="representante_legal", length=45)
    private String representanteLegal;
    @Column(length=60)
    private String rua;
    @Column(length=45)
    private String bairro;
    @Column(length=20)
    private String telefone;
    @Column(name="permicao_contador")
    private String permicaoContador;
    @Column(name="fgl_cobranca")
    private boolean cobraca;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name="data_abertura")
    private Date dataAbertura;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name="data_encerramento")
    private Date dataEncerramento;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name="data_bloqueio")
    private Date dataBloqueio;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name="data_simples")
    private Date dataSimples;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name="data_mei")
    private Date dataMei;
    @Column(name="servicos_prestados")
    private String servicosPrestados;
    @Column(name="data_liberacao_gemmap")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataLiberacaoGemmap;
    @Column(name="codigo_liberacao_senha_web")
    private String codigoLiberacaoSenhaWeb;
    @Column(name="flg_Tipo_contribuinte", length= 1)
    private String tipoTipoContribuinte;
    @Column(name="logo",length=60)
    private String logo;    
    @Column(name="pes_nro",length=10)
    private Integer pesNro;
    @Column(name="cod_iss_gemmap",length=10)
    private Integer codISSGemmap;
    @Column(name="flg_notifica_gemmap")
    private Boolean notificacaoGemmap;
    @Column(name="flg_contador")
    private Boolean contadorProfissional;    
    @Column(name="crc",length=30)
    private String crc;
    @Column(name="cod_eventual_gemmap",length=15)
    private Integer codigoEventualGemmap;
    
    
    
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "ccm")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<HistoricoBloqueio> historicoBloqueios;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "ccm")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<Socio> socios;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "ccm1")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<Contato> contatos;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "ccm")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<Usuario> usuarios;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "destinatario")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<Notificacoes> notificacoes;

    public Ccm(){
    }
    // anotacao dada para um atributo que náo deve ser mapeada no banco...    
    @Transient
    private List<Servico> listaServicosPrestados;

    public List<Servico> getListaServicosPrestados() {
        
        DAOServico daoServico = new DAOServico();
        listaServicosPrestados = daoServico.buscaServicos(this.servicosPrestados);
        
        return listaServicosPrestados;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Logradouro getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(Logradouro logradouro) {
        this.logradouro = logradouro;
    }

    public Ccm getContador() {
        return contador;
    }

    public void setContador(Ccm contador) {
        this.contador = contador;
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

    public String getFantasia() {
        return fantasia;
    }

    public void setFantasia(String fantazia) {
        this.fantasia = fantazia;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIeRg() {
        return ieRg;
    }

    public void setIeRg(String ieRg) {
        this.ieRg = ieRg;
    }

    public String getNumeroPredio() {
        return numeroPredio;
    }

    public void setNumeroPredio(String numeroPredio) {
        this.numeroPredio = numeroPredio;
    }
    
    public String getIm() {
        return im;
    }

    public void setIm(String im) {
        this.im = im;
    }

    public String getNomeRazao() {
        return nomeRazao;
    }

    public void setNomeRazao(String nomeRazao) {
        this.nomeRazao = nomeRazao;
    }
    
    public String getRegistroPl() {
        return registroPl;
    }

    public void setRegistroPl(String registroPl) {
        this.registroPl = registroPl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public int getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(int tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public int getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(int tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public List<Contato> getContatos() {
        return contatos;
    }

    public void setContatos(List<Contato> contatos) {
        this.contatos = contatos;
    }

    public List<Socio> getSocios() {
        return socios;
    }

    public void setSocios(List<Socio> socios) {
        this.socios = socios;
    }

    public String getRepresentanteLegal() {
        return representanteLegal;
    }

    public void setRepresentanteLegal(String representanteLegal) {
        this.representanteLegal = representanteLegal;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getPermicaoContador() {
        return permicaoContador;
    }

    public void setPermicaoContador(String permicaoContador) {
        this.permicaoContador = permicaoContador;
    }

    public boolean isAutonomo() {
        return autonomo;
    }

    public void setAutonomo(boolean autonomo) {
        this.autonomo = autonomo;
    }

    public boolean isIsencaoFiscal() {
        return isencaoFiscal;
    }

    public void setIsencaoFiscal(boolean isencaoFiscal) {
        this.isencaoFiscal = isencaoFiscal;
    }
    
    public boolean isMei() {
        return mei;
    }

    public void setMei(boolean mei) {
        this.mei = mei;
    }

    public boolean isProfissionalLiberal() {
        return profissionalLiberal;
    }

    public void setProfissionalLiberal(boolean profissionalLiberal) {
        this.profissionalLiberal = profissionalLiberal;
    }

    public boolean isSimples() {
        return simples;
    }

    public void setSimples(boolean simples) {
        this.simples = simples;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public boolean isCobraca() {
        return cobraca;
    }

    public void setCobraca(boolean cobraca) {
        this.cobraca = cobraca;
    }

    public Date getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(Date dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public Date getDataBloqueio() {
        return dataBloqueio;
    }

    public void setDataBloqueio(Date dataBloqueio) {
        this.dataBloqueio = dataBloqueio;
    }

    public Date getDataEncerramento() {
        return dataEncerramento;
    }

    public void setDataEncerramento(Date dataEncerramento) {
        this.dataEncerramento = dataEncerramento;
    }

    public Date getDataMei() {
        return dataMei;
    }

    public void setDataMei(Date dataMei) {
        this.dataMei = dataMei;
    }

    public Date getDataSimples() {
        return dataSimples;
    }

    public void setDataSimples(Date dataSimples) {
        this.dataSimples = dataSimples;
    }

    public String getServicosPrestados() {
        return servicosPrestados;
    }

    public void setServicosPrestados(String servicosPrestados) {
        this.servicosPrestados = servicosPrestados;
    }

    public GeneroAtividade getGeneroAtividade() {
        return generoAtividade;
    }

    public void setGeneroAtividade(GeneroAtividade generoAtividade) {
        this.generoAtividade = generoAtividade;
    }

    public List<HistoricoBloqueio> getHistoricoBloqueios() {
        return historicoBloqueios;
    }

    public void setHistoricoBloqueios(List<HistoricoBloqueio> historicoBloqueios) {
        this.historicoBloqueios = historicoBloqueios;
    }

    public Date getDataLiberacaoGemmap() {
        return dataLiberacaoGemmap;
    }

    public void setDataLiberacaoGemmap(Date dataLiberacaoGemmap) {
        this.dataLiberacaoGemmap = dataLiberacaoGemmap;
    }

    public String getCodigoLiberacaoSenhaWeb() {
        return codigoLiberacaoSenhaWeb;
    }

    public void setCodigoLiberacaoSenhaWeb(String codigoLiberacaoSenhaWeb) {
        this.codigoLiberacaoSenhaWeb = codigoLiberacaoSenhaWeb;
    }

    public String getTipoTipoContribuinte() {
        return tipoTipoContribuinte;
    }

    public void setTipoTipoContribuinte(String tipoTipoContribuinte) {
        this.tipoTipoContribuinte = tipoTipoContribuinte;
    }

    public List<Notificacoes> getNotificacoes() {
        return notificacoes;
    }

    public void setNotificacoes(List<Notificacoes> notificacoes) {
        this.notificacoes = notificacoes;
    }

    public Integer getPesNro() {
        return pesNro;
    }

    public void setPesNro(Integer pesNro) {
        this.pesNro = pesNro;
    }

    public Integer getCodISSGemmap() {
        return codISSGemmap;
    }

    public void setCodISSGemmap(Integer codISSGemmap) {
        this.codISSGemmap = codISSGemmap;
    }

    public Boolean getNotificacaoGemmap() {
        return notificacaoGemmap;
    }

    public void setNotificacaoGemmap(Boolean notificacaoGemmap) {
        this.notificacaoGemmap = notificacaoGemmap;
    }

    public boolean isBloqueio() {
        return bloqueio;
    }

    public void setBloqueio(boolean bloqueio) {
        this.bloqueio = bloqueio;
    }

    public Boolean getContadorProfissional() {
        return contadorProfissional;
    }

    public void setContadorProfissional(Boolean contadorProfissional) {
        this.contadorProfissional = contadorProfissional;
    }

    public String getCrc() {
        return crc;
    }

    public void setCrc(String crc) {
        this.crc = crc;
    }

    public Integer getCodigoEventualGemmap() {
        return codigoEventualGemmap;
    }

    public void setCodigoEventualGemmap(Integer codigoEventualGemmap) {
        this.codigoEventualGemmap = codigoEventualGemmap;
    }
}
