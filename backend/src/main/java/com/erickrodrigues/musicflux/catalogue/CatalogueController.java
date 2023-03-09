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
import java.util.function.Function;

@Tag(name = "catalogue")
@RestController
@RequestMapping("/catalogue")
@RequiredArgsConstructor
public class CatalogueController {

    private final CatalogueService catalogueService;
    private final ArtistMapper artistMapper;
    private final AlbumMapper albumMapper;
    private final SongMapper songMapper;
    private final PlaylistMapper playlistMapper;

    @Operation(summary = "Search for artists, albums, songs and/or playlists")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CatalogueResultDto search(@RequestParam("types") List<SearchableType> types,
                                     @RequestParam("value") String text) {
        return buildCatalogueResultDto(catalogueService.findAllByTypesAndText(types, text));
    }

    @Operation(summary = "Get all artists, albums and songs by genre name")
    @GetMapping("/genres/{genre}")
    @ResponseStatus(HttpStatus.OK)
    public CatalogueResultDto findAllByGenre(@PathVariable String genre) {
        return buildCatalogueResultDto(catalogueService.findAllByGenreName(genre));
    }

    private CatalogueResultDto buildCatalogueResultDto(final CatalogueResult catalogueResult) {
        return CatalogueResultDto
                .builder()
                .artists(mapEntitiesToDto(catalogueResult.getArtists(), artistMapper::toArtistDetailsDto))
                .albums(mapEntitiesToDto(catalogueResult.getAlbums(), albumMapper::toAlbumDetailsDto))
                .songs(mapEntitiesToDto(catalogueResult.getSongs(), songMapper::toSongDetailsDto))
                .playlists(mapEntitiesToDto(catalogueResult.getPlaylists(), playlistMapper::toPlaylistDetailsDto))
                .build();
    }

    private <T, K> List<K> mapEntitiesToDto(final List<T> entities, final Function<T, K> mapFunc) {
        return entities
                .stream()
                .map(mapFunc)
                .toList();
    }
}
