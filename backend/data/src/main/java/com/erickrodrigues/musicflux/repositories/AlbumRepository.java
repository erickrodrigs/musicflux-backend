package com.erickrodrigues.musicflux.repositories;

import com.erickrodrigues.musicflux.domain.Album;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface AlbumRepository extends CrudRepository<Album, Long> {

    Set<Album> findAllByTitleContainingIgnoreCase(String title);

    Set<Album> findAllByArtistsIn(Set<Long> artistIds);
}
