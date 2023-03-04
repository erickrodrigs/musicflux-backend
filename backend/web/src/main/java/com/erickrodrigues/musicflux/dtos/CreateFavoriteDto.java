package com.erickrodrigues.musicflux.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateFavoriteDto {

    @NotNull
    @Positive(message = "Song ID must be a positive value")
    private Long songId;
}
