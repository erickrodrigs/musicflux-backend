package com.erickrodrigues.musicflux.repositories;

import com.erickrodrigues.musicflux.domain.Playlist;
import com.erickrodrigues.musicflux.domain.Profile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PlaylistRepositoryTest {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private ProfileRepository profileRepository;

    private Profile profile;

    private Playlist playlist1, playlist2, playlist3;

    @BeforeEach
    public void setUp() {
        profile = Profile.builder().id(1L).build();
        playlist1 = Playlist.builder().id(1L).profile(profile).name("my fav songs - heavy metal").build();
        playlist2 = Playlist.builder().id(2L).profile(profile).name("THESE ARE MY FAV SONGS OF ALL TIME").build();
        playlist3 = Playlist.builder().id(3L).profile(profile).name("funk").build();

        profileRepository.save(profile);
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

    @Test
    public void findAllByProfileId() {
        final Set<Playlist> playlists = playlistRepository.findAllByProfileId(profile.getId());

        assertEquals(3, playlists.size());
        assertTrue(playlists.containsAll(Set.of(playlist1, playlist2, playlist3)));
    }

    @Test
    public void findAllByProfileIdWhenItDoesNotExist() {
        final Long unknownProfileId = 4235L;
        final Set<Playlist> playlists = playlistRepository.findAllByProfileId(unknownProfileId);

        assertNotNull(playlists);
        assertEquals(0, playlists.size());
    }
}
