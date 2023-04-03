package com.erickrodrigues.musicflux.search;

import com.erickrodrigues.musicflux.album.Album;
import com.erickrodrigues.musicflux.artist.Artist;
import com.erickrodrigues.musicflux.track.Track;
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
    private List<Track> tracks = new ArrayList<>();
}
