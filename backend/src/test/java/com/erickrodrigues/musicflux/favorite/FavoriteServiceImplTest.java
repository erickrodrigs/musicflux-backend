package com.erickrodrigues.musicflux.favorite;

import com.erickrodrigues.musicflux.shared.ResourceAlreadyExistsException;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceImplTest {

    private static final String WRONG_NUMBER_OF_FAVORITES = "Wrong number of favorites";
    private static final String LIST_DOES_NOT_CONTAIN_SPECIFIED_FAVORITES = "Actual list does not contain specified favorites";
    private static final String WRONG_ID = "Wrong ID";
    private static final String FAVORITE_IS_NULL = "Favorite is null";

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private UserService userService;

    @Mock
    private SongService songService;

    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    @Test
    public void shouldFavoriteASong() {
        // given
        final Long userId = 1L, songId = 1L;
        final User user = User.builder().id(userId).build();
        final Song song = Song.builder().id(songId).build();
        final Favorite favorite = Favorite.builder().user(user).song(song).build();
        when(userService.findById(userId)).thenReturn(user);
        when(songService.findById(songId)).thenReturn(song);
        when(favoriteRepository.findBySongId(songId)).thenReturn(Optional.empty());
        when(favoriteRepository.save(favorite)).thenReturn(favorite);

        // when
        final Favorite actualFavorite = favoriteService.likeSong(userId, songId);

        // then
        assertNotNull(actualFavorite, FAVORITE_IS_NULL);
        assertEquals(favorite.getUser().getId(), user.getId(), WRONG_ID);
        assertEquals(favorite.getSong().getId(), song.getId(), WRONG_ID);
        verify(userService, times(1)).findById(userId);
        verify(songService, times(1)).findById(songId);
        verify(favoriteRepository, times(1)).save(favorite);
    }

    @Test
    public void shouldThrowAnExceptionWhenLikingSongThatDoesNotExist() {
        // given
        final Long userId = 1L, songId = 1L;
        when(songService.findById(songId)).thenThrow(ResourceNotFoundException.class);

        // then
        assertThrows(ResourceNotFoundException.class, () -> favoriteService.likeSong(userId, songId));
        verify(songService, times(1)).findById(songId);
        verify(favoriteRepository, never()).save(any());
    }

    @Test
    public void shouldThrowAnExceptionWhenInvalidUserLikesSong() {
        // given
        final Long userId = 1L, songId = 1L;
        when(songService.findById(songId)).thenReturn(Song.builder().build());
        when(userService.findById(userId)).thenThrow(ResourceNotFoundException.class);

        // then
        assertThrows(ResourceNotFoundException.class, () -> favoriteService.likeSong(userId, songId));
        verify(userService, times(1)).findById(userId);
        verify(favoriteRepository, never()).save(any());
    }

    @Test
    public void shouldThrowAnExceptionWhenLinkingSongThatWasAlreadyLiked() {
        // given
        final Long userId = 1L, songId = 1L;
        when(songService.findById(songId)).thenReturn(Song.builder().build());
        when(userService.findById(userId)).thenReturn(User.builder().build());
        when(favoriteRepository.findBySongId(songId)).thenReturn(Optional.of(Favorite.builder().build()));

        // then
        assertThrows(ResourceAlreadyExistsException.class, () -> favoriteService.likeSong(userId, songId));
        verify(favoriteRepository, times(1)).findBySongId(songId);
        verify(favoriteRepository, never()).save(any());
    }

    @Test
    public void shouldDislikeSong() {
        // given
        final Long userId = 1L, songId = 1L, favoriteId = 1L;
        final User user = User.builder().id(userId).build();
        final Song song = Song.builder().id(songId).build();
        final Favorite favorite = Favorite.builder().id(favoriteId).user(user).song(song).build();
        when(userService.findById(userId)).thenReturn(user);
        when(favoriteRepository.findById(favoriteId)).thenReturn(Optional.of(favorite));

        // when
        favoriteService.dislikeSong(userId, favoriteId);

        // then
        verify(userService, times(1)).findById(userId);
        verify(favoriteRepository, times(1)).findById(favoriteId);
        verify(favoriteRepository, times(1)).delete(favorite);
    }

    @Test
    public void shouldThrowAnExceptionWhenDislikingSongThatWasNotLiked() {
        // given
        final Long userId = 1L, favoriteId = 1L;
        when(favoriteRepository.findById(favoriteId)).thenReturn(Optional.empty());

        //then
        assertThrows(ResourceNotFoundException.class, () -> favoriteService.dislikeSong(userId, favoriteId));
        verify(favoriteRepository, times(1)).findById(favoriteId);
        verify(favoriteRepository, never()).delete(any());
    }

    @Test
    public void shouldThrowAnExceptionWhenInvalidUserDislikesSong() {
        // given
        final Long userId = 1L, favoriteId = 1L;
        when(favoriteRepository.findById(favoriteId)).thenReturn(Optional.of(Favorite.builder().build()));
        when(userService.findById(userId)).thenThrow(ResourceNotFoundException.class);

        // then
        assertThrows(ResourceNotFoundException.class, () -> favoriteService.dislikeSong(userId, favoriteId));
        verify(userService, times(1)).findById(userId);
        verify(favoriteRepository, never()).delete(any());
    }

    @Test
    public void shouldFindAllLikedSongsByUserId() {
        // given
        final Long userId = 1L;
        final List<Favorite> favorites = List.of(
                Favorite.builder().id(1L).build(),
                Favorite.builder().id(2L).build(),
                Favorite.builder().id(3L).build()
        );
        when(favoriteRepository.findAllByUserId(userId)).thenReturn(favorites);

        // when
        final List<Favorite> actualFavorites = favoriteService.findAllByUserId(userId);

        // then
        assertEquals(favorites.size(), actualFavorites.size(), WRONG_NUMBER_OF_FAVORITES);
        assertTrue(actualFavorites.containsAll(favorites), LIST_DOES_NOT_CONTAIN_SPECIFIED_FAVORITES);
        verify(favoriteRepository, times(1)).findAllByUserId(userId);
    }
}
