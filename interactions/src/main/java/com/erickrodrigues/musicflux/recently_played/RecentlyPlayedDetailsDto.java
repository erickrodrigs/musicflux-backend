package com.erickrodrigues.musicflux.recently_played;

import com.erickrodrigues.musicflux.album.AlbumDto;
import com.erickrodrigues.musicflux.artist.ArtistDto;
import com.erickrodrigues.musicflux.track.TrackDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecentlyPlayedDetailsDto {

    private Long id;

    private TrackDto track;

    private AlbumDto album;

    private List<ArtistDto> artists;

    private LocalDateTime createdAt;
}
