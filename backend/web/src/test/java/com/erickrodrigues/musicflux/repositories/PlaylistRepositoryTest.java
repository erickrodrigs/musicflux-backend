package com.erickrodrigues.musicflux.repositories;

import com.erickrodrigues.musicflux.domain.Playlist;
import com.erickrodrigues.musicflux.domain.Profile;
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

    @Autowired
    private ProfileRepository profileRepository;

    private Profile profile1, profile2;
    private Playlist playlist1, playlist2, playlist3;

    @BeforeEach
    public void setUp() {
        playlist1 = Playlist.builder().id(1L).name("my fav songs - heavy metal").build();
        playlist2 = Playlist.builder().id(2L).name("THESE ARE MY FAV SONGS OF ALL TIME").build();
        playlist3 = Playlist.builder().id(3L).name("funk").build();
        profile1 = Profile.builder().id(1L).build();
        profile2 = Profile.builder().id(2L).build();

        profileRepository.save(profile1);
        profileRepository.save(profile2);
        playlistRepository.save(playlist1);
        playlistRepository.save(playlist2);
        playlistRepository.save(playlist3);

        profile1.addPlaylist(playlist1);
        profile1.addPlaylist(playlist3);
        profile2.addPlaylist(playlist2);
        playlist1.setProfile(profile1);
        playlist2.setProfile(profile2);
        playlist3.setProfile(profile1);

        profileRepository.save(profile1);
        profileRepository.save(profile2);
        playlistRepository.save(playlist1);
        playlistRepository.save(playlist2);
        playlistRepository.save(playlist3);
    }

    @Test
    public void findAllByNameContainingIgnoreCase() {
        Set<Playlist> playlists = playlistRepository.findAllByNameContainingIgnoreCase("my fav songs");

        assertEquals(2, playlists.size());
        assertTrue(playlists.containsAll(Set.of(playlist1, playlist2)));
    }

    @Test
    public void findAllByProfileId() {
        Set<Playlist> playlists;

        playlists = playlistRepository.findAllByProfileId(profile1.getId());

        assertEquals(2, playlists.size());
        assertTrue(playlists.containsAll(Set.of(playlist1, playlist3)));

        playlists = playlistRepository.findAllByProfileId(profile2.getId());

        assertEquals(1, playlists.size());
        assertTrue(playlists.contains(playlist2));
    }
}
