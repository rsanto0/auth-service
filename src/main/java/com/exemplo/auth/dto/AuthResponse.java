package com.exemplo.auth.dto;

public class AuthResponse {
    private String token;
    private String login;
    private String role;
    private Long userId;
    
    public AuthResponse(String token, String login, String role, Long userId) {
        this.token = token;
        this.login = login;
        this.role = role;
        this.userId = userId;
    }
    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}