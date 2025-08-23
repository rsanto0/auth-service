package com.exemplo.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class AuthServiceApplication {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceApplication.class);
    
    public static void main(String[] args) {
        logger.info("[AUTH] Iniciando Auth Service...");
        SpringApplication.run(AuthServiceApplication.class, args);
    }
    
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        logger.info("[AUTH] ✅ Auth Service iniciado com sucesso!");
        logger.info("[AUTH] 🚀 Servidor rodando na porta 8081");
        logger.info("[AUTH] 🔐 Endpoints disponíveis:");
        logger.info("[AUTH]   • POST /auth/login - Autenticação de usuários");
        logger.info("[AUTH]   • POST /auth/validate - Validação de tokens JWT");
        logger.info("[AUTH]   • POST /auth/users - Criação de usuários");
        logger.info("[AUTH] 🗄️ Banco H2 em memória configurado");
        logger.info("[AUTH] 🔑 JWT configurado com expiração de 1 hora");
    }
}