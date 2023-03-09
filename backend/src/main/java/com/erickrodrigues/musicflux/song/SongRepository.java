package com.erickrodrigues.musicflux.song;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SongRepository extends CrudRepository<Song, Long> {

    List<Song> findAllByTitleContainingIgnoreCase(String title);

    List<Song> findAllByGenresNameIgnoreCase(String genreName);
}
