package com.erickrodrigues.musicflux.artist;

import com.erickrodrigues.musicflux.album.Album;
import com.erickrodrigues.musicflux.track.Track;

import java.util.List;

public interface ArtistService {

    List<Artist> findAllByNameContainingIgnoreCase(String text);

    List<Album> getArtistAlbums(Long artistId);

    List<Track> getTopTracks(Long artistId);
}
