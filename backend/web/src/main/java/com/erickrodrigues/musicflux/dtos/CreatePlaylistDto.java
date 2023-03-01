package com.erickrodrigues.musicflux.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePlaylistDto {

    @NotBlank(message = "Playlist name is mandatory")
    private String name;
}
