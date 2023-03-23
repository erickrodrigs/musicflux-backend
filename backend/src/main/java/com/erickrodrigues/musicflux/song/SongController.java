package com.erickrodrigues.musicflux.song;

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

import java.util.List;

@Tag(name = "songs")
@RestController
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;
    private final SongMapper songMapper;

    @Operation(summary = "Play a song by its id")
    @GetMapping("/users/me/songs/{song_id}")
    public ResponseEntity<Resource> playSong(HttpServletRequest request,
                                   @PathVariable("song_id") Long songId) {
        final Long userId = (Long) request.getAttribute("userId");
        final Song song = songService.play(userId, songId);
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType("audio/mpeg3"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + song.getTitle() + ".mp3\"")
                .body(new ByteArrayResource(song.getData()));
    }

    @Operation(summary = "Get all songs in an album by its id")
    @GetMapping("/albums/{album_id}/songs")
    @ResponseStatus(HttpStatus.OK)
    public List<SongDetailsDto> findAllByAlbumId(@PathVariable("album_id") Long albumId) {
        return songMapper.toListOfSongDetailsDto(songService.findAllByAlbumId(albumId));
    }

    @Operation(summary = "Get the top five most played songs by an artist by their id")
    @GetMapping("/artists/{artist_id}/most_played_songs")
    @ResponseStatus(HttpStatus.OK)
    public List<SongDetailsDto> findMostPlayedSongsByArtistId(@PathVariable("artist_id") Long artistId) {
        return songMapper.toListOfSongDetailsDto(songService.findMostPlayedSongsByArtistId(artistId));
    }
}
