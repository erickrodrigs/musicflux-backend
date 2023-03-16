package com.erickrodrigues.musicflux.user;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private static User user1 = User
            .builder()
            .name("Erick")
            .username("erick666")
            .email("erick@erick.com")
            .password("erick123")
            .build();
    private static User user2 = User
            .builder()
            .name("Tiago")
            .username("tiago333")
            .email("tiago@tiago.com")
            .password("tiago123")
            .build();

    @BeforeAll
    public void setUp() {
        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
    }

    @Test
    public void shouldFindUserByUsernameOrEmail() {
        final Optional<User> optionalUserByUsername = userRepository.findByUsernameOrEmail(
                "erick666", "abc@abc.com"
        );
        final Optional<User> optionalUserByEmail = userRepository.findByUsernameOrEmail(
                "abc123", "erick@erick.com"
        );

        assertTrue(optionalUserByUsername.isPresent(), "User was not found by username");
        assertTrue(optionalUserByEmail.isPresent(), "User was not found by email");
        assertEquals(user1.getId(), optionalUserByUsername.get().getId(), "Wrong user id when finding by username");
        assertEquals(user1.getId(), optionalUserByEmail.get().getId(), "Wrong user id when finding by email");
    }

    @Test
    public void shouldNotFindUserByUsernameOrEmailWhenTheyDoNotExist() {
        final Optional<User> optionalUser = userRepository.findByUsernameOrEmail("abc123", "abc@abc.com");

        assertTrue(optionalUser.isEmpty(), "User was found by username or email when they actually do not exist");
    }

    @Test
    public void shouldFindUserByUsernameAndPassword() {
        final Optional<User> optionalUser = userRepository.findByUsernameAndPassword(
                "tiago333", "tiago123"
        );

        assertTrue(optionalUser.isPresent(), "User was not found by username and password");
        assertEquals(user2.getId(), optionalUser.get().getId(), "Wrong user id when finding by username and password");
    }

    @Test
    public void shouldNotFindUserByUsernameAndPasswordWhenTheyDoNotMatch() {
        final Optional<User> optionalUser = userRepository.findByUsernameAndPassword(
                "erick666", "tiago123"
        );

        assertTrue(optionalUser.isEmpty(), "User was found by username and password when they actually do not match");
    }
}
