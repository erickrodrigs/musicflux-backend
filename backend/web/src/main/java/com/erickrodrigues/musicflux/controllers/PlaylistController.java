package com.erickrodrigues.musicflux.controllers;

import com.erickrodrigues.musicflux.dtos.AddSongDto;
import com.erickrodrigues.musicflux.dtos.CreatePlaylistDto;
import com.erickrodrigues.musicflux.dtos.PlaylistDetailsDto;
import com.erickrodrigues.musicflux.mappers.PlaylistMapper;
import com.erickrodrigues.musicflux.services.PlaylistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "playlists")
@RestController
@RequestMapping("/profiles/{profile_id}/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;
    private final PlaylistMapper playlistMapper;

    public PlaylistController(PlaylistService playlistService, PlaylistMapper playlistMapper) {
        this.playlistService = playlistService;
        this.playlistMapper = playlistMapper;
    }

    @Operation(summary = "Create a new playlist")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlaylistDetailsDto createPlaylist(@PathVariable("profile_id") Long profileId,
                                             @RequestBody @Valid CreatePlaylistDto createPlaylistDto) {
        return playlistMapper.toPlaylistDetailsDto(playlistService.create(profileId, createPlaylistDto.getName()));
    }

    @Operation(summary = "Get all playlists by a profile by their id")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PlaylistDetailsDto> findAllByProfileId(@PathVariable("profile_id") Long profileId) {
        return playlistService
                .findAllByProfileId(profileId)
                .stream()
                .map(playlistMapper::toPlaylistDetailsDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Add a new song to a playlist")
    @PutMapping("/{playlist_id}/songs")
    @ResponseStatus(HttpStatus.CREATED)
    public PlaylistDetailsDto addSong(@PathVariable("profile_id") Long profileId,
                                      @PathVariable("playlist_id") Long playlistId,
                                      @RequestBody @Valid AddSongDto addSongDto) {
        return playlistMapper.toPlaylistDetailsDto(
                playlistService.addSong(profileId, playlistId, addSongDto.getSongId())
        );
    }

    @Operation(summary = "Remove a song from a playlist")
    @DeleteMapping("/{playlist_id}/songs/{song_id}")
    @ResponseStatus(HttpStatus.OK)
    public PlaylistDetailsDto removeSong(@PathVariable("profile_id") Long profileId,
                                         @PathVariable("playlist_id") Long playlistId,
                                         @PathVariable("song_id") Long songId) {
        return playlistMapper.toPlaylistDetailsDto(playlistService.removeSong(profileId, playlistId, songId));
    }

    @Operation(summary = "Delete a playlist by its id")
    @DeleteMapping("/{playlist_id}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePlaylist(@PathVariable("profile_id") Long profileId,
                               @PathVariable("playlist_id") Long playlistId) {
        playlistService.deleteById(profileId, playlistId);
    }
}
