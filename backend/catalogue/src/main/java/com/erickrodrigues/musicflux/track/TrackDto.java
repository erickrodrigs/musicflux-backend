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
public class TrackDto {

    private Long id;

    private String title;

    private Long length;

    private AlbumDto album;

    private List<ArtistDto> artists;

    private List<String> genres;
}
