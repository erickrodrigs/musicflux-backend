package com.erickrodrigues.musicflux.recently_played;

import com.erickrodrigues.musicflux.song.SongDetailsDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecentlyPlayedDetailsDto {

    private Long id;

    private SongDetailsDto song;

    private Long userId;

    private LocalDateTime createdAt;
}
