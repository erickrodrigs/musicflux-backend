package com.erickrodrigues.musicflux.repositories;

import com.erickrodrigues.musicflux.domain.Song;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SongRepository extends CrudRepository<Song, Long> {

    List<Song> findAllByTitleContainingIgnoreCase(String title);
}
