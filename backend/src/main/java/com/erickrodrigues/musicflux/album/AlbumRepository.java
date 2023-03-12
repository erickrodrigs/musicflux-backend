package com.erickrodrigues.musicflux.album;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AlbumRepository extends CrudRepository<Album, Long> {

    List<Album> findAllByTitleContainingIgnoreCase(String title);

    List<Album> findAllByArtistsId(Long artistId);
}
