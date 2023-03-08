package com.erickrodrigues.musicflux.playlist;

import com.erickrodrigues.musicflux.user.User;
import com.erickrodrigues.musicflux.song.Song;
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
    public void addSong() {
        playlist.addSong(Song.builder().id(1L).build());

        assertEquals(1, playlist.getSongs().size());
    }

    @Test
    public void addSongThatAlreadyExists() {
        playlist.addSong(Song.builder().id(1L).build());

        assertThrows(RuntimeException.class, () -> playlist.addSong(Song.builder().id(1L).build()));
    }

    @Test
    public void removeSong() {
        Song song = Song.builder().id(1L).build();

        playlist.addSong(song);
        playlist.removeSong(song);

        assertTrue(playlist.getSongs().isEmpty());
    }

    @Test
    public void removeSongThatIsNotIncludedInThePlaylist() {
        assertThrows(RuntimeException.class, () -> playlist.removeSong(Song.builder().id(1L).build()));
    }
}
