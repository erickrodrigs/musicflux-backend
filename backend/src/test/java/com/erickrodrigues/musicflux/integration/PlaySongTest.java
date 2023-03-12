package com.erickrodrigues.musicflux.integration;

import com.erickrodrigues.musicflux.auth.AuthCredentialsDto;
import com.erickrodrigues.musicflux.auth.AuthTokenDto;
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
public class PlaySongTest {

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
    public void playSong() {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);

        final long songId = 9L;
        final ResponseEntity<Object> response = restTemplate.exchange(
                getBaseUrl() + "/users/me/songs/" + songId + "/play",
                HttpMethod.PUT,
                requestEntity,
                Object.class
        );

        assertEquals(200, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    public void playSongWhenItDoesNotExist() {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);

        final long songId = 498L;

        assertThrows(HttpClientErrorException.NotFound.class, () -> restTemplate.exchange(
                getBaseUrl() + "/users/me/songs/" + songId + "/play",
                HttpMethod.PUT,
                requestEntity,
                Object.class
        ));
    }

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }
}
