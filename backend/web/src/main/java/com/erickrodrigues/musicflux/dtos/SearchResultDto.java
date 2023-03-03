package com.erickrodrigues.musicflux.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchResultDto {

    private List<ArtistDetailsDto> artists;

    private List<AlbumDetailsDto> albums;

    private List<SongDetailsDto> songs;

    private List<PlaylistDetailsDto> playlists;
}
