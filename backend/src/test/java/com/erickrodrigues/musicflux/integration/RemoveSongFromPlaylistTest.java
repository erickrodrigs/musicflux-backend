package com.erickrodrigues.musicflux.integration;

import com.erickrodrigues.musicflux.auth.AuthCredentialsDto;
import com.erickrodrigues.musicflux.auth.AuthTokenDto;
import com.erickrodrigues.musicflux.playlist.PlaylistDetailsDto;
import org.apache.hc.core5.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
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
public class RemoveSongFromPlaylistTest {

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

        String token = Objects.requireNonNull(response.getBody()).getToken();

        restTemplate.getInterceptors().add((outReq, bytes, clientHttpReqExec) -> {
            outReq.getHeaders().set(
                    HttpHeaders.AUTHORIZATION, "Bearer " + token
            );

            return clientHttpReqExec.execute(outReq, bytes);
        });
    }

    @Test
    public void removeSongFromPlaylist() {
        final long playlistId = 1L, songId = 4L;
        final ResponseEntity<PlaylistDetailsDto> response = restTemplate.exchange(
                getBaseUrl() + "/users/me/playlists/" + playlistId + "/songs/" + songId,
                HttpMethod.DELETE,
                null,
                PlaylistDetailsDto.class
        );

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(playlistId, response.getBody().getId());
        assertTrue(response
                .getBody()
                .getSongs()
                .stream()
                .filter(song -> song.getId() == songId)
                .toList()
                .isEmpty()
        );
    }

    @Test
    public void removeSongFromPlaylistWhenSongDoesNotExist() {
        final long playlistId = 1L, songId = 498L;
        assertThrows(HttpClientErrorException.NotFound.class, () -> restTemplate.exchange(
                getBaseUrl() + "/users/me/playlists/" + playlistId + "/songs/" + songId,
                HttpMethod.DELETE,
                null,
                Object.class
        ));
    }

    @Test
    public void removeSongFromPlaylistWhenSongIsNotIncludedInPlaylist() {
        final long playlistId = 1L, songId = 1L; // song with id 1 does exist
        assertThrows(HttpClientErrorException.NotFound.class, () -> restTemplate.exchange(
                getBaseUrl() + "/users/me/playlists/" + playlistId + "/songs/" + songId,
                HttpMethod.DELETE,
                null,
                Object.class
        ));
    }

    @Test
    public void removeSongFromPlaylistWhenPlaylistDoesNotExist() {
        final long playlistId = 498L, songId = 4L;
        assertThrows(HttpClientErrorException.NotFound.class, () -> restTemplate.exchange(
                getBaseUrl() + "/users/me/playlists/" + playlistId + "/songs/" + songId,
                HttpMethod.DELETE,
                null,
                Object.class
        ));
    }

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }
}
