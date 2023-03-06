package com.erickrodrigues.musicflux.controllers;

import com.erickrodrigues.musicflux.dtos.SongDetailsDto;
import com.erickrodrigues.musicflux.mappers.SongMapper;
import com.erickrodrigues.musicflux.services.SongService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "songs")
@RestController
public class SongController {

    private final SongService songService;
    private final SongMapper songMapper;

    public SongController(SongService songService, SongMapper songMapper) {
        this.songService = songService;
        this.songMapper = songMapper;
    }

    @Operation(summary = "Play a song by its id")
    @PutMapping("/profiles/{profile_id}/songs/{song_id}/play")
    @ResponseStatus(HttpStatus.OK)
    public void playSong(@PathVariable("profile_id") Long profileId,
                         @PathVariable("song_id") Long songId) {
        songService.play(profileId, songId);
    }

    @Operation(summary = "Get all songs in an album by its id")
    @GetMapping("/albums/{album_id}/songs")
    @ResponseStatus(HttpStatus.OK)
    public List<SongDetailsDto> findAllByAlbumId(@PathVariable("album_id") Long albumId) {
        return songService.findAllByAlbumId(albumId)
                .stream()
                .map(songMapper::toSongDetailsDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get the top five most played songs by an artist by their id")
    @GetMapping("/artists/{artist_id}/most_played_songs")
    @ResponseStatus(HttpStatus.OK)
    public List<SongDetailsDto> findMostPlayedSongsByArtistId(@PathVariable("artist_id") Long artistId) {
        return songService.findMostPlayedSongsByArtistId(artistId)
                .stream()
                .map(songMapper::toSongDetailsDto)
                .collect(Collectors.toList());
    }
}
