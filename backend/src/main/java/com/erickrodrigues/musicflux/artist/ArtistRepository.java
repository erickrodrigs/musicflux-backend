package com.erickrodrigues.musicflux.artist;

import com.erickrodrigues.musicflux.artist.Artist;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArtistRepository extends CrudRepository<Artist, Long> {

    List<Artist> findAllByNameContainingIgnoreCase(String name);
}
