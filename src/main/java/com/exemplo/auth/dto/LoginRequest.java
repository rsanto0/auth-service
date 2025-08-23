package com.exemplo.auth.dto;

public class LoginRequest {
    private String login;
    private String senha;
    
    /** @return nome de usuário para autenticação */
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    
    /** @return senha em texto plano */
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}