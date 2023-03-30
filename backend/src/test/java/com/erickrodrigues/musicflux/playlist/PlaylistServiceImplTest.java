package com.erickrodrigues.musicflux.playlist;

import com.erickrodrigues.musicflux.shared.InvalidActionException;
import com.erickrodrigues.musicflux.shared.ResourceNotFoundException;
import com.erickrodrigues.musicflux.track.TrackService;
import com.erickrodrigues.musicflux.track.Track;
import com.erickrodrigues.musicflux.user.User;
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
    private static final String WRONG_NUMBER_OF_TRACKS_IN_PLAYLIST = "Wrong number of tracks in playlist";
    private static final String ACTUAL_LIST_DOES_NOT_CONTAIN_ALL_SPECIFIED_TRACKS = "Actual list does not contain all specified tracks";

    @Mock
    private PlaylistRepository playlistRepository;

    @Mock
    private UserService userService;

    @Mock
    private TrackService trackService;

    @InjectMocks
    private PlaylistServiceImpl playlistService;

    @Test
    public void shouldCreateANewPlaylist() {
        // given
        final Long userId = 1L;
        final User user = User.builder().id(userId).build();
        final String playlistName = "my fav tracks";
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
        final String playlistName = "my fav tracks";
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
        final Playlist playlist = Playlist.builder().id(playlistId).name("these are my fav tracks").build();
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
        final String text = "my fav tracks";
        final List<Playlist> playlists = List.of(
                Playlist.builder().id(1L).name("these are my fav tracks").build(),
                Playlist.builder().id(2L).name("MY FAV TRACKS OF ALL TIME").build()
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
    public void shouldAddANewTrackToAPlaylist() {
        // given
        final Long userId = 1L, playlistId = 1L;
        final List<Long> tracksIds = List.of(1L);
        final User user = User.builder().id(userId).build();
        final Playlist playlist = Playlist.builder().id(playlistId).user(user).build();
        final Track track = Track.builder().id(tracksIds.get(0)).build();
        final Playlist expectedPlaylist = Playlist.builder().tracks(List.of(track)).build();
        when(userService.findById(userId)).thenReturn(user);
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));
        when(trackService.findById(track.getId())).thenReturn(track);
        when(playlistRepository.save(playlist)).thenReturn(expectedPlaylist);

        // when
        final Playlist actualPlaylist = playlistService.addTracks(userId, playlistId, tracksIds);

        // then
        assertEquals(expectedPlaylist.getTracks().size(), actualPlaylist.getTracks().size(), WRONG_NUMBER_OF_TRACKS_IN_PLAYLIST);
        assertTrue(actualPlaylist.getTracks().containsAll(expectedPlaylist.getTracks()), ACTUAL_LIST_DOES_NOT_CONTAIN_ALL_SPECIFIED_TRACKS);
        verify(userService, times(1)).findById(userId);
        verify(playlistRepository, times(1)).findById(playlistId);
        verify(trackService, times(1)).findById(track.getId());
        verify(playlistRepository, times(1)).save(playlist);
    }

    @Test
    public void shouldThrowAnExceptionWhenAddingATrackToAPlaylistThatDoesNotExist() {
        // given
        final Long userId = 1L, playlistId = 1L;
        final List<Long> tracksIds = List.of(1L);
        final User user = User.builder().id(userId).build();
        when(userService.findById(userId)).thenReturn(user);
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.empty());

        // then
        assertThrows(ResourceNotFoundException.class, () -> playlistService.addTracks(userId, playlistId, tracksIds));
        verify(userService, times(1)).findById(userId);
        verify(playlistRepository, times(1)).findById(playlistId);
        verify(playlistRepository, never()).save(any());
    }

    @Test
    public void shouldThrowAnExceptionWhenAddingATrackThatDoesNotExist() {
        // given
        final Long userId = 1L, playlistId = 1L;
        final List<Long> tracksIds = List.of(1L);
        final User user = User.builder().id(userId).build();
        when(userService.findById(userId)).thenReturn(user);
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(Playlist.builder().id(playlistId).user(user).build()));
        when(trackService.findById(tracksIds.get(0))).thenThrow(ResourceNotFoundException.class);

        // then
        assertThrows(ResourceNotFoundException.class, () -> playlistService.addTracks(userId, playlistId, tracksIds));
        verify(userService, times(1)).findById(userId);
        verify(trackService, times(1)).findById(tracksIds.get(0));
        verify(playlistRepository, times(1)).findById(playlistId);
        verify(playlistRepository, never()).save(any());
    }

    @Test
    public void shouldThrowAnExceptionWhenAddingATrackWhenUserDoesNotExist() {
        // given
        final Long userId = 1L, playlistId = 1L;
        final List<Long> tracksIds = List.of(1L);
        when(userService.findById(userId)).thenThrow(ResourceNotFoundException.class);

        // then
        assertThrows(ResourceNotFoundException.class, () -> playlistService.addTracks(userId, playlistId, tracksIds));
        verify(userService, times(1)).findById(userId);
        verify(playlistRepository, never()).save(any());
    }

    @Test
    public void shouldThrowAnExceptionWhenUserThatIsNotPlaylistOwnerAddsATrackToPlaylist() {
        // given
        final Long userId = 1L, playlistId = 1L;
        final List<Long> tracksIds = List.of(1L);
        final User user = User.builder().id(userId).build();
        final Playlist playlist = Playlist.builder().id(playlistId).user(User.builder().id(userId + 1L).build()).build();
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));
        when(userService.findById(userId)).thenReturn(user);

        // then
        assertThrows(InvalidActionException.class, () -> playlistService.addTracks(userId, playlistId, tracksIds));
        verify(playlistRepository, times(1)).findById(playlistId);
        verify(userService, times(1)).findById(userId);
        verify(playlistRepository, never()).save(any());
    }

    @Test
    public void shouldRemoveATrackFromAPlaylist() {
        // given
        final Long userId = 1L, playlistId = 1L;
        final List<Long> tracksIds = List.of(1L);
        final User user = User.builder().id(userId).build();
        final Track track = Track.builder().id(tracksIds.get(0)).build();
        final Playlist playlist = Playlist.builder().tracks(new ArrayList<>(List.of(track))).user(user).build();
        final Playlist expectedPlaylist = Playlist.builder().tracks(List.of()).build();
        when(userService.findById(userId)).thenReturn(user);
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));
        when(trackService.findById(track.getId())).thenReturn(track);
        when(playlistRepository.save(expectedPlaylist)).thenReturn(expectedPlaylist);

        // when
        Playlist actualPlaylist = playlistService.removeTracks(userId, playlistId, tracksIds);

        // then
        assertEquals(expectedPlaylist.getTracks().size(), actualPlaylist.getTracks().size(), WRONG_NUMBER_OF_TRACKS_IN_PLAYLIST);
        verify(userService, times(1)).findById(userId);
        verify(playlistRepository, times(1)).findById(playlistId);
        verify(trackService, times(1)).findById(track.getId());
        verify(playlistRepository, times(1)).save(expectedPlaylist);
    }

    @Test
    public void shouldThrowAnExceptionWhenRemovingATrackFromAPlaylistThatDoesNotExist() {
        // given
        final Long userId = 1L, playlistId = 1L;
        final List<Long> tracksIds = List.of(1L);
        final User user = User.builder().id(userId).build();
        when(userService.findById(userId)).thenReturn(user);
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.empty());

        // then
        assertThrows(ResourceNotFoundException.class, () -> playlistService.removeTracks(userId, playlistId, tracksIds));
        verify(userService, times(1)).findById(userId);
        verify(playlistRepository, times(1)).findById(playlistId);
        verify(playlistRepository, never()).save(any());
    }

    @Test
    public void shouldThrowAnExceptionWhenRemovingATrackThatDoesNotExist() {
        // given
        final Long userId = 1L, playlistId = 1L;
        final List<Long> tracksIds = List.of(1L);
        final User user = User.builder().id(userId).build();
        when(userService.findById(userId)).thenReturn(user);
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(Playlist.builder().id(playlistId).user(user).build()));
        when(trackService.findById(tracksIds.get(0))).thenThrow(ResourceNotFoundException.class);

        // then
        assertThrows(ResourceNotFoundException.class, () -> playlistService.removeTracks(userId, playlistId, tracksIds));
        verify(userService, times(1)).findById(userId);
        verify(playlistRepository, times(1)).findById(playlistId);
        verify(trackService, times(1)).findById(tracksIds.get(0));
        verify(playlistRepository, never()).save(any());
    }

    @Test
    public void shouldThrowAnExceptionWhenUserThatDoesNotExistRemovesATrack() {
        // given
        final Long userId = 1L, playlistId = 1L;
        final List<Long> tracksIds = List.of(1L);
        when(userService.findById(userId)).thenThrow(ResourceNotFoundException.class);

        // then
        assertThrows(ResourceNotFoundException.class, () -> playlistService.removeTracks(userId, playlistId, tracksIds));
        verify(userService, times(1)).findById(userId);
    }

    @Test
    public void shouldThrowAnExceptionWhenUserThatIsNotPlaylistOwnerRemovesATrackFromPlaylist() {
        // given
        final Long userId = 1L, playlistId = 1L;
        final List<Long> tracksIds = List.of(1L);
        final User user = User.builder().id(userId).build();
        final Playlist playlist = Playlist.builder().id(playlistId).user(User.builder().id(userId + 1L).build()).build();
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));
        when(userService.findById(userId)).thenReturn(user);

        // then
        assertThrows(InvalidActionException.class, () -> playlistService.removeTracks(userId, playlistId, tracksIds));
        verify(playlistRepository, times(1)).findById(playlistId);
        verify(userService, times(1)).findById(userId);
    }

    @Test
    public void shouldDeleteAPlaylistByItsId() {
        // given
        final Long userId = 1L, playlistId = 1L;
        final User user = User.builder().id(userId).build();
        final Playlist playlist = Playlist.builder().id(playlistId).user(user).build();
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));
        when(userService.findById(userId)).thenReturn(user);

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

    @Test
    public void shouldThrowAnExceptionWhenUserThatIsNotPlaylistOwnerDeletesPlaylist() {
        // given
        final Long userId = 1L, playlistId = 1L;
        final User user = User.builder().id(userId).build();
        final Playlist playlist = Playlist.builder().id(playlistId).user(User.builder().id(userId + 1L).build()).build();
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));
        when(userService.findById(userId)).thenReturn(user);

        // then
        assertThrows(InvalidActionException.class, () -> playlistService.deleteById(userId, playlistId));
        verify(playlistRepository, times(1)).findById(playlistId);
        verify(userService, times(1)).findById(userId);
        verify(playlistRepository, never()).deleteById(any());
    }
}
