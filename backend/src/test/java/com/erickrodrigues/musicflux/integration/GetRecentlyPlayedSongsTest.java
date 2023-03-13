package com.erickrodrigues.musicflux.integration;

import com.erickrodrigues.musicflux.auth.AuthCredentialsDto;
import com.erickrodrigues.musicflux.auth.AuthTokenDto;
import com.erickrodrigues.musicflux.recently_played.RecentlyPlayedDetailsDto;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.hc.core5.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = ITConfig.class)
@Sql({"/data-test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GetRecentlyPlayedSongsTest {

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
    public void getRecentlyPlayedSongs() {
        final ResponseEntity<PaginatedResponse<RecentlyPlayedDetailsDto>> response = restTemplate.exchange(
                getBaseUrl() + "/users/me/recently_played",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getContent().size() > 0);
    }

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }

    @SuppressWarnings("unused")
    private static class PaginatedResponse<T> extends PageImpl<T> {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public PaginatedResponse(@JsonProperty("content") List<T> content,
                                 @JsonProperty("number") int number,
                                 @JsonProperty("size") int size,
                                 @JsonProperty("totalElements") Long totalElements,
                                 @JsonProperty("pageable") JsonNode pageable,
                                 @JsonProperty("last") boolean last,
                                 @JsonProperty("totalPages") int totalPages,
                                 @JsonProperty("sort") JsonNode sort,
                                 @JsonProperty("first") boolean first,
                                 @JsonProperty("empty") boolean empty) {

            super(content, PageRequest.of(number, size), totalElements);
        }

        public PaginatedResponse(List<T> content, Pageable pageable, long total) {
            super(content, pageable, total);
        }

        public PaginatedResponse(List<T> content) {
            super(content);
        }

        public PaginatedResponse() {
            super(new ArrayList<>());
        }
    }
}
