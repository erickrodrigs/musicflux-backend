package com.erickrodrigues.musicflux.integration;

import com.erickrodrigues.musicflux.album.AlbumDetailsDto;
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
public class GetAlbumsByAnArtistTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void getAlbumsByAnArtist() {
        final ResponseEntity<AlbumDetailsDto[]> response = restTemplate.getForEntity(
                getUrl(1L),
                AlbumDetailsDto[].class
        );

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().length);
    }

    @Test
    public void getAlbumsByAnArtistThatDoesNotExist() {
        assertThrows(HttpClientErrorException.NotFound.class, () -> restTemplate.getForEntity(
                getUrl(2L),
                AlbumDetailsDto[].class
        ));
    }

    private String getUrl(Long artistId) {
        return "http://localhost:" + port + "/artists/" + artistId + "/albums";
    }
}
