package com.erickrodrigues.musicflux.artist;

import com.erickrodrigues.musicflux.album.AlbumDetailsDto;
import com.erickrodrigues.musicflux.album.AlbumMapper;
import com.erickrodrigues.musicflux.track.TrackDto;
import com.erickrodrigues.musicflux.track.TrackMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "artists")
@RestController
@RequestMapping("/artists/{artist_id}")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;
    private final ArtistMapper artistMapper;
    private final AlbumMapper albumMapper;
    private final TrackMapper trackMapper;

    @Operation(summary = "Get artist by their id")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ArtistDetailsDto getArtist(@PathVariable("artist_id") Long artistId) {
        return artistMapper.toArtistDetailsDto(artistService.findById(artistId));
    }

    @Operation(summary = "Get artist's albums by their id")
    @GetMapping("/albums")
    @ResponseStatus(HttpStatus.OK)
    public List<AlbumDetailsDto> getArtistAlbums(@PathVariable("artist_id") Long artistId) {
        return albumMapper.toListOfAlbumDetailsDto(artistService.getArtistAlbums(artistId));
    }

    @Operation(summary = "Get artist's top tracks by their id")
    @GetMapping("/top-tracks")
    @ResponseStatus(HttpStatus.OK)
    public List<TrackDto> getArtistTopTracks(@PathVariable("artist_id") Long artistId) {
        return trackMapper.toListOfTrackDetailsDto(artistService.getTopTracks(artistId));
    }
}
