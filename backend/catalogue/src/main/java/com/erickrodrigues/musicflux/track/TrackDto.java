package com.erickrodrigues.musicflux.track;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackDto {

    private Long id;

    private String title;
}
