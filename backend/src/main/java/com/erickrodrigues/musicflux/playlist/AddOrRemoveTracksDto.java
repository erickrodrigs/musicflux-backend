package com.erickrodrigues.musicflux.playlist;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddOrRemoveTracksDto {

    @NotNull(message = "Tracks IDs is mandatory")
    private List<Long> tracksIds;
}
