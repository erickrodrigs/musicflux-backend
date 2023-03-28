package com.erickrodrigues.musicflux.playlist;

import com.erickrodrigues.musicflux.track.Track;
import com.erickrodrigues.musicflux.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlaylistTest {

    private Playlist playlist;

    @BeforeEach
    public void setUp() {
        playlist = Playlist.builder()
                .name("MyPlaylist")
                .user(User.builder().build())
                .build();
    }

    @Test
    public void addTrack() {
        playlist.addTrack(Track.builder().id(1L).build());

        assertEquals(1, playlist.getTracks().size());
    }

    @Test
    public void addTrackThatAlreadyExists() {
        playlist.addTrack(Track.builder().id(1L).build());

        assertThrows(RuntimeException.class, () -> playlist.addTrack(Track.builder().id(1L).build()));
    }

    @Test
    public void removeTrack() {
        Track track = Track.builder().id(1L).build();

        playlist.addTrack(track);
        playlist.removeTrack(track);

        assertTrue(playlist.getTracks().isEmpty());
    }

    @Test
    public void removeTrackThatIsNotIncludedInThePlaylist() {
        assertThrows(RuntimeException.class, () -> playlist.removeTrack(Track.builder().id(1L).build()));
    }
}
