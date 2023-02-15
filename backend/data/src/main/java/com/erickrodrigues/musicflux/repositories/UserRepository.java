package com.erickrodrigues.musicflux.repositories;

import com.erickrodrigues.musicflux.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {}
