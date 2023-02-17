package com.erickrodrigues.musicflux.repositories;

import com.erickrodrigues.musicflux.domain.Song;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface SongRepository extends CrudRepository<Song, Long> {

    Set<Song> findAllByTitleContainingIgnoreCase(String title);
}
