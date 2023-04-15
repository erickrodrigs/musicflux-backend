package com.erickrodrigues.musicflux.end2end;

import com.erickrodrigues.musicflux.auth.AuthCredentialsDto;
import com.erickrodrigues.musicflux.auth.AuthTokenDto;
import org.apache.hc.core5.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql({"/data-test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CheckLikedTracksIT {

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
    public void shouldCheckIfSpecifiedTracksAreLikedOrNot() {
        final ParameterizedTypeReference<Map<Long, Boolean>> responseType = new ParameterizedTypeReference<>() {};
        final ResponseEntity<Map<Long, Boolean>> response = restTemplate.exchange(
                getBaseUrl() + "/me/favorites/check-liked?tracksIds=1,2",
                HttpMethod.GET,
                null,
                responseType
        );

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().get(1L));
        assertTrue(response.getBody().get(2L));
    }

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/v1";
    }
}
