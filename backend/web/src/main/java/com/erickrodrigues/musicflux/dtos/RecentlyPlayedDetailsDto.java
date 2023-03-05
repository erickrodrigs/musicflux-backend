package com.erickrodrigues.musicflux.dtos;

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

    private Long profileId;

    private LocalDateTime createdAt;
}
