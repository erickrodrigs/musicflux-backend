package com.erickrodrigues.musicflux.track;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Tag(name = "tracks")
@RestController
@RequiredArgsConstructor
public class TrackController {

    private final TrackService trackService;
    private final TrackMapper trackMapper;
    private final RestTemplate restTemplate;

    @Operation(summary = "Play a track by its id")
    @GetMapping("/users/me/tracks/{track_id}")
    public ResponseEntity<Resource> playTrack(HttpServletRequest request,
                                   @PathVariable("track_id") Long trackId) {
        final String url = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        final Long userId = (Long) request.getAttribute("userId");
        final Track track = trackService.play(userId, trackId);
        final byte[] array = restTemplate.getForObject(url + "/files/" + track.getId() + ".mp3", byte[].class);
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType("audio/mpeg3"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + track.getTitle() + ".mp3\"")
                .body(new ByteArrayResource(Objects.requireNonNull(array)));
    }

    @Operation(summary = "Get all tracks in an album by its id")
    @GetMapping("/albums/{album_id}/tracks")
    @ResponseStatus(HttpStatus.OK)
    public List<TrackDto> findAllByAlbumId(@PathVariable("album_id") Long albumId) {
        return trackMapper.toListOfTrackDetailsDto(trackService.findAllByAlbumId(albumId));
    }

    @Operation(summary = "Get the top five most played tracks by an artist by their id")
    @GetMapping("/artists/{artist_id}/most_played_tracks")
    @ResponseStatus(HttpStatus.OK)
    public List<TrackDto> findMostPlayedTracksByArtistId(@PathVariable("artist_id") Long artistId) {
        return trackMapper.toListOfTrackDetailsDto(trackService.findMostPlayedTracksByArtistId(artistId));
    }
}
