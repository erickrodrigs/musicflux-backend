package com.erickrodrigues.musicflux.track;

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
public class TrackServiceImplTest {

    private static final String TRACK_IS_NULL = "Track is null";
    private static final String TRACK_HAS_DIFFERENT_ID = "Track has different ID";
    private static final String WRONG_NUMBER_OF_PLAYS = "Wrong number of plays";
    private static final String WRONG_NUMBER_OF_TRACKS = "Wrong number of tracks";
    private static final String WRONG_ORDER_FOR_MOST_PLAYED_TRACKS = "Wrong order for most played tracks";

    @Mock
    private TrackRepository trackRepository;

    @Mock
    private UserService userService;

    @Mock
    private RecentlyPlayedService recentlyPlayedService;

    @InjectMocks
    private TrackServiceImpl trackService;

    @Test
    public void shouldPlayATrack() {
        final Long trackId = 1L, userId = 1L;
        final User user = User.builder().id(userId).build();
        final Track track = Track.builder().id(trackId).build();
        when(trackRepository.findById(trackId)).thenReturn(Optional.of(track));
        when(trackRepository.save(track)).thenReturn(track);
        when(userService.findById(userId)).thenReturn(user);

        trackService.play(userId, trackId);

        assertEquals(1, track.getNumberOfPlays(), WRONG_NUMBER_OF_PLAYS);
        verify(trackRepository, times(1)).findById(trackId);
        verify(userService, times(1)).findById(userId);
        verify(trackRepository, times(1)).save(track);
        verify(recentlyPlayedService, times(1)).save(track, user);
    }

    @Test
    public void shouldThrowAnExceptionWhenPlayingATrackWithInvalidId() {
        final Long invalidTrackId = 394L, userId = 1L;
        when(trackRepository.findById(invalidTrackId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> trackService.play(userId, invalidTrackId));
        verify(trackRepository, times(1)).findById(invalidTrackId);
    }

    @Test
    public void shouldThrowAnExceptionWhenUserWithInvalidIdPlaysATrack() {
        final Long trackId = 1L, invalidUserId = 394L;
        when(userService.findById(invalidUserId)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> trackService.play(invalidUserId, trackId));
        verify(userService, times(1)).findById(invalidUserId);
    }

    @Test
    public void shouldFindATrackByItsId() {
        final Long trackId = 1L;
        when(trackRepository.findById(trackId)).thenReturn(Optional.of(Track.builder().id(trackId).build()));

        final Track actualTrack = trackService.findById(trackId);

        assertNotNull(actualTrack, TRACK_IS_NULL);
        assertEquals(trackId, actualTrack.getId(), TRACK_HAS_DIFFERENT_ID);
        verify(trackRepository, times(1)).findById(trackId);
    }

    @Test
    public void shouldThrowAnExceptionWhenFindingTrackWithIdThatDoesNotExist() {
        final Long trackId = 1L;
        when(trackRepository.findById(trackId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> trackService.findById(trackId));
        verify(trackRepository, times(1)).findById(trackId);
    }

    @Test
    public void shouldFindAllTracksByTitleContainingTextAndIgnoringCase() {
        final String text = "I wanna love you";
        final List<Track> tracks = List.of(
                Track.builder().id(1L).title("i wanna love you").build(),
                Track.builder().id(2L).title("all i know is i wanna love you").build()
        );
        when(trackRepository.findAllByTitleContainingIgnoreCase(text)).thenReturn(tracks);

        assertEquals(tracks.size(), trackService.findAllByTitleContainingIgnoreCase(text).size(), WRONG_NUMBER_OF_TRACKS);
        verify(trackRepository, times(1)).findAllByTitleContainingIgnoreCase(text);
    }

    @Test
    public void shouldFindAllTracksByGenreName() {
        final String genre = "synth-pop";
        final List<Track> tracks = List.of(
                Track.builder().id(1L).title("Black Celebration").build(),
                Track.builder().id(2L).title("Never Let Me Down Again").build()
        );
        when(trackRepository.findAllByGenresNameIgnoreCase(genre)).thenReturn(tracks);

        final List<Track> actualTracks = trackService.findAllByGenreName(genre);

        assertEquals(tracks.size(), actualTracks.size(), WRONG_NUMBER_OF_TRACKS);
        verify(trackRepository, times(1)).findAllByGenresNameIgnoreCase(genre);
    }

    @Test
    public void shouldFindAllTracksByAlbumId() {
        final Long albumId = 1L;
        final List<Track> tracks = List.of(
                Track.builder().id(1L).title("i wanna love you").build(),
                Track.builder().id(2L).title("all i know is i wanna love you").build()
        );
        when(trackRepository.findAllByAlbumId(albumId)).thenReturn(tracks);

        final List<Track> actualTracks = trackService.findAllByAlbumId(albumId);

        assertEquals(tracks.size(), actualTracks.size(), WRONG_NUMBER_OF_TRACKS);
        verify(trackRepository, times(1)).findAllByAlbumId(albumId);
    }

    @Test
    public void shouldThrowAnExceptionWhenFindingAllTracksByAlbumIdThatDoesNotExist() {
        final Long albumId = 1L;
        when(trackRepository.findAllByAlbumId(albumId)).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () -> trackService.findAllByAlbumId(albumId));
        verify(trackRepository, times(1)).findAllByAlbumId(albumId);
    }

    @Test
    public void shouldReturnFiveMostPlayedTracksByArtistId() {
        final Long artistId = 1L;
        final List<Track> tracks = List.of(
                Track.builder().id(1L).numberOfPlays(5400L).build(),
                Track.builder().id(2L).numberOfPlays(400L).build(),
                Track.builder().id(3L).numberOfPlays(7600L).build(),
                Track.builder().id(4L).numberOfPlays(1000L).build(),
                Track.builder().id(5L).numberOfPlays(9000L).build(),
                Track.builder().id(6L).numberOfPlays(7200L).build()
        );
        when(trackRepository.findAllByAlbumArtistsId(artistId)).thenReturn(tracks);

        final String topTracksIds = trackService.findMostPlayedTracksByArtistId(artistId)
                .stream()
                .map(Track::getId)
                .toList()
                .toString();

        assertEquals("[5, 3, 6, 1, 4]", topTracksIds, WRONG_ORDER_FOR_MOST_PLAYED_TRACKS);
        verify(trackRepository, times(1)).findAllByAlbumArtistsId(artistId);
    }

    @Test
    public void WhenArtistHasLessThanFiveTracks_ShouldReturnLessThanFiveMostPlayedTracks() {
        final Long artistId = 1L;
        final List<Track> tracks = List.of(
                Track.builder().id(1L).numberOfPlays(5400L).build(),
                Track.builder().id(2L).numberOfPlays(400L).build(),
                Track.builder().id(3L).numberOfPlays(7600L).build()
        );
        when(trackRepository.findAllByAlbumArtistsId(artistId)).thenReturn(tracks);

        final String topTracksIds = trackService.findMostPlayedTracksByArtistId(artistId)
                .stream()
                .map(Track::getId)
                .toList()
                .toString();

        assertEquals("[3, 1, 2]", topTracksIds, WRONG_ORDER_FOR_MOST_PLAYED_TRACKS);
        verify(trackRepository, times(1)).findAllByAlbumArtistsId(artistId);
    }
}
