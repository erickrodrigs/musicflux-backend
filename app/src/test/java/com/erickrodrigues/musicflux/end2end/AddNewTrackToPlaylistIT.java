package com.erickrodrigues.musicflux.end2end;

import com.erickrodrigues.musicflux.auth.AuthCredentialsDto;
import com.erickrodrigues.musicflux.auth.AuthTokenDto;
import com.erickrodrigues.musicflux.playlist.AddOrRemoveTracksDto;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql({"/data-test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AddNewTrackToPlaylistIT {

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
    public void addNewTrackToPlaylist() {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        final Long playlistId = 1L;
        final List<Long> tracksIds = List.of(18L, 19L);
        final AddOrRemoveTracksDto addOrRemoveTracksDto = AddOrRemoveTracksDto
                .builder()
                .tracksIds(tracksIds)
                .build();
        final HttpEntity<AddOrRemoveTracksDto> requestEntity = new HttpEntity<>(addOrRemoveTracksDto, headers);
        final ResponseEntity<PlaylistDetailsDto> response = restTemplate.exchange(
                getBaseUrl() + "/playlists/" + playlistId + "/tracks",
                HttpMethod.POST,
                requestEntity,
                PlaylistDetailsDto.class
        );

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(playlistId, response.getBody().getId());
    }

    @Test
    public void addTrackToPlaylistWhenPlaylistIdIsInvalid() {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        final long playlistId = 498L;
        final List<Long> tracksIds = List.of(18L, 19L);
        final AddOrRemoveTracksDto addOrRemoveTracksDto = AddOrRemoveTracksDto
                .builder()
                .tracksIds(tracksIds)
                .build();
        final HttpEntity<AddOrRemoveTracksDto> requestEntity = new HttpEntity<>(addOrRemoveTracksDto, headers);

        assertThrows(HttpClientErrorException.NotFound.class, () -> restTemplate.exchange(
                getBaseUrl() + "/playlists/" + playlistId + "/tracks",
                HttpMethod.POST,
                requestEntity,
                PlaylistDetailsDto.class
        ));
    }

    @Test
    public void addTrackToPlaylistWhenTrackIdIsInvalid() {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        final long playlistId = 1L;
        final List<Long> tracksIds = List.of(498L);
        final AddOrRemoveTracksDto addOrRemoveTracksDto = AddOrRemoveTracksDto
                .builder()
                .tracksIds(tracksIds)
                .build();
        final HttpEntity<AddOrRemoveTracksDto> requestEntity = new HttpEntity<>(addOrRemoveTracksDto, headers);

        assertThrows(HttpClientErrorException.NotFound.class, () -> restTemplate.exchange(
                getBaseUrl() + "/playlists/" + playlistId + "/tracks",
                HttpMethod.POST,
                requestEntity,
                PlaylistDetailsDto.class
        ));
    }

    @Test
    public void addTrackToPlaylistWhenTrackIsAlreadyIncluded() {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        final long playlistId = 1L;
        final List<Long> tracksIds = List.of(3L);
        final AddOrRemoveTracksDto addOrRemoveTracksDto = AddOrRemoveTracksDto
                .builder()
                .tracksIds(tracksIds)
                .build();
        final HttpEntity<AddOrRemoveTracksDto> requestEntity = new HttpEntity<>(addOrRemoveTracksDto, headers);

        assertThrows(HttpClientErrorException.BadRequest.class, () -> restTemplate.exchange(
                getBaseUrl() + "" +
                        "/playlists/" + playlistId + "/tracks",
                HttpMethod.POST,
                requestEntity,
                PlaylistDetailsDto.class
        ));
    }

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/v1";
    }
}
