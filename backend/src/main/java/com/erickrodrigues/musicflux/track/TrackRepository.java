package com.erickrodrigues.musicflux.track;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TrackRepository extends CrudRepository<Track, Long> {

    List<Track> findAllByTitleContainingIgnoreCase(String title);

    List<Track> findAllByGenresNameIgnoreCase(String genreName);

    List<Track> findAllByAlbumId(Long albumId);

    List<Track> findAllByAlbumArtistsId(Long artistId);
}
