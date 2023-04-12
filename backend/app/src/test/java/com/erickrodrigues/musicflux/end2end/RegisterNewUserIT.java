package com.erickrodrigues.musicflux.end2end;

import com.erickrodrigues.musicflux.auth.AuthRegistrationDto;
import com.erickrodrigues.musicflux.auth.AuthTokenDto;
import com.erickrodrigues.musicflux.handlers.ApiError;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql({"/data-test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RegisterNewUserIT {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void registerNewUser() {
        final AuthRegistrationDto authRegistrationDto = AuthRegistrationDto
                .builder()
                .name("Erick")
                .username("erick123")
                .email("email@email.com")
                .password("carlos123")
                .build();

        final ResponseEntity<AuthTokenDto> response = restTemplate.postForEntity(
                getUrl(),
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

        assertThrows(HttpClientErrorException.BadRequest.class, () -> restTemplate.postForEntity(
                getUrl(),
                authRegistrationDto,
                ApiError.class
        ));
    }

    @Test
    public void registerNewUserWhenTheyAlreadyExist() {
        final AuthRegistrationDto authRegistrationDto = AuthRegistrationDto
                .builder()
                .name("Erick")
                .username("erickrodrigs")
                .email("erick@erick.com")
                .password("carlos123")
                .build();

        assertThrows(HttpClientErrorException.BadRequest.class, () -> restTemplate.postForEntity(
                getUrl(),
                authRegistrationDto,
                ApiError.class
        ));
    }

    private String getUrl() {
        return "http://localhost:" + port + "/api/v1" + "/auth/register";
    }
}
