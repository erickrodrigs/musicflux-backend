package com.erickrodrigues.musicflux.vo;

import com.erickrodrigues.musicflux.domain.Album;
import com.erickrodrigues.musicflux.domain.Artist;
import com.erickrodrigues.musicflux.domain.Playlist;
import com.erickrodrigues.musicflux.domain.Song;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchResult {
    private List<Artist> artists;
    private List<Album> albums;
    private List<Song> songs;
    private List<Playlist> playlists;
}
