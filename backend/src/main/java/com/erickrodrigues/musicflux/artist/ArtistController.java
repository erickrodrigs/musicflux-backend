package com.erickrodrigues.musicflux.artist;

import com.erickrodrigues.musicflux.album.AlbumDetailsDto;
import com.erickrodrigues.musicflux.album.AlbumMapper;
import com.erickrodrigues.musicflux.track.TrackDto;
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

@Tag(name = "artists")
@RestController
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;
    private final AlbumMapper albumMapper;
    private final TrackMapper trackMapper;

    @Operation(summary = "Get artist's albums by their id")
    @GetMapping("/artists/{artist_id}/albums")
    @ResponseStatus(HttpStatus.OK)
    public List<AlbumDetailsDto> getArtistAlbums(@PathVariable("artist_id") Long artistId) {
        return albumMapper.toListOfAlbumDetailsDto(artistService.getArtistAlbums(artistId));
    }

    @Operation(summary = "Get artist's top tracks by their id")
    @GetMapping("/artists/{artist_id}/top-tracks")
    @ResponseStatus(HttpStatus.OK)
    public List<TrackDto> getArtistTopTracks(@PathVariable("artist_id") Long artistId) {
        return trackMapper.toListOfTrackDetailsDto(artistService.getTopTracks(artistId));
    }
}
