package com.erickrodrigues.musicflux.repositories;

import com.erickrodrigues.musicflux.domain.Profile;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProfileRepository extends CrudRepository<Profile, Long> {

    Optional<Profile> findByUsernameOrEmail(String username, String email);

    Optional<Profile> findByUsernameAndPassword(String username, String password);
}
