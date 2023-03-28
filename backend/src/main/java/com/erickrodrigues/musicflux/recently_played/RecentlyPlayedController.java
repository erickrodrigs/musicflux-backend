package com.erickrodrigues.musicflux.recently_played;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "recently played")
@RestController
@RequestMapping("/users/me/recently_played")
@RequiredArgsConstructor
public class RecentlyPlayedController {

    private final RecentlyPlayedService recentlyPlayedService;
    private final RecentlyPlayedMapper recentlyPlayedMapper;

    @Operation(summary = "Get recently played tracks by a user by their id")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<RecentlyPlayedDetailsDto> findAllByUserId(HttpServletRequest request,
                                                          @RequestParam(name = "page", defaultValue = "0") int page,
                                                          @RequestParam(name = "size", defaultValue = "15") int size) {
        final Long userId = (Long) request.getAttribute("userId");
        return recentlyPlayedService
                .findAllByUserId(PageRequest.of(page, size), userId)
                .map(recentlyPlayedMapper::toRecentlyPlayedDetailsDto);
    }
}
