package com.erickrodrigues.musicflux.repositories;

import com.erickrodrigues.musicflux.domain.Playlist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PlaylistRepositoryTest {

    @Autowired
    private PlaylistRepository playlistRepository;

    private Playlist playlist1, playlist2, playlist3;

    @BeforeEach
    public void setUp() {
        playlist1 = Playlist.builder().id(1L).name("my fav songs - heavy metal").build();
        playlist2 = Playlist.builder().id(2L).name("THESE ARE MY FAV SONGS OF ALL TIME").build();
        playlist3 = Playlist.builder().id(3L).name("funk").build();

        playlistRepository.save(playlist1);
        playlistRepository.save(playlist2);
        playlistRepository.save(playlist3);
    }

    @Test
    public void findAllByNameContainingIgnoreCase() {
        Set<Playlist> playlists;

        playlists = playlistRepository.findAllByNameContainingIgnoreCase("my fav songs");

        assertEquals(2, playlists.size());
        assertTrue(playlists.containsAll(Set.of(playlist1, playlist2)));

        playlists = playlistRepository.findAllByNameContainingIgnoreCase("unk");

        assertEquals(1, playlists.size());
        assertTrue(playlists.contains(playlist3));
    }
}
