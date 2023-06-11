package com.erickrodrigues.musicflux.track;

import com.erickrodrigues.musicflux.shared.ResourceNotFoundException;
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

    @Mock
    private TrackRepository trackRepository;

    @InjectMocks
    private TrackServiceImpl trackService;

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
    public void shouldPlayATrack() {
        final Long trackId = 1L;
        final Track track = Track.builder().id(trackId).build();
        when(trackRepository.findById(trackId)).thenReturn(Optional.of(track));

        trackService.play(trackId);

        assertEquals(1, track.getNumberOfPlays(), WRONG_NUMBER_OF_PLAYS);
        verify(trackRepository, times(1)).findById(trackId);
    }

    @Test
    public void shouldThrowAnExceptionWhenPlayingATrackWithInvalidId() {
        final Long invalidTrackId = 394L;
        when(trackRepository.findById(invalidTrackId)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> trackService.play(invalidTrackId));
        verify(trackRepository, times(1)).findById(invalidTrackId);
    }
}
