package com.erickrodrigues.musicflux.genre;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "genres")
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;
    private final GenreMapper genreMapper;

    @Operation(summary = "Get all genres")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GenreDto> findAll() {
        return genreMapper.toListOfGenreDto(genreService.findAll());
    }
}
