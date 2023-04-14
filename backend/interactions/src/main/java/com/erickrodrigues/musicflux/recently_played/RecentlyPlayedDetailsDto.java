package com.erickrodrigues.musicflux.recently_played;

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

    private String trackTitle;

    private String albumTitle;

    private List<String> artistsNames;

    private LocalDateTime createdAt;
}
