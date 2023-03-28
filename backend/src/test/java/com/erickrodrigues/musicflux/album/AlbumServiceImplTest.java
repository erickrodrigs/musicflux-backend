package com.erickrodrigues.musicflux.album;

import com.erickrodrigues.musicflux.shared.ResourceNotFoundException;
import com.erickrodrigues.musicflux.track.Track;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AlbumServiceImplTest {

    private static final String WRONG_NUMBER_OF_ALBUMS = "Wrong number of albums";
    private static final String WRONG_NUMBER_OF_TRACKS = "Wrong number of tracks";

    @Mock
    private AlbumRepository albumRepository;

    @InjectMocks
    private AlbumServiceImpl albumService;

    @Test
    public void shouldFindAllAlbumsByTheirTitleContainingTextAndIgnoringCase() {
        // given
        final String text = "untitled";
        final List<Album> albums = List.of(
                Album.builder().id(1L).title("my untitled album").build(),
                Album.builder().id(2L).title("THESE ALBUM IS UNTITLED").build()
        );
        when(albumRepository.findAllByTitleContainingIgnoreCase(text)).thenReturn(albums);

        // when
        final List<Album> actualAlbums = albumService.findAllByTitleContainingIgnoreCase(text);

        // then
        assertEquals(albums.size(), actualAlbums.size(), WRONG_NUMBER_OF_ALBUMS);
        verify(albumRepository, times(1)).findAllByTitleContainingIgnoreCase(text);
    }

    @Test
    public void shouldFindAllTracksFromAnAlbum() {
        // given
        final Long albumId = 1L;
        final List<Track> tracks = List.of(
                Track.builder().id(1L).title("i wanna love you").build(),
                Track.builder().id(2L).title("all i know is i wanna love you").build()
        );
        final Album album = Album.builder().id(albumId).tracks(tracks).build();
        when(albumRepository.findById(albumId)).thenReturn(Optional.of(album));

        // when
        final List<Track> actualTracks = albumService.getAlbumTracks(albumId);

        // then
        assertEquals(tracks.size(), actualTracks.size(), WRONG_NUMBER_OF_TRACKS);
        verify(albumRepository, times(1)).findById(albumId);
    }

    @Test
    public void shouldThrowAnExceptionWhenFindingAllTracksFromAnAlbumThatDoesNotExist() {
        // given
        final Long albumId = 1L;
        when(albumRepository.findById(albumId)).thenReturn(Optional.empty());

        // then
        assertThrows(ResourceNotFoundException.class, () -> albumService.getAlbumTracks(albumId));
        verify(albumRepository, times(1)).findById(albumId);
    }
}
