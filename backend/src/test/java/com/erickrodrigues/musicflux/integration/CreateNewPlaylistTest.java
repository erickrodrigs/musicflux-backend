package com.erickrodrigues.musicflux.integration;

import com.erickrodrigues.musicflux.auth.AuthCredentialsDto;
import com.erickrodrigues.musicflux.auth.AuthTokenDto;
import com.erickrodrigues.musicflux.playlist.CreatePlaylistDto;
import com.erickrodrigues.musicflux.playlist.PlaylistDetailsDto;
import org.apache.hc.core5.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql({"/data-test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CreateNewPlaylistTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        final AuthCredentialsDto authCredentialsDto = AuthCredentialsDto
                .builder()
                .username("erickrodrigs")
                .password("carlos123")
                .build();
        final ResponseEntity<AuthTokenDto> response = restTemplate.postForEntity(
                getBaseUrl() + "/auth",
                authCredentialsDto,
                AuthTokenDto.class
        );
        final String token = Objects.requireNonNull(response.getBody()).getToken();

        restTemplate.getInterceptors().add((outReq, bytes, clientHttpReqExec) -> {
            outReq.getHeaders().set(
                    HttpHeaders.AUTHORIZATION, "Bearer " + token
            );

            return clientHttpReqExec.execute(outReq, bytes);
        });
    }

    @Test
    public void createNewPlaylist() {
        final String playlistName = "my favorite songs of all time";
        final CreatePlaylistDto createPlaylistDto = CreatePlaylistDto
                .builder()
                .name(playlistName)
                .build();

        final ResponseEntity<PlaylistDetailsDto> response = restTemplate.postForEntity(
                getBaseUrl() + "/users/me/playlists",
                createPlaylistDto,
                PlaylistDetailsDto.class
        );

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2L, response.getBody().getId());
        assertEquals(1L, response.getBody().getUserId());
        assertEquals(playlistName, response.getBody().getName());
        assertEquals(0, response.getBody().getSongs().size());
    }

    @Test
    public void createNewPlaylistWhenInfoAreInvalid() {
        final CreatePlaylistDto createPlaylistDto = CreatePlaylistDto
                .builder()
                .name("")
                .build();

        assertThrows(HttpClientErrorException.BadRequest.class, () -> restTemplate.postForEntity(
                getBaseUrl() + "/users/me/playlists",
                createPlaylistDto,
                PlaylistDetailsDto.class
        ));
    }

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }
}
