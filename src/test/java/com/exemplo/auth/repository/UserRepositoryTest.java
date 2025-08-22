package com.exemplo.auth.repository;

import com.exemplo.auth.model.Role;
import com.exemplo.auth.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByLogin_ExistingUser_ReturnsUser() {
        User user = new User();
        user.setLogin("testuser");
        user.setSenha("password");
        user.setNome("Test User");
        user.setCpf("12345678900");
        user.setRole(Role.FUNCIONARIO);

        entityManager.persistAndFlush(user);

        User found = userRepository.findByLogin("testuser");

        assertNotNull(found);
        assertEquals("testuser", found.getLogin());
        assertEquals("Test User", found.getNome());
        assertEquals(Role.FUNCIONARIO, found.getRole());
    }

    @Test
    void findByLogin_NonExistingUser_ReturnsNull() {
        User found = userRepository.findByLogin("nonexistent");
        
        assertNull(found);
    }

    @Test
    void save_ValidUser_PersistsUser() {
        User user = new User();
        user.setLogin("newuser");
        user.setSenha("password");
        user.setNome("New User");
        user.setCpf("98765432100");
        user.setRole(Role.ADMIN);

        User saved = userRepository.save(user);

        assertNotNull(saved.getId());
        assertEquals("newuser", saved.getLogin());
        assertEquals("New User", saved.getNome());
        assertEquals(Role.ADMIN, saved.getRole());

        User found = entityManager.find(User.class, saved.getId());
        assertNotNull(found);
        assertEquals("newuser", found.getLogin());
    }

    @Test
    void save_DuplicateLogin_ThrowsException() {
        User user1 = new User();
        user1.setLogin("duplicate");
        user1.setSenha("password1");
        user1.setNome("User 1");
        user1.setCpf("11111111111");
        user1.setRole(Role.FUNCIONARIO);

        User user2 = new User();
        user2.setLogin("duplicate");
        user2.setSenha("password2");
        user2.setNome("User 2");
        user2.setCpf("22222222222");
        user2.setRole(Role.FUNCIONARIO);

        userRepository.save(user1);

        assertThrows(Exception.class, () -> {
            userRepository.save(user2);
            entityManager.flush();
        });
    }
}