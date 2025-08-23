package com.exemplo.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
    
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
        logger.debug("[JWT] 🔨 Gerando token para: login={}, role={}, userId={}", login, role, userId);
        
        Date issuedAt = new Date();
        Date expiresAt = new Date(System.currentTimeMillis() + expiration);
        
        logger.debug("[JWT] ⏰ Token válido de {} até {}", issuedAt, expiresAt);
        
        String token = Jwts.builder()
                .subject(login)
                .claim("role", role)
                .claim("userId", userId)
                .issuedAt(issuedAt)
                .expiration(expiresAt)
                .signWith(getSigningKey())
                .compact();
        
        logger.info("[JWT] ✅ Token gerado com sucesso para usuário: {}", login);
        logger.debug("[JWT] 🔑 Token: {}...", token.substring(0, Math.min(30, token.length())));
        
        return token;
    }
    
    /**
     * Valida e extrai claims de um token JWT
     * @param token JWT a ser validado
     * @return claims do token se válido
     * @throws JwtException se token inválido/expirado
     */
    public Claims validateToken(String token) {
        logger.debug("[JWT] 🔍 Validando token: {}...", token.substring(0, Math.min(20, token.length())));
        
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            
            String subject = claims.getSubject();
            String role = (String) claims.get("role");
            Date expiration = claims.getExpiration();
            
            logger.info("[JWT] ✅ Token válido - Subject: {}, Role: {}, Expira em: {}", subject, role, expiration);
            
            return claims;
        } catch (Exception e) {
            logger.warn("[JWT] ❌ Falha na validação do token: {}", e.getMessage());
            throw e;
        }
    }
    
    /**
     * Gera chave de assinatura HMAC a partir do secret
     * @return chave secreta para assinar/validar tokens
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}