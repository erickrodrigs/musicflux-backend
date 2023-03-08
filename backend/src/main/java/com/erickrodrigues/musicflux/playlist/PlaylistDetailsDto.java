package com.erickrodrigues.musicflux.playlist;

import com.erickrodrigues.musicflux.song.SongDetailsDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistDetailsDto {

    private Long id;

    private String name;

    private Long userId;

    private List<SongDetailsDto> songs;
}
