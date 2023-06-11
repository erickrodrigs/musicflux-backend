package com.erickrodrigues.musicflux.end2end;

import com.erickrodrigues.musicflux.playlist.PlaylistDetailsDto;
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
public class GetPlaylistByIdIT {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void getPlaylistDetailsByItsId() {
        final ResponseEntity<PlaylistDetailsDto> response = restTemplate.getForEntity(
                getUrl(1L),
                PlaylistDetailsDto.class
        );

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getTracks().size() > 0);
    }

    @Test
    public void getPlaylistDetailsByItsIdWhenItDoesNotExist() {
        assertThrows(HttpClientErrorException.NotFound.class, () -> restTemplate.getForEntity(
                getUrl(498L),
                PlaylistDetailsDto.class
        ));
    }

    private String getUrl(Long playlistId) {
        return "http://localhost:" + port + "/api/v1" + "/playlists/" + playlistId;
    }
}
