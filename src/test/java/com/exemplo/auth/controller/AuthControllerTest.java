package com.exemplo.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.exemplo.auth.dto.LoginRequest;
import com.exemplo.auth.model.Role;
import com.exemplo.auth.model.User;
import com.exemplo.auth.repository.UserRepository;
import com.exemplo.auth.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login_ValidCredentials_ReturnsAuthResponse() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setLogin("admin");
        user.setSenha("admin123");
        user.setRole(Role.ADMIN);

        LoginRequest request = new LoginRequest();
        request.setLogin("admin");
        request.setSenha("admin123");

        when(userRepository.findByLogin("admin")).thenReturn(user);
        when(jwtService.generateToken("admin", "ADMIN", 1L)).thenReturn("jwt-token");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.login").value("admin"))
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    void login_InvalidCredentials_ReturnsUnauthorized() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setLogin("admin");
        request.setSenha("wrong-password");

        User user = new User();
        user.setSenha("admin123");

        when(userRepository.findByLogin("admin")).thenReturn(user);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_UserNotFound_ReturnsUnauthorized() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setLogin("nonexistent");
        request.setSenha("password");

        when(userRepository.findByLogin("nonexistent")).thenReturn(null);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createUser_ValidUser_ReturnsUser() throws Exception {
        User user = new User();
        user.setLogin("newuser");
        user.setSenha("password");
        user.setNome("New User");
        user.setRole(Role.FUNCIONARIO);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setLogin("newuser");
        savedUser.setSenha("password");
        savedUser.setNome("New User");
        savedUser.setRole(Role.FUNCIONARIO);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        mockMvc.perform(post("/auth/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.login").value("newuser"))
                .andExpect(jsonPath("$.nome").value("New User"));
    }
}