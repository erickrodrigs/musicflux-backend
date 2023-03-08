package com.erickrodrigues.musicflux.playlist;

import com.erickrodrigues.musicflux.user.User;
import com.erickrodrigues.musicflux.song.Song;
import com.erickrodrigues.musicflux.user.UserRepository;
import com.erickrodrigues.musicflux.song.SongRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlaylistServiceImplTest {

    @Mock
    private PlaylistRepository playlistRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SongRepository songRepository;

    @InjectMocks
    private PlaylistServiceImpl playlistService;

    @Test
    public void create() {
        Long userId = 1L;
        User user = User.builder().id(userId).build();
        String playlistName = "my fav songs";
        Playlist mockedPlaylist = Playlist.builder()
                .id(1L)
                .name(playlistName)
                .user(user)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(playlistRepository.save(any())).thenReturn(mockedPlaylist);

        Playlist actualPlaylist = playlistService.create(userId, playlistName);

        assertNotNull(actualPlaylist);
        assertNotNull(actualPlaylist.getId());
        assertEquals(playlistName, actualPlaylist.getName());
        assertEquals(userId, actualPlaylist.getUser().getId());
        verify(userRepository, times(1)).findById(anyLong());
        verify(playlistRepository, times(1)).save(any());
    }

    @Test
    public void createWithUserThatDoesNotExist() {
        Long userId = 1L;
        String playlistName = "my fav songs";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> playlistService.create(userId, playlistName));
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(0)).save(any());
        verify(playlistRepository, times(0)).save(any());
    }

    @Test
    public void findById() {
        final Long playlistId = 1L;
        final Playlist playlist = Playlist.builder().id(playlistId).name("these are my fav songs").build();

        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));

        assertEquals(playlist.getId(), playlistService.findById(playlistId).getId());
        verify(playlistRepository, times(1)).findById(anyLong());
    }

    @Test
    public void findByIdThatDoesNotExist() {
        final Long playlistId = 1L;
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> playlistService.findById(playlistId));
        verify(playlistRepository, times(1)).findById(anyLong());
    }

    @Test
    public void findAllByName() {
        String name = "my fav songs";
        List<Playlist> playlists = List.of(
                Playlist.builder().id(1L).name("these are my fav songs").build(),
                Playlist.builder().id(2L).name("MY FAV SONGS OF ALL TIME").build()
        );

        when(playlistRepository.findAllByNameContainingIgnoreCase(name)).thenReturn(playlists);

        assertEquals(2, playlistService.findAllByNameContainingIgnoreCase(name).size());
        verify(playlistRepository, times(1)).findAllByNameContainingIgnoreCase(anyString());
    }

    @Test
    public void findAllByUserId() {
        final Long userId = 1L;
        final List<Playlist> playlists = List.of(
                Playlist.builder().id(1L).name("heavy metal").build(),
                Playlist.builder().id(2L).name("cool funk").build()
        );

        when(playlistRepository.findAllByUserId(userId)).thenReturn(playlists);

        assertEquals(2, playlistService.findAllByUserId(userId).size());
        verify(playlistRepository, times(1)).findAllByUserId(anyLong());
    }

    @Test
    public void addSong() {
        final Long userId = 1L, playlistId = 1L, songId = 1L;

        User user = User.builder().id(userId).build();
        Playlist playlist = Playlist.builder().id(playlistId).build();
        Song song = Song.builder().id(songId).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));
        when(songRepository.findById(songId)).thenReturn(Optional.of(song));

        Playlist expectedPlaylist = Playlist.builder().id(playlistId).songs(List.of(song)).build();

        when(playlistRepository.save(any())).thenReturn(expectedPlaylist);

        Playlist actualPlaylist = playlistService.addSong(userId, playlistId, songId);

        assertEquals(userId, actualPlaylist.getId());
        assertEquals(expectedPlaylist.getSongs().size(), actualPlaylist.getSongs().size());
        assertTrue(actualPlaylist.getSongs().containsAll(expectedPlaylist.getSongs()));
        verify(userRepository, times(1)).findById(anyLong());
        verify(playlistRepository, times(1)).findById(anyLong());
        verify(songRepository, times(1)).findById(anyLong());
        verify(playlistRepository, times(1)).save(any());
    }

    @Test
    public void addSongWhenPlaylistDoesNotExist() {
        when(playlistRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> playlistService.addSong(1L, 1L, 1L));
        verify(playlistRepository, times(1)).findById(anyLong());
        verify(playlistRepository, times(0)).save(any());
    }

    @Test
    public void addSongWhenSongDoesNotExist() {
        when(playlistRepository.findById(anyLong())).thenReturn(Optional.of(Playlist.builder().build()));
        when(songRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> playlistService.addSong(1L, 1L, 1L));
        verify(songRepository, times(1)).findById(anyLong());
        verify(playlistRepository, times(0)).save(any());
    }

    @Test
    public void addSongWhenUserDoesNotExist() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(playlistRepository.findById(anyLong())).thenReturn(Optional.of(Playlist.builder().build()));
        when(songRepository.findById(anyLong())).thenReturn(Optional.of(Song.builder().build()));

        assertThrows(RuntimeException.class, () -> playlistService.addSong(1L, 1L, 1L));
        verify(userRepository, times(1)).findById(anyLong());
        verify(playlistRepository, times(0)).save(any());
    }

    @Test
    public void removeSong() {
        final Long userId = 1L, playlistId = 1L, songId = 1L;

        User user = User.builder().id(userId).build();
        Song song = Song.builder().id(songId).build();
        Playlist playlist = Playlist.builder().id(playlistId).songs(new ArrayList<>(List.of(song))).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));
        when(songRepository.findById(songId)).thenReturn(Optional.of(song));

        Playlist expectedPlaylist = Playlist.builder().id(playlistId).songs(List.of()).build();

        when(playlistRepository.save(any())).thenReturn(expectedPlaylist);

        Playlist actualPlaylist = playlistService.removeSong(userId, playlistId, songId);

        assertEquals(userId, actualPlaylist.getId());
        assertEquals(expectedPlaylist.getSongs().size(), actualPlaylist.getSongs().size());
        verify(userRepository, times(1)).findById(anyLong());
        verify(playlistRepository, times(1)).findById(anyLong());
        verify(songRepository, times(1)).findById(anyLong());
        verify(playlistRepository, times(1)).save(any());
    }

    @Test
    public void removeSongWhenPlaylistDoesNotExist() {
        when(playlistRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> playlistService.removeSong(1L, 1L, 1L));
        verify(playlistRepository, times(1)).findById(anyLong());
        verify(playlistRepository, times(0)).save(any());
    }

    @Test
    public void removeSongWhenSongDoesNotExist() {
        when(playlistRepository.findById(anyLong())).thenReturn(Optional.of(Playlist.builder().build()));
        when(songRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> playlistService.removeSong(1L, 1L, 1L));
        verify(songRepository, times(1)).findById(anyLong());
        verify(playlistRepository, times(0)).save(any());
    }

    @Test
    public void removeSongWhenUserDoesNotExist() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(playlistRepository.findById(anyLong())).thenReturn(Optional.of(Playlist.builder().build()));
        when(songRepository.findById(anyLong())).thenReturn(Optional.of(Song.builder().build()));

        assertThrows(RuntimeException.class, () -> playlistService.removeSong(1L, 1L, 1L));
        verify(userRepository, times(1)).findById(anyLong());
        verify(playlistRepository, times(0)).save(any());
    }

    @Test
    public void deleteById() {
        final Long userId = 1L, playlistId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(User.builder().id(userId).build()));

        playlistService.deleteById(userId, playlistId);

        verify(playlistRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void deleteByIdWhenUserDoesNotExist() {
        final Long userId = 1L, playlistId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> playlistService.deleteById(userId, playlistId));
        verify(playlistRepository, times(0)).deleteById(anyLong());
    }
}
