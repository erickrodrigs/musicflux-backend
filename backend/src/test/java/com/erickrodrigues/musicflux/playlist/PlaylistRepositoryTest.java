package com.erickrodrigues.musicflux.playlist;

import com.erickrodrigues.musicflux.user.User;
import com.erickrodrigues.musicflux.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PlaylistRepositoryTest {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    private Playlist playlist1, playlist2, playlist3;

    @BeforeEach
    public void setUp() {
        user = User.builder().id(1L).build();
        playlist1 = Playlist.builder().id(1L).user(user).name("my fav songs - heavy metal").build();
        playlist2 = Playlist.builder().id(2L).user(user).name("THESE ARE MY FAV SONGS OF ALL TIME").build();
        playlist3 = Playlist.builder().id(3L).user(user).name("funk").build();

        userRepository.save(user);
        playlistRepository.save(playlist1);
        playlistRepository.save(playlist2);
        playlistRepository.save(playlist3);
    }

    @Test
    public void findAllByNameContainingIgnoreCase() {
        List<Playlist> playlists;

        playlists = playlistRepository.findAllByNameContainingIgnoreCase("my fav songs");

        assertEquals(2, playlists.size());
        assertTrue(playlists.containsAll(List.of(playlist1, playlist2)));

        playlists = playlistRepository.findAllByNameContainingIgnoreCase("unk");

        assertEquals(1, playlists.size());
        assertTrue(playlists.contains(playlist3));
    }

    @Test
    public void findAllByUserId() {
        final List<Playlist> playlists = playlistRepository.findAllByUserId(user.getId());

        assertEquals(3, playlists.size());
        assertTrue(playlists.containsAll(List.of(playlist1, playlist2, playlist3)));
    }

    @Test
    public void findAllByUserIdWhenItDoesNotExist() {
        final Long unknownUserId = 4235L;
        final List<Playlist> playlists = playlistRepository.findAllByUserId(unknownUserId);

        assertNotNull(playlists);
        assertEquals(0, playlists.size());
    }
}
