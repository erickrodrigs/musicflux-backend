package com.erickrodrigues.musicflux.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

    @Operation(summary = "Update current user's profile info")
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDetailsDto updateCurrentUserProfileInfo(HttpServletRequest request,
                                                       @Valid @RequestBody UpdateUserDto updateUserDto) {
        final Long userId = (Long) request.getAttribute("userId");
        final Map<String, Object> updates = new HashMap<>();
        updates.put("name", updateUserDto.getName());
        updates.put("username", updateUserDto.getUsername());
        updates.put("email", updateUserDto.getEmail());
        updates.put("password", updateUserDto.getPassword());

        return userMapper.toUserDetailsDto(userService.update(userId, updates));
    }
}
