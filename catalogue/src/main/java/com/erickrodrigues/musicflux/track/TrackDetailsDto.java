package com.erickrodrigues.musicflux.track;

import com.erickrodrigues.musicflux.album.AlbumDto;
import com.erickrodrigues.musicflux.artist.ArtistDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackDetailsDto {

    private Long id;

    private String title;

    private Long length;

    private Long numberOfPlays;

    private AlbumDto album;

    private List<ArtistDto> artists;

    private List<String> genres;
}
