package com.erickrodrigues.musicflux.playlist;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "playlists")
@RestController
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;
    private final PlaylistMapper playlistMapper;

    @Operation(summary = "Create a new playlist")
    @PostMapping("/playlists")
    @ResponseStatus(HttpStatus.CREATED)
    public PlaylistDetailsDto createPlaylist(HttpServletRequest request,
                                             @RequestBody @Valid CreatePlaylistDto createPlaylistDto) {
        final Long userId = (Long) request.getAttribute("userId");
        return playlistMapper.toPlaylistDetailsDto(playlistService.create(userId, createPlaylistDto.getName()));
    }

    @Operation(summary = "Get current user's playlists")
    @GetMapping("/me/playlists")
    @ResponseStatus(HttpStatus.OK)
    public List<PlaylistDto> getCurrentUsersPlaylists(HttpServletRequest request) {
        final Long userId = (Long) request.getAttribute("userId");
        return playlistMapper.toListOfPlaylistDto(playlistService.findAllByUserId(userId));
    }

    @Operation(summary = "Get a specific playlist by their id")
    @GetMapping("/playlists/{playlist_id}")
    @ResponseStatus(HttpStatus.OK)
    public PlaylistDetailsDto findById(@PathVariable("playlist_id") Long playlistId) {
        return playlistMapper.toPlaylistDetailsDto(playlistService.findById(playlistId));
    }

    @Operation(summary = "Add a new track to a playlist")
    @PostMapping("/playlists/{playlist_id}/tracks")
    @ResponseStatus(HttpStatus.CREATED)
    public PlaylistDetailsDto addTrack(HttpServletRequest request,
                                      @PathVariable("playlist_id") Long playlistId,
                                      @RequestBody @Valid AddOrRemoveTracksDto addOrRemoveTracksDto) {
        final Long userId = (Long) request.getAttribute("userId");
        return playlistMapper.toPlaylistDetailsDto(
                playlistService.addTracks(userId, playlistId, addOrRemoveTracksDto.getTracksIds())
        );
    }

    @Operation(summary = "Remove a track from a playlist")
    @DeleteMapping("/playlists/{playlist_id}/tracks")
    @ResponseStatus(HttpStatus.OK)
    public PlaylistDetailsDto removeTrack(HttpServletRequest request,
                                         @PathVariable("playlist_id") Long playlistId,
                                         @RequestBody @Valid AddOrRemoveTracksDto addOrRemoveTracksDto) {
        final Long userId = (Long) request.getAttribute("userId");
        return playlistMapper.toPlaylistDetailsDto(
                playlistService.removeTracks(userId, playlistId, addOrRemoveTracksDto.getTracksIds())
        );
    }

    @Operation(summary = "Delete a playlist by its id")
    @DeleteMapping("/playlists/{playlist_id}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePlaylist(HttpServletRequest request,
                               @PathVariable("playlist_id") Long playlistId) {
        final Long userId = (Long) request.getAttribute("userId");
        playlistService.deleteById(userId, playlistId);
    }
}
