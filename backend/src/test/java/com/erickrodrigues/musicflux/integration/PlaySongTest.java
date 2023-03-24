package com.erickrodrigues.musicflux.integration;

import com.erickrodrigues.musicflux.auth.AuthCredentialsDto;
import com.erickrodrigues.musicflux.auth.AuthTokenDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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

    private static final String INVALID_STATUS_CODE = "Invalid status code";
    private static final String CONTENT_TYPE_IS_NULL = "Content type is null";
    private static final String WRONG_CONTENT_TYPE = "Wrong content type";
    private static final String RESPONSE_BODY_IS_NULL = "Response body is null";

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
    public void shouldReturnASongForTheClientToPlay() {
        // given
        final long songId = 9L;

        // when
        final ResponseEntity<Resource> response = restTemplate.getForEntity(
                getBaseUrl() + "/users/me/songs/" + songId,
                Resource.class
        );

        // then
        assertNotNull(response.getHeaders().getContentType(), CONTENT_TYPE_IS_NULL);
        assertNotNull(response.getBody(), RESPONSE_BODY_IS_NULL);
        assertEquals(200, response.getStatusCode().value(), INVALID_STATUS_CODE);
        assertEquals("audio/mpeg3", response.getHeaders().getContentType().toString(), WRONG_CONTENT_TYPE);
    }

    @Test
    public void shouldRespondWithANotFoundErrorWhenRequestToPlayASongThatDoesNotExist() {
        // given
        final long invalidSongId = 498L;

        // then
        assertThrows(HttpClientErrorException.NotFound.class, () -> restTemplate.getForEntity(
                getBaseUrl() + "/users/me/songs/" + invalidSongId,
                Resource.class
        ));
    }

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }
}
