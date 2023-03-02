package com.erickrodrigues.musicflux.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongDetailsDto {

    private Long id;

    private String title;

    private Long length;

    private Long numberOfPlays;

    private List<String> genres;

    private Long albumId;
}
