package com.exemplo.auth.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String login;
    
    private String senha;
    private String nome;
    private String cpf;
    
    @Enumerated(EnumType.STRING)
    private Role role;
    
    // Getters e Setters
    /** @return ID único do usuário */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    /** @return login único do usuário */
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    
    /** @return senha em texto plano */
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    
    /** @return nome completo do usuário */
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    /** @return CPF do usuário */
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    
    /** @return papel do usuário (ADMIN/FUNCIONARIO) */
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}