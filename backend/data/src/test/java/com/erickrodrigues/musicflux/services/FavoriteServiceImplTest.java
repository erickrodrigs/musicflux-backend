package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Favorite;
import com.erickrodrigues.musicflux.domain.Profile;
import com.erickrodrigues.musicflux.domain.Song;
import com.erickrodrigues.musicflux.repositories.FavoriteRepository;
import com.erickrodrigues.musicflux.repositories.ProfileRepository;
import com.erickrodrigues.musicflux.repositories.SongRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceImplTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private SongRepository songRepository;

    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    @Test
    public void likeSong() {
        final Long profileId = 1L, songId = 1L;
        final Profile profile = Profile.builder().id(profileId).build();
        final Song song = Song.builder().id(songId).build();
        final Favorite favorite = Favorite.builder().id(1L).profile(profile).song(song).build();

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));
        when(songRepository.findById(songId)).thenReturn(Optional.of(song));
        when(favoriteRepository.save(any())).thenReturn(favorite);

        final Favorite actualFavorite = favoriteService.likeSong(profileId, songId);

        assertNotNull(actualFavorite);
        assertNotNull(actualFavorite.getId());
        assertEquals(favorite.getId(), actualFavorite.getId());
        assertEquals(favorite.getProfile().getId(), profile.getId());
        assertEquals(favorite.getSong().getId(), song.getId());
        verify(profileRepository, times(1)).findById(anyLong());
        verify(songRepository, times(1)).findById(anyLong());
        verify(favoriteRepository, times(1)).save(any());
    }

    @Test
    public void likeSongWhenSongDoesNotExist() {
        when(songRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> favoriteService.likeSong(1L, 1L));
        verify(songRepository, times(1)).findById(anyLong());
        verify(favoriteRepository, times(0)).save(any());
    }

    @Test
    public void likeSongWhenProfileDoesNotExist() {
        when(songRepository.findById(anyLong())).thenReturn(Optional.of(Song.builder().build()));
        when(profileRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> favoriteService.likeSong(1L, 1L));
        verify(profileRepository, times(1)).findById(anyLong());
        verify(favoriteRepository, times(0)).save(any());
    }

    @Test
    public void dislikeSong() {
        final Long profileId = 1L, songId = 1L, favoriteId = 1L;
        final Profile profile = Profile.builder().id(profileId).build();
        final Song song = Song.builder().id(songId).build();
        final Favorite favorite = Favorite.builder().id(favoriteId).profile(profile).song(song).build();

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));
        when(favoriteRepository.findById(favoriteId)).thenReturn(Optional.of(favorite));

        favoriteService.dislikeSong(profileId, favoriteId);

        verify(profileRepository, times(1)).findById(anyLong());
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
    public void dislikeSongWhenProfileDoesNotExist() {
        when(favoriteRepository.findById(anyLong())).thenReturn(Optional.of(Favorite.builder().build()));
        when(profileRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> favoriteService.dislikeSong(1L, 1L));
        verify(profileRepository, times(1)).findById(anyLong());
        verify(favoriteRepository, times(0)).delete(any());
    }

    @Test
    public void findAllByProfileId() {
        final Long profileId = 1L;
        final Set<Favorite> favorites = Set.of(
                Favorite.builder().id(1L).build(),
                Favorite.builder().id(2L).build(),
                Favorite.builder().id(3L).build()
        );
        final Profile profile = Profile.builder().id(profileId).favorites(favorites).build();

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));

        final Set<Favorite> actualFavorites = favoriteService.findAllByProfileId(profileId);

        assertEquals(favorites.size(), actualFavorites.size());
        assertTrue(actualFavorites.containsAll(favorites));
        verify(profileRepository, times(1)).findById(anyLong());
    }

    @Test
    public void findAllByProfileIdThatDoesNotExist() {
        final Long profileId = 1L;

        when(profileRepository.findById(profileId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> favoriteService.findAllByProfileId(profileId));
        verify(profileRepository, times(1)).findById(anyLong());
    }
}
