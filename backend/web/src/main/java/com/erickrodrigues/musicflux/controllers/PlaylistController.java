package com.erickrodrigues.musicflux.controllers;

import com.erickrodrigues.musicflux.dtos.CreatePlaylistDto;
import com.erickrodrigues.musicflux.dtos.PlaylistDetailsDto;
import com.erickrodrigues.musicflux.mappers.PlaylistMapper;
import com.erickrodrigues.musicflux.services.PlaylistService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/profiles/{profile_id}/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;
    private final PlaylistMapper playlistMapper;

    public PlaylistController(PlaylistService playlistService, PlaylistMapper playlistMapper) {
        this.playlistService = playlistService;
        this.playlistMapper = playlistMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlaylistDetailsDto createPlaylist(@PathVariable("profile_id") Long profileId,
                                             @RequestBody @Valid CreatePlaylistDto createPlaylistDto) {
        return playlistMapper.toPlaylistDetailsDto(playlistService.create(profileId, createPlaylistDto.getName()));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Set<PlaylistDetailsDto> findAllByProfileId(@PathVariable("profile_id") Long profileId) {
        return playlistService
                .findAllByProfileId(profileId)
                .stream()
                .map(playlistMapper::toPlaylistDetailsDto)
                .collect(Collectors.toSet());
    }
}
