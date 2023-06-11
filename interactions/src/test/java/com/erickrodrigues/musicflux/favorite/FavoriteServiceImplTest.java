package com.erickrodrigues.musicflux.favorite;

import com.erickrodrigues.musicflux.shared.ResourceAlreadyExistsException;
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

import java.util.List;
import java.util.Map;
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
    private TrackService trackService;

    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    @Test
    public void shouldFavoriteATrack() {
        // given
        final Long userId = 1L, trackId = 1L;
        final User user = User.builder().id(userId).build();
        final Track track = Track.builder().id(trackId).build();
        final Favorite favorite = Favorite.builder().user(user).track(track).build();
        when(userService.findById(userId)).thenReturn(user);
        when(trackService.findById(trackId)).thenReturn(track);
        when(favoriteRepository.findByTrackId(trackId)).thenReturn(Optional.empty());
        when(favoriteRepository.save(favorite)).thenReturn(favorite);

        // when
        final Favorite actualFavorite = favoriteService.likeTrack(userId, trackId);

        // then
        assertNotNull(actualFavorite, FAVORITE_IS_NULL);
        assertEquals(favorite.getUser().getId(), user.getId(), WRONG_ID);
        assertEquals(favorite.getTrack().getId(), track.getId(), WRONG_ID);
        verify(userService, times(1)).findById(userId);
        verify(trackService, times(1)).findById(trackId);
        verify(favoriteRepository, times(1)).save(favorite);
    }

    @Test
    public void shouldThrowAnExceptionWhenLikingTrackThatDoesNotExist() {
        // given
        final Long userId = 1L, trackId = 1L;
        when(trackService.findById(trackId)).thenThrow(ResourceNotFoundException.class);

        // then
        assertThrows(ResourceNotFoundException.class, () -> favoriteService.likeTrack(userId, trackId));
        verify(trackService, times(1)).findById(trackId);
        verify(favoriteRepository, never()).save(any());
    }

    @Test
    public void shouldThrowAnExceptionWhenInvalidUserLikesTrack() {
        // given
        final Long userId = 1L, trackId = 1L;
        when(trackService.findById(trackId)).thenReturn(Track.builder().build());
        when(userService.findById(userId)).thenThrow(ResourceNotFoundException.class);

        // then
        assertThrows(ResourceNotFoundException.class, () -> favoriteService.likeTrack(userId, trackId));
        verify(userService, times(1)).findById(userId);
        verify(favoriteRepository, never()).save(any());
    }

    @Test
    public void shouldThrowAnExceptionWhenLinkingTrackThatWasAlreadyLiked() {
        // given
        final Long userId = 1L, trackId = 1L;
        when(trackService.findById(trackId)).thenReturn(Track.builder().build());
        when(userService.findById(userId)).thenReturn(User.builder().build());
        when(favoriteRepository.findByTrackId(trackId)).thenReturn(Optional.of(Favorite.builder().build()));

        // then
        assertThrows(ResourceAlreadyExistsException.class, () -> favoriteService.likeTrack(userId, trackId));
        verify(favoriteRepository, times(1)).findByTrackId(trackId);
        verify(favoriteRepository, never()).save(any());
    }

    @Test
    public void shouldDislikeTrack() {
        // given
        final Long userId = 1L, trackId = 1L, favoriteId = 1L;
        final User user = User.builder().id(userId).build();
        final Track track = Track.builder().id(trackId).build();
        final Favorite favorite = Favorite.builder().id(favoriteId).user(user).track(track).build();
        when(userService.findById(userId)).thenReturn(user);
        when(favoriteRepository.findById(favoriteId)).thenReturn(Optional.of(favorite));

        // when
        favoriteService.dislikeTrack(userId, favoriteId);

        // then
        verify(userService, times(1)).findById(userId);
        verify(favoriteRepository, times(1)).findById(favoriteId);
        verify(favoriteRepository, times(1)).delete(favorite);
    }

    @Test
    public void shouldThrowAnExceptionWhenDislikingTrackThatWasNotLiked() {
        // given
        final Long userId = 1L, favoriteId = 1L;
        when(favoriteRepository.findById(favoriteId)).thenReturn(Optional.empty());

        //then
        assertThrows(ResourceNotFoundException.class, () -> favoriteService.dislikeTrack(userId, favoriteId));
        verify(favoriteRepository, times(1)).findById(favoriteId);
        verify(favoriteRepository, never()).delete(any());
    }

    @Test
    public void shouldThrowAnExceptionWhenInvalidUserDislikesTrack() {
        // given
        final Long userId = 1L, favoriteId = 1L;
        when(favoriteRepository.findById(favoriteId)).thenReturn(Optional.of(Favorite.builder().build()));
        when(userService.findById(userId)).thenThrow(ResourceNotFoundException.class);

        // then
        assertThrows(ResourceNotFoundException.class, () -> favoriteService.dislikeTrack(userId, favoriteId));
        verify(userService, times(1)).findById(userId);
        verify(favoriteRepository, never()).delete(any());
    }

    @Test
    public void shouldFindAllLikedTracksByUserId() {
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

    @Test
    public void shouldReturnIfTracksSpecifiedAreLikedOrNot() {
        // given
        final Long userId = 1L;
        final User user = User.builder()
                .id(userId)
                .build();
        final Track track1 = Track.builder()
                .id(1L)
                .build();
        final Track track2 = Track.builder()
                .id(2L)
                .build();
        final Favorite favorite = Favorite.builder()
                .id(1L)
                .track(track1)
                .user(user)
                .build();
        when(userService.findById(user.getId())).thenReturn(user);
        when(trackService.findById(track1.getId())).thenReturn(track1);
        when(trackService.findById(track2.getId())).thenReturn(track2);
        when(favoriteRepository.findByTrackId(track1.getId())).thenReturn(Optional.of(favorite));
        when(favoriteRepository.findByTrackId(track2.getId())).thenReturn(Optional.empty());

        // when
        final Map<Long, Boolean> tracksToLiked = favoriteService.checkWhetherTracksAreLiked(
                user.getId(), List.of(track1.getId(), track2.getId())
        );

        // then
        assertTrue(tracksToLiked.containsKey(track1.getId()));
        assertTrue(tracksToLiked.containsKey(track2.getId()));
        assertTrue(tracksToLiked.get(track1.getId()));
        assertFalse(tracksToLiked.get(track2.getId()));
        verify(userService, times(1)).findById(user.getId());
        verify(trackService, times(1)).findById(track1.getId());
        verify(trackService, times(1)).findById(track2.getId());
        verify(favoriteRepository, times(1)).findByTrackId(track1.getId());
        verify(favoriteRepository, times(1)).findByTrackId(track2.getId());
    }
}
