package com.erickrodrigues.musicflux.vo;

import com.erickrodrigues.musicflux.domain.Album;
import com.erickrodrigues.musicflux.domain.Artist;
import com.erickrodrigues.musicflux.domain.Playlist;
import com.erickrodrigues.musicflux.domain.Song;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SearchResult {
    private Set<Artist> artists;
    private Set<Album> albums;
    private Set<Song> songs;
    private Set<Playlist> playlists;
}
