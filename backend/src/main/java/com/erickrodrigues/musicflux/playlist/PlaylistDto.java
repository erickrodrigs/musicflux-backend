package com.erickrodrigues.musicflux.playlist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistDto {

    private Long id;

    private String name;

    private Long userId;
}
