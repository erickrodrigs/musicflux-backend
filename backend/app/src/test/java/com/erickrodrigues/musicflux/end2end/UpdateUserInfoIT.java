package com.erickrodrigues.musicflux.end2end;

import com.erickrodrigues.musicflux.auth.AuthCredentialsDto;
import com.erickrodrigues.musicflux.auth.AuthTokenDto;
import com.erickrodrigues.musicflux.user.UpdateUserDto;
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
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql({"/data-test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UpdateUserInfoIT {

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
    public void shouldUpdateUsersName() {
        // given
        final UpdateUserDto updateUserDto = UpdateUserDto
                .builder()
                .name("Carlos")
                .build();

        // when
        final ResponseEntity<UpdateUserDto> response = restTemplate.exchange(
                getBaseUrl() + "/me",
                HttpMethod.PATCH,
                new HttpEntity<>(updateUserDto),
                UpdateUserDto.class
        );

        // then
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(updateUserDto.getName(), response.getBody().getName());
    }

    @Test
    public void shouldUpdateUsersUsername() {
        // given
        final UpdateUserDto updateUserDto = UpdateUserDto
                .builder()
                .username("carlos123")
                .build();

        // when
        final ResponseEntity<UpdateUserDto> response = restTemplate.exchange(
                getBaseUrl() + "/me",
                HttpMethod.PATCH,
                new HttpEntity<>(updateUserDto),
                UpdateUserDto.class
        );

        // then
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(updateUserDto.getUsername(), response.getBody().getUsername());
    }

    @Test
    public void shouldUpdateUsersEmail() {
        // given
        final UpdateUserDto updateUserDto = UpdateUserDto
                .builder()
                .email("carlos@carlos.com")
                .build();

        // when
        final ResponseEntity<UpdateUserDto> response = restTemplate.exchange(
                getBaseUrl() + "/me",
                HttpMethod.PATCH,
                new HttpEntity<>(updateUserDto),
                UpdateUserDto.class
        );

        // then
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(updateUserDto.getEmail(), response.getBody().getEmail());
    }

    @Test
    public void shouldUpdateUsersPassword() {
        // given
        final UpdateUserDto updateUserDto = UpdateUserDto
                .builder()
                .password("erick123")
                .build();

        // when
        final ResponseEntity<UpdateUserDto> response = restTemplate.exchange(
                getBaseUrl() + "/me",
                HttpMethod.PATCH,
                new HttpEntity<>(updateUserDto),
                UpdateUserDto.class
        );

        // then
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCode().value());
    }

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/v1";
    }
}
