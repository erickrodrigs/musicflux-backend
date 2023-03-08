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
@RequestMapping("/users/me/playlists")
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;
    private final PlaylistMapper playlistMapper;

    @Operation(summary = "Create a new playlist")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlaylistDetailsDto createPlaylist(HttpServletRequest request,
                                             @RequestBody @Valid CreatePlaylistDto createPlaylistDto) {
        final Long userId = (Long) request.getAttribute("userId");
        return playlistMapper.toPlaylistDetailsDto(playlistService.create(userId, createPlaylistDto.getName()));
    }

    @Operation(summary = "Get all playlists by a user by their id")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PlaylistDetailsDto> findAllByUserId(HttpServletRequest request) {
        final Long userId = (Long) request.getAttribute("userId");
        return playlistService
                .findAllByUserId(userId)
                .stream()
                .map(playlistMapper::toPlaylistDetailsDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Add a new song to a playlist")
    @PutMapping("/{playlist_id}/songs")
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
    @DeleteMapping("/{playlist_id}/songs/{song_id}")
    @ResponseStatus(HttpStatus.OK)
    public PlaylistDetailsDto removeSong(HttpServletRequest request,
                                         @PathVariable("playlist_id") Long playlistId,
                                         @PathVariable("song_id") Long songId) {
        final Long userId = (Long) request.getAttribute("userId");
        return playlistMapper.toPlaylistDetailsDto(playlistService.removeSong(userId, playlistId, songId));
    }

    @Operation(summary = "Delete a playlist by its id")
    @DeleteMapping("/{playlist_id}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePlaylist(HttpServletRequest request,
                               @PathVariable("playlist_id") Long playlistId) {
        final Long userId = (Long) request.getAttribute("userId");
        playlistService.deleteById(userId, playlistId);
    }
}
