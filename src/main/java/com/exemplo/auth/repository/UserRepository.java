package com.exemplo.auth.repository;

import com.exemplo.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Busca usuário pelo login único
     * @param login nome de usuário
     * @return usuário encontrado ou null
     */
    User findByLogin(String login);
}