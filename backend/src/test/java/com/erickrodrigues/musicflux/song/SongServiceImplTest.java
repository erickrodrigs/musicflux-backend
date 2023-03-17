package com.erickrodrigues.musicflux.song;

import com.erickrodrigues.musicflux.recently_played.RecentlyPlayedService;
import com.erickrodrigues.musicflux.shared.ResourceNotFoundException;
import com.erickrodrigues.musicflux.user.User;
import com.erickrodrigues.musicflux.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SongServiceImplTest {

    private static final String WRONG_NUMBER_OF_PLAYS = "Wrong number of plays";
    private static final String WRONG_NUMBER_OF_SONGS = "Wrong number of songs";
    private static final String WRONG_ORDER_FOR_MOST_PLAYED_SONGS = "Wrong order for most played songs";

    @Mock
    private SongRepository songRepository;

    @Mock
    private UserService userService;

    @Mock
    private RecentlyPlayedService recentlyPlayedService;

    @InjectMocks
    private SongServiceImpl songService;

    @Test
    public void shouldPlayASong() {
        final Long songId = 1L, userId = 1L;
        final User user = User.builder().id(userId).build();
        final Song song = Song.builder().id(songId).build();
        when(songRepository.findById(songId)).thenReturn(Optional.of(song));
        when(userService.findById(userId)).thenReturn(user);

        songService.play(userId, songId);

        assertEquals(1, song.getNumberOfPlays(), WRONG_NUMBER_OF_PLAYS);
        verify(songRepository, times(1)).findById(songId);
        verify(userService, times(1)).findById(userId);
        verify(songRepository, times(1)).save(song);
        verify(recentlyPlayedService, times(1)).save(song, user);
    }

    @Test
    public void shouldThrowAnExceptionWhenPlayingASongWithInvalidId() {
        final Long invalidSongId = 394L, userId = 1L;
        when(songRepository.findById(invalidSongId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> songService.play(userId, invalidSongId));
        verify(songRepository, times(1)).findById(invalidSongId);
    }

    @Test
    public void shouldThrowAnExceptionWhenUserWithInvalidIdPlaysASong() {
        final Long songId = 1L, invalidUserId = 394L;
        final Song song = Song.builder().id(songId).build();
        when(songRepository.findById(songId)).thenReturn(Optional.of(song));
        when(userService.findById(invalidUserId)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> songService.play(invalidUserId, songId));
        verify(songRepository, times(1)).findById(songId);
        verify(userService, times(1)).findById(invalidUserId);
    }

    @Test
    public void shouldFindAllSongsByTitleContainingTextAndIgnoringCase() {
        final String text = "I wanna love you";
        final List<Song> songs = List.of(
                Song.builder().id(1L).title("i wanna love you").build(),
                Song.builder().id(2L).title("all i know is i wanna love you").build()
        );
        when(songRepository.findAllByTitleContainingIgnoreCase(text)).thenReturn(songs);

        assertEquals(songs.size(), songService.findAllByTitleContainingIgnoreCase(text).size(), WRONG_NUMBER_OF_SONGS);
        verify(songRepository, times(1)).findAllByTitleContainingIgnoreCase(text);
    }

    @Test
    public void shouldFindAllSongsByGenreName() {
        final String genre = "synth-pop";
        final List<Song> songs = List.of(
                Song.builder().id(1L).title("Black Celebration").build(),
                Song.builder().id(2L).title("Never Let Me Down Again").build()
        );
        when(songRepository.findAllByGenresNameIgnoreCase(genre)).thenReturn(songs);

        final List<Song> actualSongs = songService.findAllByGenreName(genre);

        assertEquals(songs.size(), actualSongs.size(), WRONG_NUMBER_OF_SONGS);
        verify(songRepository, times(1)).findAllByGenresNameIgnoreCase(genre);
    }

    @Test
    public void shouldFindAllSongsByAlbumId() {
        final Long albumId = 1L;
        final List<Song> songs = List.of(
                Song.builder().id(1L).title("i wanna love you").build(),
                Song.builder().id(2L).title("all i know is i wanna love you").build()
        );
        when(songRepository.findAllByAlbumId(albumId)).thenReturn(songs);

        final List<Song> actualSongs = songService.findAllByAlbumId(albumId);

        assertEquals(songs.size(), actualSongs.size(), WRONG_NUMBER_OF_SONGS);
        verify(songRepository, times(1)).findAllByAlbumId(albumId);
    }

    @Test
    public void shouldThrowAnExceptionWhenFindingAllSongsByAlbumIdThatDoesNotExist() {
        final Long albumId = 1L;
        when(songRepository.findAllByAlbumId(albumId)).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () -> songService.findAllByAlbumId(albumId));
        verify(songRepository, times(1)).findAllByAlbumId(albumId);
    }

    @Test
    public void shouldReturnFiveMostPlayedSongsByArtistId() {
        final Long artistId = 1L;
        final List<Song> songs = List.of(
                Song.builder().id(1L).numberOfPlays(5400L).build(),
                Song.builder().id(2L).numberOfPlays(400L).build(),
                Song.builder().id(3L).numberOfPlays(7600L).build(),
                Song.builder().id(4L).numberOfPlays(1000L).build(),
                Song.builder().id(5L).numberOfPlays(9000L).build(),
                Song.builder().id(6L).numberOfPlays(7200L).build()
        );
        when(songRepository.findAllByAlbumArtistsId(artistId)).thenReturn(songs);

        final String topSongsIds = songService.findMostPlayedSongsByArtistId(artistId)
                .stream()
                .map(Song::getId)
                .toList()
                .toString();

        assertEquals("[5, 3, 6, 1, 4]", topSongsIds, WRONG_ORDER_FOR_MOST_PLAYED_SONGS);
        verify(songRepository, times(1)).findAllByAlbumArtistsId(artistId);
    }

    @Test
    public void WhenArtistHasLessThanFiveSongs_ShouldReturnLessThanFiveMostPlayedSongs() {
        final Long artistId = 1L;
        final List<Song> songs = List.of(
                Song.builder().id(1L).numberOfPlays(5400L).build(),
                Song.builder().id(2L).numberOfPlays(400L).build(),
                Song.builder().id(3L).numberOfPlays(7600L).build()
        );
        when(songRepository.findAllByAlbumArtistsId(artistId)).thenReturn(songs);

        final String topSongsIds = songService.findMostPlayedSongsByArtistId(artistId)
                .stream()
                .map(Song::getId)
                .toList()
                .toString();

        assertEquals("[3, 1, 2]", topSongsIds, WRONG_ORDER_FOR_MOST_PLAYED_SONGS);
        verify(songRepository, times(1)).findAllByAlbumArtistsId(artistId);
    }
}
