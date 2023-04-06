package com.erickrodrigues.musicflux.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "users")
@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(summary = "Get current user's profile")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDetailsDto getCurrentUserProfile(HttpServletRequest request) {
        final Long userId = (Long) request.getAttribute("userId");
        return userMapper.toUserDetailsDto(userService.findById(userId));
    }
}