package com.erickrodrigues.musicflux.user;

import com.erickrodrigues.musicflux.shared.ResourceAlreadyExistsException;
import com.erickrodrigues.musicflux.shared.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    @Test
    public void shouldGetCurrentUserProfile() throws Exception {
        // given
        final Long userId = 1L;
        final User user = User.builder()
                .id(userId)
                .name("Erick")
                .username("erick123")
                .email("erick@erick.com")
                .build();
        final UserDetailsDto userDetailsDto = UserDetailsDto.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
        when(userService.findById(user.getId())).thenReturn(user);
        when(userMapper.toUserDetailsDto(user)).thenReturn(userDetailsDto);

        // when
        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        final MvcResult mvcResult = mockMvc.perform(get("/me")
                .requestAttr("userId", user.getId()))
                .andExpect(status().isOk())
                .andReturn();
        final UserDetailsDto actualResponse = new ObjectMapper().readValue(
                mvcResult.getResponse().getContentAsString(),
                UserDetailsDto.class
        );

        // then
        assertNotNull(actualResponse);
        assertEquals(userDetailsDto.getId(), actualResponse.getId());
        assertEquals(userDetailsDto.getName(), actualResponse.getName());
        assertEquals(userDetailsDto.getUsername(), actualResponse.getUsername());
        assertEquals(userDetailsDto.getEmail(), actualResponse.getEmail());
        verify(userService, times(1)).findById(user.getId());
        verify(userMapper, times(1)).toUserDetailsDto(user);
    }

    @Test
    public void shouldThrowAnExceptionWhenNotFindingUserWithGivenId() {
        // given
        final Long userId = 1L;
        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        when(userService.findById(userId)).thenThrow(ResourceNotFoundException.class);

        // then
        assertThrows(ServletException.class, () ->
                mockMvc.perform(get("/me")
                        .requestAttr("userId", userId))
        );
    }

    @Test
    public void shouldUpdateCurrentUserInfo() throws Exception {
        // given
        final Long userId = 1L;
        final UpdateUserDto updateUserDto = UpdateUserDto.builder()
                .name("Carlos")
                .password("carlos123")
                .build();
        final User user = User.builder()
                .id(userId)
                .name("Carlos")
                .username("erick123")
                .email("erick@erick.com")
                .password("carlos123")
                .build();
        final UserDetailsDto userDetailsDto = UserDetailsDto.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
        final Map<String, Object> updates = new HashMap<>();
        updates.put("name", updateUserDto.getName());
        updates.put("username", updateUserDto.getUsername());
        updates.put("email", updateUserDto.getEmail());
        updates.put("password", updateUserDto.getPassword());
        when(userService.update(user.getId(), updates)).thenReturn(user);
        when(userMapper.toUserDetailsDto(user)).thenReturn(userDetailsDto);

        // when
        final ObjectMapper objectMapper = new ObjectMapper();
        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        final MvcResult mvcResult = mockMvc.perform(patch("/me")
                .requestAttr("userId", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserDto)))
                .andExpect(status().isOk())
                .andReturn();
        final UserDetailsDto actualResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                UserDetailsDto.class
        );

        // then
        assertNotNull(actualResponse);
        assertEquals(userDetailsDto.getId(), actualResponse.getId());
        assertEquals(userDetailsDto.getName(), actualResponse.getName());
        assertEquals(userDetailsDto.getUsername(), actualResponse.getUsername());
        assertEquals(userDetailsDto.getEmail(), actualResponse.getEmail());
        verify(userService, times(1)).update(user.getId(), updates);
        verify(userMapper, times(1)).toUserDetailsDto(user);
    }

    @Test
    public void shouldThrowAnExceptionWhenUpdatingUserInfoToInvalidInfo() {
        // given
        final Long userId = 1L;
        final UpdateUserDto updateUserDto = UpdateUserDto.builder().build();
        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        final Map<String, Object> updates = new HashMap<>();
        updates.put("name", updateUserDto.getName());
        updates.put("username", updateUserDto.getUsername());
        updates.put("email", updateUserDto.getEmail());
        updates.put("password", updateUserDto.getPassword());
        when(userService.update(userId, updates)).thenThrow(ResourceAlreadyExistsException.class);

        // then
        assertThrows(ServletException.class, () -> mockMvc.perform(patch("/me")
                .requestAttr("userId", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updateUserDto)))
        );
        verify(userService, times(1)).update(userId, updates);
    }
}
