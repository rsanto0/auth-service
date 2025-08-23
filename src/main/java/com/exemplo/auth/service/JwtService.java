package com.exemplo.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private Long expiration;
    
    /**
     * Gera token JWT com claims personalizados
     * @param login nome de usuário (subject)
     * @param role papel do usuário (ADMIN/FUNCIONARIO)
     * @param userId ID único do usuário
     * @return token JWT assinado
     */
    public String generateToken(String login, String role, Long userId) {
        return Jwts.builder()
                .subject(login)
                .claim("role", role)
                .claim("userId", userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }
    
    /**
     * Valida e extrai claims de um token JWT
     * @param token JWT a ser validado
     * @return claims do token se válido
     * @throws JwtException se token inválido/expirado
     */
    public Claims validateToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    /**
     * Gera chave de assinatura HMAC a partir do secret
     * @return chave secreta para assinar/validar tokens
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}