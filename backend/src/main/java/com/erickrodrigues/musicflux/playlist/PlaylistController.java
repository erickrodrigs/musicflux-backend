package com.erickrodrigues.musicflux.playlist;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "playlists")
@RestController
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;
    private final PlaylistMapper playlistMapper;

    @Operation(summary = "Create a new playlist")
    @PostMapping("/users/me/playlists")
    @ResponseStatus(HttpStatus.CREATED)
    public PlaylistDetailsDto createPlaylist(HttpServletRequest request,
                                             @RequestBody @Valid CreatePlaylistDto createPlaylistDto) {
        final Long userId = (Long) request.getAttribute("userId");
        return playlistMapper.toPlaylistDetailsDto(playlistService.create(userId, createPlaylistDto.getName()));
    }

    @Operation(summary = "Get all playlists by a user by their id")
    @GetMapping("/users/{user_id}/playlists")
    @ResponseStatus(HttpStatus.OK)
    public List<PlaylistDto> findAllByUserId(@PathVariable("user_id") Long userId) {
        return playlistService
                .findAllByUserId(userId)
                .stream()
                .map(playlistMapper::toPlaylistDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get a specific playlist by their id")
    @GetMapping("/playlists/{playlist_id}")
    @ResponseStatus(HttpStatus.OK)
    public PlaylistDetailsDto findById(@PathVariable("playlist_id") Long playlistId) {
        return playlistMapper.toPlaylistDetailsDto(playlistService.findById(playlistId));
    }

    @Operation(summary = "Add a new song to a playlist")
    @PutMapping("/users/me/playlists/{playlist_id}/songs")
    @ResponseStatus(HttpStatus.CREATED)
    public PlaylistDetailsDto addSong(HttpServletRequest request,
                                      @PathVariable("playlist_id") Long playlistId,
                                      @RequestBody @Valid AddSongToPlaylistDto addSongToPlaylistDto) {
        final Long userId = (Long) request.getAttribute("userId");
        return playlistMapper.toPlaylistDetailsDto(
                playlistService.addSong(userId, playlistId, addSongToPlaylistDto.getSongId())
        );
    }

    @Operation(summary = "Remove a song from a playlist")
    @DeleteMapping("/users/me/playlists/{playlist_id}/songs/{song_id}")
    @ResponseStatus(HttpStatus.OK)
    public PlaylistDetailsDto removeSong(HttpServletRequest request,
                                         @PathVariable("playlist_id") Long playlistId,
                                         @PathVariable("song_id") Long songId) {
        final Long userId = (Long) request.getAttribute("userId");
        return playlistMapper.toPlaylistDetailsDto(playlistService.removeSong(userId, playlistId, songId));
    }

    @Operation(summary = "Delete a playlist by its id")
    @DeleteMapping("/users/me/playlists/{playlist_id}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePlaylist(HttpServletRequest request,
                               @PathVariable("playlist_id") Long playlistId) {
        final Long userId = (Long) request.getAttribute("userId");
        playlistService.deleteById(userId, playlistId);
    }
}
