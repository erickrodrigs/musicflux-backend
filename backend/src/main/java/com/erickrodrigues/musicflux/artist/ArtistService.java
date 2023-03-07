package com.erickrodrigues.musicflux.artist;

import com.erickrodrigues.musicflux.artist.Artist;

import java.util.List;

public interface ArtistService {

    List<Artist> findAllByNameContainingIgnoreCase(String text);
}
