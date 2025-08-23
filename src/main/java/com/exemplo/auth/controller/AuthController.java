package com.exemplo.auth.controller;

import com.exemplo.auth.dto.AuthResponse;
import com.exemplo.auth.dto.LoginRequest;
import com.exemplo.auth.model.User;
import com.exemplo.auth.repository.UserRepository;
import com.exemplo.auth.service.JwtService;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UserRepository userRepository;
    private final JwtService jwtService;
    
    /**
     * Construtor com injeção de dependências
     */
    public AuthController(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }
    
    /**
     * Autentica usuário e gera token JWT
     * @param request dados de login (login/senha)
     * @return token JWT + dados do usuário ou 401 se inválido
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        logger.info("[LOGIN] 🔑 Tentativa de autenticação - Login: {}", request.getLogin());
        
        User user = userRepository.findByLogin(request.getLogin());
        if (user == null) {
            logger.warn("[LOGIN] ❌ Usuário não encontrado - Login: {}", request.getLogin());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        if (!user.getSenha().equals(request.getSenha())) {
            logger.warn("[LOGIN] ❌ Credenciais inválidas - Login: {}", request.getLogin());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        logger.info("[LOGIN] 🔍 Gerando token JWT para usuário: {}", user.getLogin());
        String token = jwtService.generateToken(user.getLogin(), user.getRole().name(), user.getId());
        
        logger.info("[LOGIN] ✅ Autenticação bem-sucedida - Login: {}, Role: {}, UserId: {}", 
                   user.getLogin(), user.getRole().name(), user.getId());
        
        return ResponseEntity.ok(new AuthResponse(token, user.getLogin(), user.getRole().name(), user.getId()));
    }
    
    /**
     * Valida token JWT e retorna claims
     * @param authHeader header Authorization com Bearer token
     * @return claims do token ou 401 se inválido
     */
    @PostMapping("/validate")
    public ResponseEntity<Claims> validate(@RequestHeader("Authorization") String authHeader) {
        logger.info("[VALIDATE] 🔍 Validando token JWT recebido do Gateway");
        
        try {
            String token = authHeader.replace("Bearer ", "");
            logger.debug("[VALIDATE] 🔑 Processando token: {}...", token.substring(0, Math.min(20, token.length())));
            
            Claims claims = jwtService.validateToken(token);
            
            String subject = claims.getSubject();
            String role = (String) claims.get("role");
            Integer userId = (Integer) claims.get("userId");
            
            logger.info("[VALIDATE] ✅ Token válido - Subject: {}, Role: {}, UserId: {}", subject, role, userId);
            
            return ResponseEntity.ok(claims);
        } catch (Exception e) {
            logger.warn("[VALIDATE] ❌ Token inválido - Erro: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    
    /**
     * Cria novo usuário no sistema
     * @param user dados do usuário (login, senha, nome, cpf, role)
     * @return usuário criado com ID gerado
     */
    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        logger.info("[CREATE_USER] 👥 Iniciando criação de usuário - Login: {}, Role: {}", 
                   user.getLogin(), user.getRole());
        
        // Verificar se usuário já existe
        User existingUser = userRepository.findByLogin(user.getLogin());
        if (existingUser != null) {
            logger.warn("[CREATE_USER] ⚠️ Usuário já existe - Login: {}", user.getLogin());
            throw new RuntimeException("Usuário já existe");
        }
        
        try {
            logger.debug("[CREATE_USER] 💾 Salvando usuário no banco de dados");
            User savedUser = userRepository.save(user);
            
            logger.info("[CREATE_USER] ✅ Usuário criado com sucesso - ID: {}, Login: {}, CPF: {}", 
                       savedUser.getId(), savedUser.getLogin(), savedUser.getCpf());
            return savedUser;
        } catch (Exception e) {
            logger.error("[CREATE_USER] ❌ Erro ao criar usuário - Login: {}, Erro: {}", 
                        user.getLogin(), e.getMessage());
            throw e;
        }
    }
}