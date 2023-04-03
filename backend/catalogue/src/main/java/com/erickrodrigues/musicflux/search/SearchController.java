package com.erickrodrigues.musicflux.search;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "search")
@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;
    private final SearchMapper searchMapper;

    @Operation(summary = "Search for artists, albums, tracks, playlists or genres")
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public SearchResultDto search(@RequestParam("type") SearchableType type,
                                     @RequestParam("q") String text) {
        return searchMapper.toSearchResultDto(searchService.findAllByTypeAndText(type, text));
    }
}
