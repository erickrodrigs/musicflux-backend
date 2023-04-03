package com.erickrodrigues.musicflux.end2end;

import com.erickrodrigues.musicflux.track.TrackDto;
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
public class GetTracksInAnAlbumIT {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void getAlbumById() {
        final ResponseEntity<TrackDto[]> response = restTemplate.getForEntity(
                getUrl(1L),
                TrackDto[].class
        );

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(11, response.getBody().length);
    }

    @Test
    public void getAlbumByIdWhenItDoesNotExist() {
        assertThrows(HttpClientErrorException.NotFound.class, () -> restTemplate.getForEntity(
                getUrl(3L),
                TrackDto[].class
        ));
    }

    private String getUrl(Long albumId) {
        return "http://localhost:" + port + "/albums/" + albumId + "/tracks";
    }
}
