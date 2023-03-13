package com.erickrodrigues.musicflux.favorite;

import com.erickrodrigues.musicflux.shared.ResourceAlreadyExistsException;
import com.erickrodrigues.musicflux.shared.ResourceNotFoundException;
import com.erickrodrigues.musicflux.user.User;
import com.erickrodrigues.musicflux.song.Song;
import com.erickrodrigues.musicflux.user.UserRepository;
import com.erickrodrigues.musicflux.song.SongRepository;
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

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SongRepository songRepository;

    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    @Test
    public void likeSong() {
        final Long userId = 1L, songId = 1L;
        final User user = User.builder().id(userId).build();
        final Song song = Song.builder().id(songId).build();
        final Favorite favorite = Favorite.builder().id(1L).user(user).song(song).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(songRepository.findById(songId)).thenReturn(Optional.of(song));
        when(favoriteRepository.findBySongId(songId)).thenReturn(Optional.empty());
        when(favoriteRepository.save(any())).thenReturn(favorite);

        final Favorite actualFavorite = favoriteService.likeSong(userId, songId);

        assertNotNull(actualFavorite);
        assertNotNull(actualFavorite.getId());
        assertEquals(favorite.getId(), actualFavorite.getId());
        assertEquals(favorite.getUser().getId(), user.getId());
        assertEquals(favorite.getSong().getId(), song.getId());
        verify(userRepository, times(1)).findById(anyLong());
        verify(songRepository, times(1)).findById(anyLong());
        verify(favoriteRepository, times(1)).save(any());
    }

    @Test
    public void likeSongWhenSongDoesNotExist() {
        when(songRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> favoriteService.likeSong(1L, 1L));
        verify(songRepository, times(1)).findById(anyLong());
        verify(favoriteRepository, times(0)).save(any());
    }

    @Test
    public void likeSongWhenUserDoesNotExist() {
        when(songRepository.findById(anyLong())).thenReturn(Optional.of(Song.builder().build()));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> favoriteService.likeSong(1L, 1L));
        verify(userRepository, times(1)).findById(anyLong());
        verify(favoriteRepository, times(0)).save(any());
    }

    @Test
    public void likeSongWhenItWasAlreadyLiked() {
        when(songRepository.findById(anyLong())).thenReturn(Optional.of(Song.builder().build()));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(User.builder().build()));
        when(favoriteRepository.findBySongId(anyLong())).thenReturn(Optional.of(Favorite.builder().build()));

        assertThrows(ResourceAlreadyExistsException.class, () -> favoriteService.likeSong(1L, 1L));
        verify(favoriteRepository, times(1)).findBySongId(anyLong());
        verify(favoriteRepository, times(0)).save(any());
    }

    @Test
    public void dislikeSong() {
        final Long userId = 1L, songId = 1L, favoriteId = 1L;
        final User user = User.builder().id(userId).build();
        final Song song = Song.builder().id(songId).build();
        final Favorite favorite = Favorite.builder().id(favoriteId).user(user).song(song).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(favoriteRepository.findById(favoriteId)).thenReturn(Optional.of(favorite));

        favoriteService.dislikeSong(userId, favoriteId);

        verify(userRepository, times(1)).findById(anyLong());
        verify(favoriteRepository, times(1)).findById(anyLong());
        verify(favoriteRepository, times(1)).delete(favorite);
    }

    @Test
    public void dislikeSongWhenSongDoesNotExist() {
        when(favoriteRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> favoriteService.dislikeSong(1L, 1L));
        verify(favoriteRepository, times(1)).findById(anyLong());
        verify(favoriteRepository, times(0)).delete(any());
    }

    @Test
    public void dislikeSongWhenUserDoesNotExist() {
        when(favoriteRepository.findById(anyLong())).thenReturn(Optional.of(Favorite.builder().build()));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> favoriteService.dislikeSong(1L, 1L));
        verify(userRepository, times(1)).findById(anyLong());
        verify(favoriteRepository, times(0)).delete(any());
    }

    @Test
    public void findAllByUserId() {
        final Long userId = 1L;
        final List<Favorite> favorites = List.of(
                Favorite.builder().id(1L).build(),
                Favorite.builder().id(2L).build(),
                Favorite.builder().id(3L).build()
        );

        when(favoriteRepository.findAllByUserId(userId)).thenReturn(favorites);

        final List<Favorite> actualFavorites = favoriteService.findAllByUserId(userId);

        assertEquals(favorites.size(), actualFavorites.size());
        assertTrue(actualFavorites.containsAll(favorites));
        verify(favoriteRepository, times(1)).findAllByUserId(anyLong());
    }
}
