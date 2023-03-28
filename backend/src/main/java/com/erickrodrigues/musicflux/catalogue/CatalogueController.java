package com.erickrodrigues.musicflux.catalogue;

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
public class CatalogueController {

    private final CatalogueService catalogueService;
    private final CatalogueMapper catalogueMapper;

    @Operation(summary = "Search for artists, albums, tracks and/or playlists")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CatalogueResultDto search(@RequestParam("types") List<SearchableType> types,
                                     @RequestParam("value") String text) {
        return catalogueMapper.toCatalogueResultDto(catalogueService.findAllByTypesAndText(types, text));
    }

    @Operation(summary = "Get all artists, albums and tracks by genre name")
    @GetMapping("/genres/{genre}")
    @ResponseStatus(HttpStatus.OK)
    public CatalogueResultDto findAllByGenre(@PathVariable String genre) {
        return catalogueMapper.toCatalogueResultDto(catalogueService.findAllByGenreName(genre));
    }
}
