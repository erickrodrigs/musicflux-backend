package com.erickrodrigues.musicflux.search;

import com.erickrodrigues.musicflux.album.AlbumDetailsDto;
import com.erickrodrigues.musicflux.artist.ArtistDetailsDto;
import com.erickrodrigues.musicflux.playlist.PlaylistDto;
import com.erickrodrigues.musicflux.track.TrackDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchResultDto {

    @Builder.Default
    private List<ArtistDetailsDto> artists = new ArrayList<>();

    @Builder.Default
    private List<AlbumDetailsDto> albums = new ArrayList<>();

    @Builder.Default
    private List<TrackDto> tracks = new ArrayList<>();

    @Builder.Default
    private List<PlaylistDto> playlists = new ArrayList<>();
}
