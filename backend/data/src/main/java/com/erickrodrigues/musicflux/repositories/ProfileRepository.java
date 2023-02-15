package com.erickrodrigues.musicflux.repositories;

import com.erickrodrigues.musicflux.domain.Profile;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<Profile, Long> {}
