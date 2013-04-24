/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.nfse.to;

import java.io.Serializable;
import java.text.Collator;
import java.util.Date;
import java.util.Locale;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;



/**
 *
 * @author Thiago Henrique
 */
@Entity
@Table(name="usuarios")
public class Usuario implements Serializable, Comparable {
        
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_lancamentos_sequence_generator")
    @SequenceGenerator(name = "usuario_lancamentos_sequence_generator", sequenceName = "usuario_lancamentos_sequence_id", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;
    @ManyToOne
    private Ccm ccm;
    @Column(nullable=false)
    private String login;
    @Column(nullable=false)
    private String senha;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name="data_inscricao")
    private Date dataInscricao;
    private String salt;
    @Column(name="dica_senha")
    private String dicaSenha;
    @Column(length=100,name="nome")
    private String nome;
    @Column(length=60 ,name="email")
    private String email;
    @Column(length=20 ,name="telefone")
    private String telefone;
    @Column(length=45)
    private String cargo;
    @Column(length=1, name="tipo_usr")
    private Integer tipoUsuario;
    @Column(length=45)
    private String cadastroConfirmado;
    

    public String getDicaSenha() {
        return dicaSenha;
    }

    public void setDicaSenha(String dicaSenha) {
        this.dicaSenha = dicaSenha;
    }

    public Ccm getCcm() {
        return ccm;
    }

    public void setCcm(Ccm ccm) {
        this.ccm = ccm;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataInscricao() {
        return dataInscricao;
    }

    public void setDataInscricao(Date dataInscricao) {
        this.dataInscricao = dataInscricao;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Integer getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(Integer tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getCadastroConfirmado() {
        return cadastroConfirmado;
    }

    public void setCadastroConfirmado(String cadastroConfirmado) {
        this.cadastroConfirmado = cadastroConfirmado;
    }

    @Override
    public int compareTo(Object u) {
       // Collator cot = Collator.getInstance(new Locale("pt", "BR"));
        if (u != null) {
            Usuario usuario = (Usuario) u;
            return this.getNome().compareTo(usuario.getNome());
        } else {
            return 0;
        }
    }

}
