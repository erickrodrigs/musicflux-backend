package com.erickrodrigues.musicflux.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query(value = """
        SELECT t FROM Token t INNER JOIN Profile p\s
        ON t.profile.id = p.id\s
        WHERE p.id = :id AND (t.expired = false OR t.revoked = false)\s
        """
    )
    List<Token> findAllValidTokensByProfile(Long id);

    Optional<Token> findByToken(String token);
}
