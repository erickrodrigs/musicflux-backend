package com.erickrodrigues.musicflux.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongDetailsDto {

    private Long id;

    private String title;

    private Long length;

    private Long numberOfPlays;

    private Set<String> genres;

    private Long albumId;
}
