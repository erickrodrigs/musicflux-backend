package com.erickrodrigues.musicflux.profile;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProfileRepositoryTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Test
    public void findByUsernameOrEmail() {
        final Profile profile1 = Profile.builder().id(1L).username("erick666").email("erick@erick.com").build();
        final Profile profile2 = Profile.builder().id(2L).username("tiago333").email("tiago@tiago.com").build();

        profileRepository.save(profile1);
        profileRepository.save(profile2);

        Optional<Profile> optionalProfile;

        optionalProfile = profileRepository.findByUsernameOrEmail("erick666", "abc@abc.com");

        assertTrue(optionalProfile.isPresent());
        assertEquals(profile1.getId(), optionalProfile.get().getId());

        optionalProfile = profileRepository.findByUsernameOrEmail("abc123", "tiago@tiago.com");

        assertTrue(optionalProfile.isPresent());
        assertEquals(profile2.getId(), optionalProfile.get().getId());
    }

    @Test
    public void findByUsernameOrEmailWhenTheyDoNotExist() {
        final Profile user1 = Profile.builder().id(1L).username("erick666").email("erick@erick.com").build();
        final Profile user2 = Profile.builder().id(2L).username("tiago333").email("tiago@tiago.com").build();

        profileRepository.save(user1);
        profileRepository.save(user2);

        Optional<Profile> optionalProfile = profileRepository.findByUsernameOrEmail("abc123", "abc@abc.com");

        assertTrue(optionalProfile.isEmpty());
    }

    @Test
    public void findByUsernameAndPassword() {
        final Profile user1 = Profile.builder().id(1L).username("erick666").password("erick123").build();
        final Profile user2 = Profile.builder().id(2L).username("tiago333").password("tiago123").build();

        profileRepository.save(user1);
        profileRepository.save(user2);

        Optional<Profile> optionalProfile;

        optionalProfile = profileRepository.findByUsernameAndPassword("erick666", "erick123");

        assertTrue(optionalProfile.isPresent());
        assertEquals(user1.getId(), optionalProfile.get().getId());

        optionalProfile = profileRepository.findByUsernameAndPassword("tiago333", "tiago123");

        assertTrue(optionalProfile.isPresent());
        assertEquals(user2.getId(), optionalProfile.get().getId());
    }

    @Test
    public void findByUsernameAndPasswordWhenTheyDoNotMatch() {
        final Profile user1 = Profile.builder().id(1L).username("erick666").password("erick123").build();
        final Profile user2 = Profile.builder().id(2L).username("tiago333").password("tiago123").build();

        profileRepository.save(user1);
        profileRepository.save(user2);

        Optional<Profile> optionalProfile;

        optionalProfile = profileRepository.findByUsernameAndPassword("erick666", "tiago123");

        assertTrue(optionalProfile.isEmpty());

        optionalProfile = profileRepository.findByUsernameAndPassword("tiago333", "erick123");

        assertTrue(optionalProfile.isEmpty());
    }
}
