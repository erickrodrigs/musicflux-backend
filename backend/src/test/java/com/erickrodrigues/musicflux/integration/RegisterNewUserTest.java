package com.erickrodrigues.musicflux.integration;

import com.erickrodrigues.musicflux.auth.AuthRegistrationDto;
import com.erickrodrigues.musicflux.auth.AuthTokenDto;
import com.erickrodrigues.musicflux.handlers.ApiError;
import com.erickrodrigues.musicflux.user.User;
import com.erickrodrigues.musicflux.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegisterNewUserTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void registerNewUser() {
        final AuthRegistrationDto authRegistrationDto = AuthRegistrationDto
                .builder()
                .name("Erick")
                .username("erick123")
                .email("erick@erick.com")
                .password("carlos123")
                .build();

        final ResponseEntity<AuthTokenDto> response = restTemplate.postForEntity(
                baseUrl() + "/auth/register",
                authRegistrationDto,
                AuthTokenDto.class
        );

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getToken());
    }

    @Test
    public void registerNewUserWhenInfoAreInvalid() {
        final AuthRegistrationDto authRegistrationDto = AuthRegistrationDto
                .builder()
                .name("")
                .username("")
                .email("")
                .password("")
                .build();

        final ResponseEntity<ApiError> response = restTemplate.postForEntity(
                baseUrl() + "/auth/register",
                authRegistrationDto,
                ApiError.class
        );

        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), response.getBody().getError());
    }

    @Test
    public void registerNewUserWhenTheyAlreadyExist() {
        userRepository.save(
                User.builder().name("Erick").username("erick123").email("erick@erick.com").password("erick123").build()
        );

        final AuthRegistrationDto authRegistrationDto = AuthRegistrationDto
                .builder()
                .name("Erick")
                .username("erick123")
                .email("erick@erick.com")
                .password("carlos123")
                .build();
        final ResponseEntity<ApiError> response = restTemplate.postForEntity(
                baseUrl() + "/auth/register",
                authRegistrationDto,
                ApiError.class
        );

        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), response.getBody().getError());
    }

    private String baseUrl() {
        return "http://localhost:" + port + "/";
    }
}
