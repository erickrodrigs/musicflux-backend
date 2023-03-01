package com.erickrodrigues.musicflux.controllers;

import com.erickrodrigues.musicflux.dtos.SongDetailsDto;
import com.erickrodrigues.musicflux.mappers.SongMapper;
import com.erickrodrigues.musicflux.services.SongService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class SongController {

    private final SongService songService;
    private final SongMapper songMapper;

    public SongController(SongService songService, SongMapper songMapper) {
        this.songService = songService;
        this.songMapper = songMapper;
    }

    @GetMapping("/albums/{album_id}/songs")
    @ResponseStatus(HttpStatus.OK)
    public Set<SongDetailsDto> findAllByAlbumId(@PathVariable("album_id") Long albumId) {
        return songService.findAllByAlbumId(albumId)
                .stream()
                .map(songMapper::toSongDetailsDto)
                .collect(Collectors.toSet());
    }
}
