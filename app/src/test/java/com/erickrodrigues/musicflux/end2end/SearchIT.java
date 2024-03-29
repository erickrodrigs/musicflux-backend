package com.erickrodrigues.musicflux.end2end;

import com.erickrodrigues.musicflux.search.SearchResultDto;
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
public class SearchIT {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void searchForArtists() {
        final ResponseEntity<SearchResultDto> response = restTemplate.getForEntity(
                getUrl() + "?type=artist&q=dep",
                SearchResultDto.class
        );

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getArtists().size());
    }

    @Test
    public void searchForAlbums() {
        final ResponseEntity<SearchResultDto> response = restTemplate.getForEntity(
                getUrl() + "?type=album&q=e",
                SearchResultDto.class
        );

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getAlbums().size());
    }

    @Test
    public void searchForTracks() {
        final ResponseEntity<SearchResultDto> response = restTemplate.getForEntity(
                getUrl() + "?type=track&q=of",
                SearchResultDto.class
        );

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().getTracks().size());
    }

    @Test
    public void searchForPlaylists() {
        final ResponseEntity<SearchResultDto> response = restTemplate.getForEntity(
                getUrl() + "?type=playlist&q=of",
                SearchResultDto.class
        );

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getPlaylists().size());
    }

    @Test
    public void findArtistsAlbumsAndTracksByGenre() {
        final ResponseEntity<SearchResultDto> response = restTemplate.getForEntity(
                getUrl() + "?type=genre&q=Synth-pop",
                SearchResultDto.class
        );

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getArtists().size());
        assertEquals(2, response.getBody().getAlbums().size());
        assertEquals(21, response.getBody().getTracks().size());
    }

    @Test
    public void findArtistsAlbumsAndTracksByGenreWhenItDoesNotExist() {
        final ResponseEntity<SearchResultDto> response = restTemplate.getForEntity(
                getUrl() + "?type=genre&q=unknown",
                SearchResultDto.class
        );

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().getArtists().size());
        assertEquals(0, response.getBody().getAlbums().size());
        assertEquals(0, response.getBody().getTracks().size());
    }

    private String getUrl() {
        return "http://localhost:" + port + "/api/v1" + "/search";
    }
}
