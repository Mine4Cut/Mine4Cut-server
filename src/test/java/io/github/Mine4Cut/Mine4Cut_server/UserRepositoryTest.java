package io.github.Mine4Cut.Mine4Cut_server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest extends AbstractIntegrationTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void testUserSave() {
        User user = new User("testuser", "email@test.com");
        userRepository.save(user);
        assertEquals(1, userRepository.findAll().size());
    }
}