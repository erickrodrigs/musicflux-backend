package com.erickrodrigues.musicflux.album;

import com.erickrodrigues.musicflux.track.TrackDetailsDto;
import com.erickrodrigues.musicflux.track.TrackMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "albums")
@RestController
@RequestMapping("/albums/{album_id}")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;
    private final AlbumMapper albumMapper;
    private final TrackMapper trackMapper;

    @Operation(summary = "Find an album by its id")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public AlbumDetailsDto getAlbum(@PathVariable("album_id") Long albumId) {
        return albumMapper.toAlbumDetailsDto(albumService.findById(albumId));
    }

    @Operation(summary = "Get all tracks in an album by its id")
    @GetMapping("/tracks")
    @ResponseStatus(HttpStatus.OK)
    public List<TrackDetailsDto> findAllByAlbumId(@PathVariable("album_id") Long albumId) {
        return trackMapper.toListOfTrackDetailsDto(albumService.getAlbumTracks(albumId));
    }
}
