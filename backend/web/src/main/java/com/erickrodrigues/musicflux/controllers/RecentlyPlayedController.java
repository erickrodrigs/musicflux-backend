package com.erickrodrigues.musicflux.controllers;

import com.erickrodrigues.musicflux.dtos.RecentlyPlayedDetailsDto;
import com.erickrodrigues.musicflux.mappers.RecentlyPlayedMapper;
import com.erickrodrigues.musicflux.services.RecentlyPlayedService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles/{profile_id}/recently_played")
public class RecentlyPlayedController {

    private final RecentlyPlayedService recentlyPlayedService;
    private final RecentlyPlayedMapper recentlyPlayedMapper;

    public RecentlyPlayedController(RecentlyPlayedService recentlyPlayedService,
                                    RecentlyPlayedMapper recentlyPlayedMapper) {
        this.recentlyPlayedService = recentlyPlayedService;
        this.recentlyPlayedMapper = recentlyPlayedMapper;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<RecentlyPlayedDetailsDto> findAllByProfileId(@PathVariable("profile_id") Long profileId,
                                                             @RequestParam(name = "page", defaultValue = "0") int page,
                                                             @RequestParam(name = "size", defaultValue = "15") int size) {
        return recentlyPlayedService
                .findAllByProfileId(PageRequest.of(page, size), profileId)
                .map(recentlyPlayedMapper::toRecentlyPlayedDetailsDto);
    }
}
