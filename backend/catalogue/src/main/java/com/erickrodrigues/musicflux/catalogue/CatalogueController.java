package com.erickrodrigues.musicflux.catalogue;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "catalogue")
@RestController
@RequiredArgsConstructor
public class CatalogueController {

    private final CatalogueService catalogueService;
    private final CatalogueMapper catalogueMapper;

    @Operation(summary = "Search for artists, albums, tracks, playlists or genres")
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public CatalogueResultDto search(@RequestParam("type") SearchableType type,
                                     @RequestParam("q") String text) {
        return catalogueMapper.toCatalogueResultDto(catalogueService.findAllByTypeAndText(type, text));
    }
}
