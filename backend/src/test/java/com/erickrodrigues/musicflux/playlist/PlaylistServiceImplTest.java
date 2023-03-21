package com.erickrodrigues.musicflux.playlist;

import com.erickrodrigues.musicflux.shared.ResourceNotFoundException;
import com.erickrodrigues.musicflux.song.SongService;
import com.erickrodrigues.musicflux.user.User;
import com.erickrodrigues.musicflux.song.Song;
import com.erickrodrigues.musicflux.user.UserService;
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

    private static final String PLAYLIST_IS_NULL = "Playlist is null";
    private static final String WRONG_PLAYLIST_NAME = "Wrong playlist name";
    private static final String WRONG_USER_ID = "Wrong user ID";
    private static final String WRONG_PLAYLIST_ID = "Wrong playlist ID";
    private static final String WRONG_NUMBER_OF_PLAYLISTS = "Wrong number of playlists";
    private static final String ACTUAL_LIST_DOES_NOT_CONTAIN_SPECIFIED_PLAYLISTS = "Actual list does not contain specified playlists";
    private static final String WRONG_NUMBER_OF_SONGS_IN_PLAYLIST = "Wrong number of songs in playlist";
    private static final String ACTUAL_LIST_DOES_NOT_CONTAIN_ALL_SPECIFIED_SONGS = "Actual list does not contain all specified songs";

    @Mock
    private PlaylistRepository playlistRepository;

    @Mock
    private UserService userService;

    @Mock
    private SongService songService;

    @InjectMocks
    private PlaylistServiceImpl playlistService;

    @Test
    public void shouldCreateANewPlaylist() {
        // given
        final Long userId = 1L;
        final User user = User.builder().id(userId).build();
        final String playlistName = "my fav songs";
        final Playlist playlist = Playlist.builder()
                .name(playlistName)
                .user(user)
                .build();
        when(userService.findById(userId)).thenReturn(user);
        when(playlistRepository.save(playlist)).thenReturn(playlist);

        // when
        Playlist actualPlaylist = playlistService.create(userId, playlistName);

        // then
        assertNotNull(actualPlaylist, PLAYLIST_IS_NULL);
        assertEquals(playlistName, actualPlaylist.getName(), WRONG_PLAYLIST_NAME);
        assertEquals(userId, actualPlaylist.getUser().getId(), WRONG_USER_ID);
        verify(userService, times(1)).findById(userId);
        verify(playlistRepository, times(1)).save(playlist);
    }

    @Test
    public void shouldThrowAnExceptionWhenUserThatDoesNotExistCreatesAPlaylist() {
        // given
        final Long userId = 1L;
        final String playlistName = "my fav songs";
        when(userService.findById(userId)).thenThrow(ResourceNotFoundException.class);

        // then
        assertThrows(ResourceNotFoundException.class, () -> playlistService.create(userId, playlistName));
        verify(userService, times(1)).findById(userId);
        verify(playlistRepository, never()).save(any());
    }

    @Test
    public void shouldFindPlaylistByItsId() {
        // given
        final Long playlistId = 1L;
        final Playlist playlist = Playlist.builder().id(playlistId).name("these are my fav songs").build();
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));

        // when
        final Playlist actualPlaylist = playlistService.findById(playlistId);

        // then
        assertEquals(playlist.getId(), actualPlaylist.getId(), WRONG_PLAYLIST_ID);
        verify(playlistRepository, times(1)).findById(playlistId);
    }

    @Test
    public void shouldThrowAnExceptionWhenFindingPlaylistByIdThatDoesNotExist() {
        // given
        final Long playlistId = 1L;
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.empty());

        // then
        assertThrows(ResourceNotFoundException.class, () -> playlistService.findById(playlistId));
        verify(playlistRepository, times(1)).findById(playlistId);
    }

    @Test
    public void shouldFindAllPlaylistsByNameContainingTextAndIgnoringCase() {
        // given
        final String text = "my fav songs";
        final List<Playlist> playlists = List.of(
                Playlist.builder().id(1L).name("these are my fav songs").build(),
                Playlist.builder().id(2L).name("MY FAV SONGS OF ALL TIME").build()
        );
        when(playlistRepository.findAllByNameContainingIgnoreCase(text)).thenReturn(playlists);

        // when
        final List<Playlist> actualList = playlistService.findAllByNameContainingIgnoreCase(text);

        // then
        assertEquals(playlists.size(), actualList.size(), WRONG_NUMBER_OF_PLAYLISTS);
        assertTrue(actualList.containsAll(playlists), ACTUAL_LIST_DOES_NOT_CONTAIN_SPECIFIED_PLAYLISTS);
        verify(playlistRepository, times(1)).findAllByNameContainingIgnoreCase(text);
    }

    @Test
    public void shouldFindAllPlaylistsByUserId() {
        // given
        final Long userId = 1L;
        final List<Playlist> playlists = List.of(
                Playlist.builder().id(1L).name("heavy metal").build(),
                Playlist.builder().id(2L).name("cool funk").build()
        );
        when(playlistRepository.findAllByUserId(userId)).thenReturn(playlists);

        // when
        final List<Playlist> actualList = playlistService.findAllByUserId(userId);

        // then
        assertEquals(playlists.size(), actualList.size(), WRONG_NUMBER_OF_PLAYLISTS);
        assertTrue(actualList.containsAll(playlists), ACTUAL_LIST_DOES_NOT_CONTAIN_SPECIFIED_PLAYLISTS);
        verify(playlistRepository, times(1)).findAllByUserId(userId);
    }

    @Test
    public void shouldAddANewSongToAPlaylist() {
        // given
        final Long userId = 1L, playlistId = 1L, songId = 1L;
        final User user = User.builder().id(userId).build();
        final Playlist playlist = Playlist.builder().id(playlistId).build();
        final Song song = Song.builder().id(songId).build();
        final Playlist expectedPlaylist = Playlist.builder().songs(List.of(song)).build();
        when(userService.findById(userId)).thenReturn(user);
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));
        when(songService.findById(songId)).thenReturn(song);
        when(playlistRepository.save(playlist)).thenReturn(expectedPlaylist);

        // when
        final Playlist actualPlaylist = playlistService.addSong(userId, playlistId, songId);

        // then
        assertEquals(expectedPlaylist.getSongs().size(), actualPlaylist.getSongs().size(), WRONG_NUMBER_OF_SONGS_IN_PLAYLIST);
        assertTrue(actualPlaylist.getSongs().containsAll(expectedPlaylist.getSongs()), ACTUAL_LIST_DOES_NOT_CONTAIN_ALL_SPECIFIED_SONGS);
        verify(userService, times(1)).findById(userId);
        verify(playlistRepository, times(1)).findById(playlistId);
        verify(songService, times(1)).findById(songId);
        verify(playlistRepository, times(1)).save(playlist);
    }

    @Test
    public void shouldThrowAnExceptionWhenAddingASongToAPlaylistThatDoesNotExist() {
        // given
        final Long userId = 1L, playlistId = 1L, songId = 1L;
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.empty());

        // then
        assertThrows(ResourceNotFoundException.class, () -> playlistService.addSong(userId, playlistId, songId));
        verify(playlistRepository, times(1)).findById(playlistId);
        verify(playlistRepository, never()).save(any());
    }

    @Test
    public void shouldThrowAnExceptionWhenAddingASongThatDoesNotExist() {
        // given
        final Long userId = 1L, playlistId = 1L, songId = 1L;
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(Playlist.builder().build()));
        when(songService.findById(songId)).thenThrow(ResourceNotFoundException.class);

        // then
        assertThrows(ResourceNotFoundException.class, () -> playlistService.addSong(userId, playlistId, songId));
        verify(songService, times(1)).findById(songId);
        verify(playlistRepository, times(1)).findById(playlistId);
        verify(playlistRepository, never()).save(any());
    }

    @Test
    public void shouldThrowAnExceptionWhenAddingASongWhenUserDoesNotExist() {
        // given
        final Long userId = 1L, playlistId = 1L, songId = 1L;
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(Playlist.builder().build()));
        when(songService.findById(songId)).thenReturn(Song.builder().build());
        when(userService.findById(userId)).thenThrow(ResourceNotFoundException.class);

        // then
        assertThrows(ResourceNotFoundException.class, () -> playlistService.addSong(userId, playlistId, songId));
        verify(songService, times(1)).findById(songId);
        verify(playlistRepository, times(1)).findById(playlistId);
        verify(userService, times(1)).findById(userId);
        verify(playlistRepository, never()).save(any());
    }

    @Test
    public void shouldRemoveASongFromAPlaylist() {
        // given
        final Long userId = 1L, playlistId = 1L, songId = 1L;
        final User user = User.builder().id(userId).build();
        final Song song = Song.builder().id(songId).build();
        final Playlist playlist = Playlist.builder().songs(new ArrayList<>(List.of(song))).build();
        final Playlist expectedPlaylist = Playlist.builder().songs(List.of()).build();
        when(userService.findById(userId)).thenReturn(user);
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));
        when(songService.findById(songId)).thenReturn(song);
        when(playlistRepository.save(expectedPlaylist)).thenReturn(expectedPlaylist);

        // when
        Playlist actualPlaylist = playlistService.removeSong(userId, playlistId, songId);

        // then
        assertEquals(expectedPlaylist.getSongs().size(), actualPlaylist.getSongs().size(), WRONG_NUMBER_OF_SONGS_IN_PLAYLIST);
        verify(userService, times(1)).findById(userId);
        verify(playlistRepository, times(1)).findById(playlistId);
        verify(songService, times(1)).findById(songId);
        verify(playlistRepository, times(1)).save(expectedPlaylist);
    }

    @Test
    public void shouldThrowAnExceptionWhenRemovingASongFromAPlaylistThatDoesNotExist() {
        // given
        final Long userId = 1L, playlistId = 1L, songId = 1L;
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.empty());

        // then
        assertThrows(ResourceNotFoundException.class, () -> playlistService.removeSong(userId, playlistId, songId));
        verify(playlistRepository, times(1)).findById(playlistId);
        verify(playlistRepository, never()).save(any());
    }

    @Test
    public void shouldThrowAnExceptionWhenRemovingASongThatDoesNotExist() {
        // given
        final Long userId = 1L, playlistId = 1L, songId = 1L;
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(Playlist.builder().build()));
        when(songService.findById(songId)).thenThrow(ResourceNotFoundException.class);

        // then
        assertThrows(ResourceNotFoundException.class, () -> playlistService.removeSong(userId, playlistId, songId));
        verify(playlistRepository, times(1)).findById(playlistId);
        verify(songService, times(1)).findById(songId);
        verify(playlistRepository, never()).save(any());
    }

    @Test
    public void shouldThrowAnExceptionWhenUserThatDoesNotExistRemovesASong() {
        // given
        final Long userId = 1L, playlistId = 1L, songId = 1L;
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(Playlist.builder().build()));
        when(songService.findById(songId)).thenReturn(Song.builder().build());
        when(userService.findById(userId)).thenThrow(ResourceNotFoundException.class);

        // then
        assertThrows(ResourceNotFoundException.class, () -> playlistService.removeSong(userId, playlistId, songId));
        verify(playlistRepository, times(1)).findById(playlistId);
        verify(songService, times(1)).findById(songId);
        verify(userService, times(1)).findById(userId);
        verify(playlistRepository, never()).save(any());
    }

    @Test
    public void shouldDeleteAPlaylistByItsId() {
        // given
        final Long userId = 1L, playlistId = 1L;
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(Playlist.builder().id(playlistId).build()));
        when(userService.findById(userId)).thenReturn(User.builder().id(userId).build());

        // when
        playlistService.deleteById(userId, playlistId);

        // then
        verify(userService, times(1)).findById(userId);
        verify(playlistRepository, times(1)).findById(playlistId);
        verify(playlistRepository, times(1)).deleteById(playlistId);
    }

    @Test
    public void shouldThrowAnExceptionWhenUserThatDoesNotExistDeletesAPlaylist() {
        // given
        final Long userId = 1L, playlistId = 1L;
        when(userService.findById(userId)).thenThrow(ResourceNotFoundException.class);

        // then
        assertThrows(ResourceNotFoundException.class, () -> playlistService.deleteById(userId, playlistId));
        verify(playlistRepository, never()).deleteById(playlistId);
    }
}
