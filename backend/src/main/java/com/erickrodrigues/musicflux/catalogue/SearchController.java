package com.erickrodrigues.musicflux.catalogue;

import com.erickrodrigues.musicflux.album.AlbumMapper;
import com.erickrodrigues.musicflux.artist.ArtistMapper;
import com.erickrodrigues.musicflux.playlist.PlaylistMapper;
import com.erickrodrigues.musicflux.song.SongMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "catalogue")
@RestController
@RequestMapping("/catalogue")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;
    private final ArtistMapper artistMapper;
    private final AlbumMapper albumMapper;
    private final SongMapper songMapper;
    private final PlaylistMapper playlistMapper;

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
