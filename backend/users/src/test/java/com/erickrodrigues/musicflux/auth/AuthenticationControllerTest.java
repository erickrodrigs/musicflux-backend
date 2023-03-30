package com.erickrodrigues.musicflux.auth;

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

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    public void register() throws Exception {
        final String name = "Erick", username = "erick123", email = "erick@erick.com", password = "erick123";
        final String token = "myToken";
        final ObjectMapper objectMapper = new ObjectMapper();
        final String json = objectMapper.writeValueAsString(Map.of(
                "name", name,
                "username", username,
                "email", email,
                "password", password
        ));

        when(authenticationService.register(name, username, email, password)).thenReturn(token);

        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
        final MvcResult mvcResult = mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isCreated())
                .andReturn();

        final AuthTokenDto actualResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                AuthTokenDto.class
        );

        assertEquals(token, actualResponse.getToken());
        verify(authenticationService, times(1)).register(
                anyString(), anyString(), anyString(), anyString()
        );
    }

    @Test
    public void authenticate() throws Exception {
        final String username = "erick123", password = "erick123";
        final String token = "myToken";
        final ObjectMapper objectMapper = new ObjectMapper();
        final String json = objectMapper.writeValueAsString(Map.of(
                "username", username,
                "password", password
        ));

        when(authenticationService.authenticate(username, password)).thenReturn(token);

        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
        final MvcResult mvcResult = mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andReturn();

        final AuthTokenDto actualResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                AuthTokenDto.class
        );

        assertEquals(token, actualResponse.getToken());
        verify(authenticationService, times(1)).authenticate(anyString(), anyString());
    }
}
