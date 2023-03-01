package com.erickrodrigues.musicflux.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistDetailsDto {

    private Long id;

    private String name;

    private Long profileId;
}
