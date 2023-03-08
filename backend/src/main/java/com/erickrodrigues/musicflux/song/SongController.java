package com.erickrodrigues.musicflux.song;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "songs")
@RestController
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;
    private final SongMapper songMapper;

    @Operation(summary = "Play a song by its id")
    @PutMapping("/users/me/songs/{song_id}/play")
    @ResponseStatus(HttpStatus.OK)
    public void playSong(HttpServletRequest request,
                         @PathVariable("song_id") Long songId) {
        final Long userId = (Long) request.getAttribute("userId");
        songService.play(userId, songId);
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
