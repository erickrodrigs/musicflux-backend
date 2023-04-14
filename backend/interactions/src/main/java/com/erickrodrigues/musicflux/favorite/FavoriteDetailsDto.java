package com.erickrodrigues.musicflux.favorite;

import com.erickrodrigues.musicflux.track.TrackDetailsDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteDetailsDto {

    private Long id;

    private TrackDetailsDto track;

    private Long userId;
}
