package com.erickrodrigues.musicflux.catalogue;

import com.erickrodrigues.musicflux.album.AlbumDetailsDto;
import com.erickrodrigues.musicflux.artist.ArtistDetailsDto;
import com.erickrodrigues.musicflux.track.TrackDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatalogueResultDto {

    private List<ArtistDetailsDto> artists;

    private List<AlbumDetailsDto> albums;

    private List<TrackDto> tracks;
}
