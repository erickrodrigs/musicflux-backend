package com.erickrodrigues.musicflux.integration;

import com.erickrodrigues.musicflux.auth.AuthCredentialsDto;
import com.erickrodrigues.musicflux.auth.AuthTokenDto;
import com.erickrodrigues.musicflux.playlist.AddSongToPlaylistDto;
import com.erickrodrigues.musicflux.playlist.PlaylistDetailsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = ITConfig.class)
@Sql({"/data-test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AddNewSongToPlaylistTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    private String token;

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

        token = Objects.requireNonNull(response.getBody()).getToken();
    }

    @Test
    public void addNewSongToPlaylist() {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        final Long playlistId = 1L, songId = 18L;
        final AddSongToPlaylistDto addSongToPlaylistDto = AddSongToPlaylistDto
                .builder()
                .songId(songId)
                .build();
        final HttpEntity<AddSongToPlaylistDto> requestEntity = new HttpEntity<>(addSongToPlaylistDto, headers);
        final ResponseEntity<PlaylistDetailsDto> response = restTemplate.exchange(
                getBaseUrl() + "/users/me/playlists/" + playlistId + "/songs",
                HttpMethod.PUT,
                requestEntity,
                PlaylistDetailsDto.class
        );

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(playlistId, response.getBody().getId());
    }

    @Test
    public void addSongToPlaylistWhenPlaylistIdIsInvalid() {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        final long playlistId = 498L, songId = 18L;
        final AddSongToPlaylistDto addSongToPlaylistDto = AddSongToPlaylistDto
                .builder()
                .songId(songId)
                .build();
        final HttpEntity<AddSongToPlaylistDto> requestEntity = new HttpEntity<>(addSongToPlaylistDto, headers);

        assertThrows(HttpClientErrorException.NotFound.class, () -> restTemplate.exchange(
                getBaseUrl() + "/users/me/playlists/" + playlistId + "/songs",
                HttpMethod.PUT,
                requestEntity,
                PlaylistDetailsDto.class
        ));
    }

    @Test
    public void addSongToPlaylistWhenSongIdIsInvalid() {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        final long playlistId = 1L, songId = 498L;
        final AddSongToPlaylistDto addSongToPlaylistDto = AddSongToPlaylistDto
                .builder()
                .songId(songId)
                .build();
        final HttpEntity<AddSongToPlaylistDto> requestEntity = new HttpEntity<>(addSongToPlaylistDto, headers);

        assertThrows(HttpClientErrorException.NotFound.class, () -> restTemplate.exchange(
                getBaseUrl() + "/users/me/playlists/" + playlistId + "/songs",
                HttpMethod.PUT,
                requestEntity,
                PlaylistDetailsDto.class
        ));
    }

    @Test
    public void addSongToPlaylistWhenSongIsAlreadyIncluded() {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        final long playlistId = 1L, songId = 3L;
        final AddSongToPlaylistDto addSongToPlaylistDto = AddSongToPlaylistDto
                .builder()
                .songId(songId)
                .build();
        final HttpEntity<AddSongToPlaylistDto> requestEntity = new HttpEntity<>(addSongToPlaylistDto, headers);

        assertThrows(HttpClientErrorException.BadRequest.class, () -> restTemplate.exchange(
                getBaseUrl() + "/users/me/playlists/" + playlistId + "/songs",
                HttpMethod.PUT,
                requestEntity,
                PlaylistDetailsDto.class
        ));
    }

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }
}
