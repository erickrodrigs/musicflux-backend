package com.erickrodrigues.musicflux.album;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumDto {

    private Long id;

    private String title;

    private String coverUrl;
}
