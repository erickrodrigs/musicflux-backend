package com.erickrodrigues.musicflux.vo;

import com.erickrodrigues.musicflux.domain.Album;
import com.erickrodrigues.musicflux.domain.Artist;
import com.erickrodrigues.musicflux.domain.Playlist;
import com.erickrodrigues.musicflux.domain.Song;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchResult {

    @Builder.Default
    private List<Artist> artists = new ArrayList<>();

    @Builder.Default
    private List<Album> albums = new ArrayList<>();

    @Builder.Default
    private List<Song> songs = new ArrayList<>();

    @Builder.Default
    private List<Playlist> playlists = new ArrayList<>();
}
