package com.erickrodrigues.musicflux.repositories;

import com.erickrodrigues.musicflux.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByUsernameOrEmail() {
        final User user1 = User.builder().id(1L).username("erick666").email("erick@erick.com").build();
        final User user2 = User.builder().id(2L).username("tiago333").email("tiago@tiago.com").build();

        userRepository.save(user1);
        userRepository.save(user2);

        Optional<User> optionalUser;

        optionalUser = userRepository.findByUsernameOrEmail("erick666", "abc@abc.com");

        assertTrue(optionalUser.isPresent());
        assertEquals(user1.getId(), optionalUser.get().getId());

        optionalUser = userRepository.findByUsernameOrEmail("abc123", "tiago@tiago.com");

        assertTrue(optionalUser.isPresent());
        assertEquals(user2.getId(), optionalUser.get().getId());
    }

    @Test
    public void findByUsernameOrEmailWhenTheyDoNotExist() {
        final User user1 = User.builder().id(1L).username("erick666").email("erick@erick.com").build();
        final User user2 = User.builder().id(2L).username("tiago333").email("tiago@tiago.com").build();

        userRepository.save(user1);
        userRepository.save(user2);

        Optional<User> optionalUser = userRepository.findByUsernameOrEmail("abc123", "abc@abc.com");

        assertTrue(optionalUser.isEmpty());
    }

    @Test
    public void findByUsernameAndPassword() {
        final User user1 = User.builder().id(1L).username("erick666").password("erick123").build();
        final User user2 = User.builder().id(2L).username("tiago333").password("tiago123").build();

        userRepository.save(user1);
        userRepository.save(user2);

        Optional<User> optionalUser;

        optionalUser = userRepository.findByUsernameAndPassword("erick666", "erick123");

        assertTrue(optionalUser.isPresent());
        assertEquals(user1.getId(), optionalUser.get().getId());

        optionalUser = userRepository.findByUsernameAndPassword("tiago333", "tiago123");

        assertTrue(optionalUser.isPresent());
        assertEquals(user2.getId(), optionalUser.get().getId());
    }

    @Test
    public void findByUsernameAndPasswordWhenTheyDoNotMatch() {
        final User user1 = User.builder().id(1L).username("erick666").password("erick123").build();
        final User user2 = User.builder().id(2L).username("tiago333").password("tiago123").build();

        userRepository.save(user1);
        userRepository.save(user2);

        Optional<User> optionalUser;

        optionalUser = userRepository.findByUsernameAndPassword("erick666", "tiago123");

        assertTrue(optionalUser.isEmpty());

        optionalUser = userRepository.findByUsernameAndPassword("tiago333", "erick123");

        assertTrue(optionalUser.isEmpty());
    }
}
