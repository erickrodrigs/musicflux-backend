package com.erickrodrigues.musicflux.integration;

import com.erickrodrigues.musicflux.auth.AuthCredentialsDto;
import com.erickrodrigues.musicflux.auth.AuthTokenDto;
import com.erickrodrigues.musicflux.playlist.AddOrRemoveTracksDto;
import com.erickrodrigues.musicflux.playlist.PlaylistDetailsDto;
import org.apache.hc.core5.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql({"/data-test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RemoveTrackFromPlaylistTest {

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
    public void removeTrackFromPlaylist() {
        final long playlistId = 1L;
        final List<Long> tracksIds = List.of(4L);
        final HttpEntity<AddOrRemoveTracksDto> requestBody = new HttpEntity<>(AddOrRemoveTracksDto
                .builder()
                .tracksIds(tracksIds)
                .build()
        );
        final ResponseEntity<PlaylistDetailsDto> response = restTemplate.exchange(
                getBaseUrl() + "/playlists/" + playlistId + "/tracks",
                HttpMethod.DELETE,
                requestBody,
                PlaylistDetailsDto.class
        );

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(playlistId, response.getBody().getId());
    }

    @Test
    public void removeTrackFromPlaylistWhenTrackDoesNotExist() {
        final long playlistId = 1L;
        final List<Long> tracksIds = List.of(498L);
        final HttpEntity<AddOrRemoveTracksDto> requestBody = new HttpEntity<>(AddOrRemoveTracksDto
                .builder()
                .tracksIds(tracksIds)
                .build()
        );
        assertThrows(HttpClientErrorException.NotFound.class, () -> restTemplate.exchange(
                getBaseUrl() + "/playlists/" + playlistId + "/tracks",
                HttpMethod.DELETE,
                requestBody,
                Object.class
        ));
    }

    @Test
    public void removeTrackFromPlaylistWhenTrackIsNotIncludedInPlaylist() {
        final long playlistId = 1L;
        final List<Long> tracksIds = List.of(1L);
        final HttpEntity<AddOrRemoveTracksDto> requestBody = new HttpEntity<>(AddOrRemoveTracksDto
                .builder()
                .tracksIds(tracksIds)
                .build()
        );
        assertThrows(HttpClientErrorException.NotFound.class, () -> restTemplate.exchange(
                getBaseUrl() + "/playlists/" + playlistId + "/tracks",
                HttpMethod.DELETE,
                requestBody,
                Object.class
        ));
    }

    @Test
    public void removeTrackFromPlaylistWhenPlaylistDoesNotExist() {
        final long playlistId = 498L;
        final List<Long> tracksIds = List.of(4L);
        final HttpEntity<AddOrRemoveTracksDto> requestBody = new HttpEntity<>(AddOrRemoveTracksDto
                .builder()
                .tracksIds(tracksIds)
                .build()
        );
        assertThrows(HttpClientErrorException.NotFound.class, () -> restTemplate.exchange(
                getBaseUrl() + "/playlists/" + playlistId + "/tracks",
                HttpMethod.DELETE,
                requestBody,
                Object.class
        ));
    }

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }
}
