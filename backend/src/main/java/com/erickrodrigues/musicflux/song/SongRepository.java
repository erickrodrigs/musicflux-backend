package com.erickrodrigues.musicflux.song;

import com.erickrodrigues.musicflux.song.Song;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SongRepository extends CrudRepository<Song, Long> {

    List<Song> findAllByTitleContainingIgnoreCase(String title);
}
