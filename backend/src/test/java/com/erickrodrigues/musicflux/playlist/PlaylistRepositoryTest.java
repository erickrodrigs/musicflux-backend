package com.erickrodrigues.musicflux.playlist;

import com.erickrodrigues.musicflux.user.User;
import com.erickrodrigues.musicflux.user.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlaylistRepositoryTest {

    private static final String WRONG_NUMBER_OF_PLAYLISTS = "Wrong number of playlists";
    private static final String LIST_DOES_NOT_CONTAIN_SPECIFIED_PLAYLISTS = "Actual list does not contain specified playlists";
    private static final String PLAYLISTS_LIST_IS_NOT_EMPTY = "Playlists list is not empty";

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private UserRepository userRepository;

    private static User user = User
            .builder()
            .build();
    private static Playlist playlist1 = Playlist
            .builder()
            .name("my fav tracks - heavy metal")
            .build();
    private static Playlist playlist2 = Playlist
            .builder()
            .name("THESE ARE MY FAV TRACKS OF ALL TIME")
            .build();
    private static Playlist playlist3 = Playlist
            .builder()
            .name("funk")
            .build();

    @BeforeAll
    public void setUp() {
        user = userRepository.save(user);

        playlist1.setUser(user);
        playlist2.setUser(user);
        playlist3.setUser(user);

        playlist1 = playlistRepository.save(playlist1);
        playlist2 = playlistRepository.save(playlist2);
        playlist3 = playlistRepository.save(playlist3);
    }

    @Test
    public void shouldFindAllPlaylistsByNameContainingIgnoreCase() {
        final String text = "my fav tracks";
        final List<Playlist> playlists = playlistRepository.findAllByNameContainingIgnoreCase(text);

        assertEquals(2, playlists.size(), WRONG_NUMBER_OF_PLAYLISTS);
        assertTrue(playlists.containsAll(List.of(playlist1, playlist2)), LIST_DOES_NOT_CONTAIN_SPECIFIED_PLAYLISTS);
    }

    @Test
    public void shouldFindAllPlaylistsByUserId() {
        final List<Playlist> playlists = playlistRepository.findAllByUserId(user.getId());

        assertEquals(3, playlists.size(), WRONG_NUMBER_OF_PLAYLISTS);
        assertTrue(playlists.containsAll(List.of(playlist1, playlist2, playlist3)), LIST_DOES_NOT_CONTAIN_SPECIFIED_PLAYLISTS);
    }

    @Test
    public void shouldNotFindAnyPlaylistByUserIdWhenItDoesNotExist() {
        final Long unknownUserId = 4235L;
        final List<Playlist> playlists = playlistRepository.findAllByUserId(unknownUserId);

        assertTrue(playlists.isEmpty(), PLAYLISTS_LIST_IS_NOT_EMPTY);
    }
}
