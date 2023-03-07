package com.erickrodrigues.musicflux.profile;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProfileRepository extends CrudRepository<Profile, Long> {

    Optional<Profile> findByUsername(String username);

    Optional<Profile> findByUsernameOrEmail(String username, String email);

    Optional<Profile> findByUsernameAndPassword(String username, String password);
}
