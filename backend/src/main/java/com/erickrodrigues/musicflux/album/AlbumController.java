package com.erickrodrigues.musicflux.album;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "albums")
@RestController
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;
    private final AlbumMapper albumMapper;

    @Operation(summary = "Get all albums by an artist by their id")
    @GetMapping("/artists/{artist_id}/albums")
    @ResponseStatus(HttpStatus.OK)
    public List<AlbumDetailsDto> findAllByArtistId(@PathVariable("artist_id") Long artistId) {
        return albumMapper.toListOfAlbumDetailsDto(albumService.findAllByArtistId(artistId));
    }
}
