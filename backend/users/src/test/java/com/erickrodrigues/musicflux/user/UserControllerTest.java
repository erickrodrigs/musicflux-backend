package com.erickrodrigues.musicflux.user;

import com.erickrodrigues.musicflux.shared.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
}
