package br.com.fiap.comerbem.model;

import java.time.LocalDate;
import java.util.regex.Pattern;
import br.com.fiap.comerbem.model.enums.TipoUsuario;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Usuario {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String login;
    private String senha;

    @Embedded
    private Endereco endereco;
    private TipoUsuario tipoUsuario;
    private LocalDate ultimaAlteracao;

    public Usuario() {
    }

    public Usuario(Long id, String nome, String email, String login, Endereco endereco, TipoUsuario tipoUsuario,
            LocalDate ultimaAlteracao) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.login = login;
        this.endereco = endereco;
        this.tipoUsuario = tipoUsuario;
        this.ultimaAlteracao = ultimaAlteracao;
    }
    

    public Usuario(String nome, String email, String login, String senha, TipoUsuario tipoUsuario, Endereco endereco, LocalDate ultimaAlteracao) {
        this.nome = nome;
        this.email = email;
        this.login = login;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
        this.endereco = endereco;
        this.ultimaAlteracao = ultimaAlteracao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null) {
            throw new IllegalArgumentException("O campo email não pode ser vazio!");
        }
        if (!Pattern.compile(EMAIL_REGEX).matcher(email).matches()) {
            throw new IllegalArgumentException("O email passado não corresponde o padrão: exemplo@email.com");
        }
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public LocalDate getUltimaAlteracao() {
        return ultimaAlteracao;
    }

    public void setUltimaAlteracao(LocalDate ultimaAlteracao) {
        if (this.ultimaAlteracao.isAfter(ultimaAlteracao)) {
            throw new IllegalArgumentException("A data da ultima modificação não deve ser menor a anterior!");
        }
        this.ultimaAlteracao = ultimaAlteracao;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public TipoUsuario getTipo() {
        return tipoUsuario;
    }

    public void setTipo(TipoUsuario tipo) {
        this.tipoUsuario = tipo;
    }
}
