package com.erickrodrigues.musicflux.end2end;

import com.erickrodrigues.musicflux.artist.ArtistDetailsDto;
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
public class GetArtistByTheirIdIT {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void shouldReturnAnArtistByTheirId() {
        final Long artistId = 1L;
        final ResponseEntity<ArtistDetailsDto> response = restTemplate.getForEntity(
                getUrl(artistId), ArtistDetailsDto.class
        );

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(artistId, response.getBody().getId());
    }

    @Test
    public void shouldReturnNotFoundErrorWhenArtistDoesNotExist() {
        final Long artistId = 498L;
        assertThrows(HttpClientErrorException.NotFound.class, () -> restTemplate.getForEntity(
                getUrl(artistId), ArtistDetailsDto.class
        ));
    }

    private String getUrl(Long artistId) {
        return "http://localhost:" + port + "/api/v1/artists/" + artistId;
    }
}
