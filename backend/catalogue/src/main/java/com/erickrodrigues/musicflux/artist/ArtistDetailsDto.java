package com.erickrodrigues.musicflux.artist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistDetailsDto {

    private Long id;

    private String name;

    private String biography;

    private String photoUrl;
}
