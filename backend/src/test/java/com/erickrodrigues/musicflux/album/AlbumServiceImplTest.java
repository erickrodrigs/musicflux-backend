package com.erickrodrigues.musicflux.album;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AlbumServiceImplTest {

    private static final String WRONG_NUMBER_OF_ALBUMS = "Wrong number of albums";

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
    public void shouldFindAllAlbumsByArtistId() {
        // given
        final Long artistId = 1L;
        final List<Album> albums = List.of(
            Album.builder().id(1L).title("Master of Puppets").build(),
            Album.builder().id(2L).title("Ride the Lightning").build()
        );
        when(albumRepository.findAllByArtistsId(artistId)).thenReturn(albums);

        // when
        final List<Album> actualAlbums = albumService.findAllByArtistId(artistId);

        // then
        assertEquals(albums.size(), actualAlbums.size(), WRONG_NUMBER_OF_ALBUMS);
        verify(albumRepository, times(1)).findAllByArtistsId(artistId);
    }

    @Test
    public void shouldThrowAnExceptionWhenFindingAlbumsByArtistIdThatDoesNotExist() {
        // given
        final Long artistId = 1L;
        when(albumRepository.findAllByArtistsId(artistId)).thenReturn(List.of());

        // then
        assertThrows(RuntimeException.class, () -> albumService.findAllByArtistId(artistId));
        verify(albumRepository, times(1)).findAllByArtistsId(artistId);
    }
}
