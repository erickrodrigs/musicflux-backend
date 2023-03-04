package com.erickrodrigues.musicflux.controllers;

import com.erickrodrigues.musicflux.dtos.CreateFavoriteDto;
import com.erickrodrigues.musicflux.dtos.FavoriteDetailsDto;
import com.erickrodrigues.musicflux.mappers.FavoriteMapper;
import com.erickrodrigues.musicflux.services.FavoriteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profiles/{profile_id}/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final FavoriteMapper favoriteMapper;

    public FavoriteController(FavoriteService favoriteService, FavoriteMapper favoriteMapper) {
        this.favoriteService = favoriteService;
        this.favoriteMapper = favoriteMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FavoriteDetailsDto likeSong(@PathVariable("profile_id") Long profileId,
                         @RequestBody @Valid CreateFavoriteDto createFavoriteDto) {
        return favoriteMapper.toFavoriteDetailsDto(
                favoriteService.likeSong(profileId, createFavoriteDto.getSongId())
        );
    }

    @DeleteMapping("/{favorite_id}")
    @ResponseStatus(HttpStatus.OK)
    public void dislikeSong(@PathVariable("profile_id") Long profileId,
                            @PathVariable("favorite_id") Long favoriteId) {
        favoriteService.dislikeSong(profileId, favoriteId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FavoriteDetailsDto> findAllByProfileId(@PathVariable("profile_id") Long profileId) {
        return favoriteService
                .findAllByProfileId(profileId)
                .stream()
                .map(favoriteMapper::toFavoriteDetailsDto)
                .toList();
    }
}
