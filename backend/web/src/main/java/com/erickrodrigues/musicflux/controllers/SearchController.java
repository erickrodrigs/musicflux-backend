package com.erickrodrigues.musicflux.controllers;

import com.erickrodrigues.musicflux.dtos.SearchResultDto;
import com.erickrodrigues.musicflux.mappers.AlbumMapper;
import com.erickrodrigues.musicflux.mappers.ArtistMapper;
import com.erickrodrigues.musicflux.mappers.PlaylistMapper;
import com.erickrodrigues.musicflux.mappers.SongMapper;
import com.erickrodrigues.musicflux.services.SearchService;
import com.erickrodrigues.musicflux.vo.SearchableType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "catalogue")
@RestController
@RequestMapping("/catalogue")
public class SearchController {

    private final SearchService searchService;
    private final ArtistMapper artistMapper;
    private final AlbumMapper albumMapper;
    private final SongMapper songMapper;
    private final PlaylistMapper playlistMapper;

    public SearchController(SearchService searchService,
                            ArtistMapper artistMapper,
                            AlbumMapper albumMapper,
                            SongMapper songMapper,
                            PlaylistMapper playlistMapper) {
        this.searchService = searchService;
        this.artistMapper = artistMapper;
        this.albumMapper = albumMapper;
        this.songMapper = songMapper;
        this.playlistMapper = playlistMapper;
    }

    @Operation(summary = "Search for artists, albums, songs and/or playlists")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public SearchResultDto search(@RequestParam("types") List<SearchableType> types,
                                  @RequestParam("value") String text) {
        final var searchResult = searchService.execute(types, text);
        final SearchResultDto searchResultDto = new SearchResultDto();
        searchResultDto.setArtists(
                searchResult
                        .getArtists()
                        .stream()
                        .map(artistMapper::toArtistDetailsDto)
                        .toList()
        );
        searchResultDto.setAlbums(
                searchResult
                        .getAlbums()
                        .stream()
                        .map(albumMapper::toAlbumDetailsDto)
                        .toList()
        );
        searchResultDto.setSongs(
                searchResult
                        .getSongs()
                        .stream()
                        .map(songMapper::toSongDetailsDto)
                        .toList()
        );
        searchResultDto.setPlaylists(
                searchResult
                        .getPlaylists()
                        .stream()
                        .map(playlistMapper::toPlaylistDetailsDto)
                        .toList()
        );

        return searchResultDto;
    }
}
