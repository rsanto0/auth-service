package com.exemplo.auth.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void constructor_ValidData_CreatesUser() {
        User user = new User();
        user.setLogin("testuser");
        user.setSenha("password");
        user.setNome("Test User");
        user.setCpf("12345678900");
        user.setRole(Role.FUNCIONARIO);

        assertEquals("testuser", user.getLogin());
        assertEquals("password", user.getSenha());
        assertEquals("Test User", user.getNome());
        assertEquals("12345678900", user.getCpf());
        assertEquals(Role.FUNCIONARIO, user.getRole());
    }

    @Test
    void setRole_AdminRole_SetsCorrectly() {
        User user = new User();
        user.setRole(Role.ADMIN);

        assertEquals(Role.ADMIN, user.getRole());
    }

    @Test
    void setRole_FuncionarioRole_SetsCorrectly() {
        User user = new User();
        user.setRole(Role.FUNCIONARIO);

        assertEquals(Role.FUNCIONARIO, user.getRole());
    }

    @Test
    void getId_NewUser_ReturnsNull() {
        User user = new User();
        
        assertNull(user.getId());
    }

    @Test
    void setId_ValidId_SetsCorrectly() {
        User user = new User();
        user.setId(1L);

        assertEquals(1L, user.getId());
    }

    @Test
    void allFields_SetAndGet_WorkCorrectly() {
        User user = new User();
        
        user.setId(1L);
        user.setLogin("admin");
        user.setSenha("admin123");
        user.setNome("Administrator");
        user.setCpf("00000000000");
        user.setRole(Role.ADMIN);

        assertEquals(1L, user.getId());
        assertEquals("admin", user.getLogin());
        assertEquals("admin123", user.getSenha());
        assertEquals("Administrator", user.getNome());
        assertEquals("00000000000", user.getCpf());
        assertEquals(Role.ADMIN, user.getRole());
    }
}