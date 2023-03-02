package com.erickrodrigues.musicflux.repositories;

import com.erickrodrigues.musicflux.domain.Album;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AlbumRepository extends CrudRepository<Album, Long> {

    List<Album> findAllByTitleContainingIgnoreCase(String title);

    List<Album> findAllByArtistsIn(List<Long> artistIds);
}
