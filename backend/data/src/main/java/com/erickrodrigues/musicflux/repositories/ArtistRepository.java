package com.erickrodrigues.musicflux.repositories;

import com.erickrodrigues.musicflux.domain.Artist;
import org.springframework.data.repository.CrudRepository;

public interface ArtistRepository extends CrudRepository<Artist, Long> {}
