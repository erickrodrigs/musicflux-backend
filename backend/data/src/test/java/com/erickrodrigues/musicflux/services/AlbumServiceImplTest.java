package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Album;
import com.erickrodrigues.musicflux.domain.Artist;
import com.erickrodrigues.musicflux.repositories.AlbumRepository;
import com.erickrodrigues.musicflux.repositories.ArtistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AlbumServiceImplTest {

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private ArtistRepository artistRepository;

    @InjectMocks
    private AlbumServiceImpl albumService;

    @Test
    public void findAllByTitle() {
        String title = "untitled";
        Set<Album> albums = Set.of(
                Album.builder().id(1L).title("my untitled album").build(),
                Album.builder().id(2L).title("THESE ALBUM IS UNTITLED").build()
        );

        when(albumRepository.findAllByTitleContainingIgnoreCase(title)).thenReturn(albums);

        assertEquals(2, albumService.findAllByTitle(title).size());
        verify(albumRepository, times(1)).findAllByTitleContainingIgnoreCase(anyString());
    }

    @Test
    public void findAllByArtistId() {
        Long artistId = 1L;
        Artist artist = Artist.builder().id(artistId).name("Metallica").build();
        Album album1 = Album.builder().id(1L).title("Master of Puppets").build();
        Album album2 = Album.builder().id(2L).title("Ride the Lightning").build();

        artist.setAlbums(Set.of(album1, album2));
        album1.setArtists(Set.of(artist));
        album2.setArtists(Set.of(artist));

        when(artistRepository.findById(artistId)).thenReturn(Optional.of(artist));

        assertEquals(2, albumService.findAllByArtistId(artistId).size());
    }

    @Test
    public void findAllByArtistIdWhenArtistDoesNotExist() {
        when(artistRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> albumService.findAllByArtistId(1L));
    }
}