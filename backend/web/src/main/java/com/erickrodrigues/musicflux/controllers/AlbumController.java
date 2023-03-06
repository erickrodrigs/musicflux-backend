package com.erickrodrigues.musicflux.controllers;

import com.erickrodrigues.musicflux.dtos.AlbumDetailsDto;
import com.erickrodrigues.musicflux.mappers.AlbumMapper;
import com.erickrodrigues.musicflux.services.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "albums")
@RestController
public class AlbumController {

    private final AlbumService albumService;
    private final AlbumMapper albumMapper;

    public AlbumController(AlbumService albumService, AlbumMapper albumMapper) {
        this.albumService = albumService;
        this.albumMapper = albumMapper;
    }

    @Operation(summary = "Get all albums by an artist by their id")
    @GetMapping("/artists/{artist_id}/albums")
    @ResponseStatus(HttpStatus.OK)
    public List<AlbumDetailsDto> findAllByArtistId(@PathVariable("artist_id") Long artistId) {
        return albumService
                .findAllByArtistId(artistId)
                .stream()
                .map(albumMapper::toAlbumDetailsDto)
                .toList();
    }
}
