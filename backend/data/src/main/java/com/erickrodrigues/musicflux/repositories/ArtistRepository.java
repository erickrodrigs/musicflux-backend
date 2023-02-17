package com.erickrodrigues.musicflux.repositories;

import com.erickrodrigues.musicflux.domain.Artist;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ArtistRepository extends CrudRepository<Artist, Long> {

    Set<Artist> findAllByNameContainingIgnoreCase(String name);
}
