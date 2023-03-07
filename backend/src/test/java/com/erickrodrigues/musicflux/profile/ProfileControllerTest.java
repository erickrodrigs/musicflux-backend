package com.erickrodrigues.musicflux.profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ProfileControllerTest {

    @Mock
    private ProfileService profileService;

    @Mock
    private ProfileMapper profileMapper;

    @InjectMocks
    private ProfileController profileController;

    @Test
    public void login() throws Exception {
        final String username = "erick123", password = "erick123";
        final LoginDto loginDto = LoginDto.builder().username(username).password(password).build();
        final Profile profile = Profile.builder()
                .id(1L)
                .name("Erick")
                .username(username)
                .email("erick@erick.com")
                .password(password)
                .build();
        final ProfileDetailsDto profileDetailsDto = ProfileDetailsDto.builder()
                .id(profile.getId())
                .name(profile.getName())
                .username(profile.getUsername())
                .email(profile.getEmail())
                .build();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String json = objectMapper.writeValueAsString(loginDto);

        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(profileController).build();

        when(profileService.login(username, password)).thenReturn(profile);
        when(profileMapper.toProfileDetailsDto(profile)).thenReturn(profileDetailsDto);

        final MvcResult mvcResult = mockMvc.perform(post("/profiles/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andReturn();

        final ProfileDetailsDto actualResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                ProfileDetailsDto.class
        );

        assertEquals(profileDetailsDto.getId(), actualResponse.getId());
        assertEquals(profileDetailsDto.getName(), actualResponse.getName());
        assertEquals(profileDetailsDto.getUsername(), actualResponse.getUsername());
        assertEquals(profileDetailsDto.getEmail(), actualResponse.getEmail());
        verify(profileService, times(1)).login(anyString(), anyString());
        verify(profileMapper, times(1)).toProfileDetailsDto(any());
    }

    @Test
    public void createProfile() throws Exception {
        final String name = "Erick", username = "erick123", email = "erick@erick.com", password = "erick123";
        final CreateProfileDto createProfileDto = CreateProfileDto.builder()
                .name(name)
                .username(username)
                .email(email)
                .password(password)
                .build();
        final Profile profile = Profile.builder()
                .id(1L)
                .name(createProfileDto.getName())
                .username(createProfileDto.getUsername())
                .email(createProfileDto.getEmail())
                .password(createProfileDto.getPassword())
                .build();
        final ProfileDetailsDto profileDetailsDto = ProfileDetailsDto.builder()
                .id(profile.getId())
                .name(profile.getName())
                .username(profile.getUsername())
                .email(profile.getEmail())
                .build();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String json = objectMapper.writeValueAsString(createProfileDto);

        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(profileController).build();

        when(profileService.signUp(name, username, email, password)).thenReturn(profile);
        when(profileMapper.toProfileDetailsDto(profile)).thenReturn(profileDetailsDto);

        final MvcResult mvcResult = mockMvc.perform(post("/profiles")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isCreated())
                .andReturn();

        final ProfileDetailsDto actualResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                ProfileDetailsDto.class
        );

        assertEquals(profileDetailsDto.getId(), actualResponse.getId());
        assertEquals(profileDetailsDto.getName(), actualResponse.getName());
        assertEquals(profileDetailsDto.getUsername(), actualResponse.getUsername());
        assertEquals(profileDetailsDto.getEmail(), actualResponse.getEmail());
        verify(profileService, times(1)).signUp(anyString(), anyString(), anyString(), anyString());
        verify(profileMapper, times(1)).toProfileDetailsDto(any());
    }
}
