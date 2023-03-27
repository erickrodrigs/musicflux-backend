package com.erickrodrigues.musicflux.integration;

import com.erickrodrigues.musicflux.catalogue.CatalogueResultDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql({"/data-test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CatalogueSearchTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void searchForArtistsAlbumsAndSongs() {
        final ResponseEntity<CatalogueResultDto> response = restTemplate.getForEntity(
                getUrl() + "?types=artist,album,song,playlist&value=" + "of",
                CatalogueResultDto.class
        );

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().getArtists().size());
        assertEquals(0, response.getBody().getAlbums().size());
        assertEquals(3, response.getBody().getSongs().size());
        assertEquals(1, response.getBody().getPlaylists().size());
    }

    private String getUrl() {
        return "http://localhost:" + port + "/catalogue";
    }
}
