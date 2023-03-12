package com.erickrodrigues.musicflux.integration;

import com.erickrodrigues.musicflux.song.SongDetailsDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = ITConfig.class)
@Sql({"/data-test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GetSongsInAnAlbumTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void getAlbumById() {
        final ResponseEntity<SongDetailsDto[]> response = restTemplate.getForEntity(
                getUrl(1L),
                SongDetailsDto[].class
        );

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(11, response.getBody().length);
    }

    @Test
    public void getAlbumByIdWhenItDoesNotExist() {
        assertThrows(HttpClientErrorException.NotFound.class, () -> restTemplate.getForEntity(
                getUrl(3L),
                SongDetailsDto[].class
        ));
    }

    private String getUrl(Long albumId) {
        return "http://localhost:" + port + "/albums/" + albumId + "/songs";
    }
}
