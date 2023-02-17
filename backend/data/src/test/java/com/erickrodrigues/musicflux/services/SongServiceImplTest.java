package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Album;
import com.erickrodrigues.musicflux.domain.Profile;
import com.erickrodrigues.musicflux.domain.Song;
import com.erickrodrigues.musicflux.repositories.AlbumRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SongServiceImplTest {

    @Mock
    private SongRepository songRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private AlbumRepository albumRepository;

    @InjectMocks
    private SongServiceImpl songService;

    @Test
    public void play() {
        Long songId = 1L, profileId = 1L;

        Profile profile = Profile.builder().id(profileId).build();
        Song song = Song.builder().id(songId).build();

        when(songRepository.findById(songId)).thenReturn(Optional.of(song));
        when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));

        songService.play(profileId, songId);

        assertEquals(1, song.getNumberOfPlays());
        assertEquals(1, profile.getRecentlyListenedSongs().size());

        verify(songRepository, times(1)).findById(anyLong());
        verify(profileRepository, times(1)).findById(anyLong());
        verify(songRepository, times(1)).save(any());
        verify(profileRepository, times(1)).save(any());
    }

    @Test
    public void playWithInvalidArguments() {
        when(songRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> songService.play(1L, 1L));

        when(songRepository.findById(anyLong())).thenReturn(Optional.of(Song.builder().build()));
        when(profileRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> songService.play(1L, 1L));
    }

    @Test
    public void findAllByTitle() {
        String title = "I wanna love you";
        Set<Song> songs = Set.of(
                Song.builder().id(1L).title("i wanna love you").build(),
                Song.builder().id(2L).title("all i know is i wanna love you").build()
        );

        when(songRepository.findAllByTitleContainingIgnoreCase(title)).thenReturn(songs);

        assertEquals(2, songService.findAllByTitle(title).size());
        verify(songRepository, times(1)).findAllByTitleContainingIgnoreCase(anyString());
    }

    @Test
    public void findAllByAlbumId() {
        final Long albumId = 1L;
        final Set<Song> songs = Set.of(
                Song.builder().id(1L).title("i wanna love you").build(),
                Song.builder().id(2L).title("all i know is i wanna love you").build()
        );
        final Album album = Album.builder().id(albumId).songs(songs).build();

        when(albumRepository.findById(albumId)).thenReturn(Optional.of(album));

        assertEquals(2, songService.findAllByAlbumId(albumId).size());
        verify(albumRepository, times(1)).findById(anyLong());
    }

    @Test
    public void findAllByAlbumIdThatDoesNotExist() {
        final Long albumId = 1L;
        when(albumRepository.findById(albumId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> songService.findAllByAlbumId(albumId));
        verify(albumRepository, times(1)).findById(anyLong());
    }

    @Test
    public void findAllByArtistId() {
        Set<Album> albums = Set.of(
                Album.builder()
                        .id(1L)
                        .songs(
                                Set.of(Song.builder().id(1L).numberOfPlays(5400L).build(),
                                        Song.builder().id(2L).numberOfPlays(400L).build(),
                                        Song.builder().id(3L).numberOfPlays(7600L).build())
                        )
                        .build(),
                Album.builder()
                        .id(2L)
                        .songs(
                                Set.of(Song.builder().id(4L).numberOfPlays(1000L).build(),
                                        Song.builder().id(5L).numberOfPlays(9000L).build(),
                                        Song.builder().id(6L).numberOfPlays(7200L).build())
                        )
                        .build()
        );

        when(albumRepository.findAllByArtistsIn(Set.of(1L))).thenReturn(albums);

        String topSongsIds = songService.findMostListenedSongsByArtistId(1L)
                .stream()
                .map(Song::getId)
                .toList()
                .toString();

        assertEquals("[5, 3, 6, 1, 4]", topSongsIds);
    }

    @Test
    public void findAllByArtistIdWhenArtistHasLessThanFiveSongs() {
        Set<Album> albums = Set.of(
                Album.builder()
                        .id(1L)
                        .songs(
                                Set.of(Song.builder().id(1L).numberOfPlays(5400L).build(),
                                        Song.builder().id(2L).numberOfPlays(400L).build(),
                                        Song.builder().id(3L).numberOfPlays(7600L).build())
                        )
                        .build()
        );

        when(albumRepository.findAllByArtistsIn(Set.of(1L))).thenReturn(albums);

        String topSongsIds = songService.findMostListenedSongsByArtistId(1L)
                .stream()
                .map(Song::getId)
                .toList()
                .toString();

        assertEquals("[3, 1, 2]", topSongsIds);
    }
}
