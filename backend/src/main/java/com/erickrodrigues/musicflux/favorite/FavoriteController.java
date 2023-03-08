package com.erickrodrigues.musicflux.favorite;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "favorites")
@RestController
@RequestMapping("/users/me/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final FavoriteMapper favoriteMapper;

    @Operation(summary = "Like a song")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FavoriteDetailsDto likeSong(HttpServletRequest request,
                                       @RequestBody @Valid CreateFavoriteDto createFavoriteDto) {
        final Long userId = (Long) request.getAttribute("userId");
        return favoriteMapper.toFavoriteDetailsDto(
                favoriteService.likeSong(userId, createFavoriteDto.getSongId())
        );
    }

    @Operation(summary = "Dislike a song")
    @DeleteMapping("/{favorite_id}")
    @ResponseStatus(HttpStatus.OK)
    public void dislikeSong(HttpServletRequest request,
                            @PathVariable("favorite_id") Long favoriteId) {
        final Long userId = (Long) request.getAttribute("userId");
        favoriteService.dislikeSong(userId, favoriteId);
    }

    @Operation(summary = "Get all liked songs by a user by their id")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FavoriteDetailsDto> findAllByUserId(HttpServletRequest request) {
        final Long userId = (Long) request.getAttribute("userId");
        return favoriteService
                .findAllByUserId(userId)
                .stream()
                .map(favoriteMapper::toFavoriteDetailsDto)
                .toList();
    }
}
