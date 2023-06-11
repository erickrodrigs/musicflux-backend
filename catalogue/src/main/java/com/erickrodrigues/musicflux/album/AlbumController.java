package com.erickrodrigues.musicflux.album;

import com.erickrodrigues.musicflux.track.TrackDetailsDto;
import com.erickrodrigues.musicflux.track.TrackMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "albums")
@RestController
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;
    private final TrackMapper trackMapper;

    @Operation(summary = "Get all tracks in an album by its id")
    @GetMapping("/albums/{album_id}/tracks")
    @ResponseStatus(HttpStatus.OK)
    public List<TrackDetailsDto> findAllByAlbumId(@PathVariable("album_id") Long albumId) {
        return trackMapper.toListOfTrackDetailsDto(albumService.getAlbumTracks(albumId));
    }
}
