package com.erickrodrigues.musicflux.repositories;

import com.erickrodrigues.musicflux.domain.Song;
import org.springframework.data.repository.CrudRepository;

public interface SongRepository extends CrudRepository<Song, Long> {}
