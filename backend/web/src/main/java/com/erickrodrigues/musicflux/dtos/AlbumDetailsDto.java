package com.erickrodrigues.musicflux.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumDetailsDto {

    private Long id;

    private String title;

    private String coverUrl;

    private LocalDate releaseDate;

    private List<Long> artistsIds;
}
