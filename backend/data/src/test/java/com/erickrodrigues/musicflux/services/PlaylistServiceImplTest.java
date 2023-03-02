package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Playlist;
import com.erickrodrigues.musicflux.domain.Profile;
import com.erickrodrigues.musicflux.domain.Song;
import com.erickrodrigues.musicflux.repositories.PlaylistRepository;
import com.erickrodrigues.musicflux.repositories.ProfileRepository;
import com.erickrodrigues.musicflux.repositories.SongRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlaylistServiceImplTest {

    @Mock
    private PlaylistRepository playlistRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private SongRepository songRepository;

    @InjectMocks
    private PlaylistServiceImpl playlistService;

    @Test
    public void create() {
        Long profileId = 1L;
        Profile profile = Profile.builder().id(profileId).build();
        String playlistName = "my fav songs";
        Playlist mockedPlaylist = Playlist.builder()
                .id(1L)
                .name(playlistName)
                .profile(profile)
                .build();

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));
        when(playlistRepository.save(any())).thenReturn(mockedPlaylist);

        Playlist actualPlaylist = playlistService.create(profileId, playlistName);

        assertNotNull(actualPlaylist);
        assertNotNull(actualPlaylist.getId());
        assertEquals(playlistName, actualPlaylist.getName());
        assertEquals(profileId, actualPlaylist.getProfile().getId());
        verify(profileRepository, times(1)).findById(anyLong());
        verify(playlistRepository, times(1)).save(any());
    }

    @Test
    public void createWithProfileThatDoesNotExist() {
        Long profileId = 1L;
        String playlistName = "my fav songs";

        when(profileRepository.findById(profileId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> playlistService.create(profileId, playlistName));
        verify(profileRepository, times(1)).findById(anyLong());
        verify(profileRepository, times(0)).save(any());
        verify(playlistRepository, times(0)).save(any());
    }

    @Test
    public void findById() {
        final Long playlistId = 1L;
        final Playlist playlist = Playlist.builder().id(playlistId).name("these are my fav songs").build();

        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));

        assertEquals(playlist.getId(), playlistService.findById(playlistId).getId());
        verify(playlistRepository, times(1)).findById(anyLong());
    }

    @Test
    public void findByIdThatDoesNotExist() {
        final Long playlistId = 1L;
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> playlistService.findById(playlistId));
        verify(playlistRepository, times(1)).findById(anyLong());
    }

    @Test
    public void findAllByName() {
        String name = "my fav songs";
        List<Playlist> playlists = List.of(
                Playlist.builder().id(1L).name("these are my fav songs").build(),
                Playlist.builder().id(2L).name("MY FAV SONGS OF ALL TIME").build()
        );

        when(playlistRepository.findAllByNameContainingIgnoreCase(name)).thenReturn(playlists);

        assertEquals(2, playlistService.findAllByNameContainingIgnoreCase(name).size());
        verify(playlistRepository, times(1)).findAllByNameContainingIgnoreCase(anyString());
    }

    @Test
    public void findAllByProfileId() {
        final Long profileId = 1L;
        final List<Playlist> playlists = List.of(
                Playlist.builder().id(1L).name("heavy metal").build(),
                Playlist.builder().id(2L).name("cool funk").build()
        );

        when(playlistRepository.findAllByProfileId(profileId)).thenReturn(playlists);

        assertEquals(2, playlistService.findAllByProfileId(profileId).size());
        verify(playlistRepository, times(1)).findAllByProfileId(anyLong());
    }

    @Test
    public void addSong() {
        final Long profileId = 1L, playlistId = 1L, songId = 1L;

        Profile profile = Profile.builder().id(profileId).build();
        Playlist playlist = Playlist.builder().id(playlistId).build();
        Song song = Song.builder().id(songId).build();

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));
        when(songRepository.findById(songId)).thenReturn(Optional.of(song));

        Playlist expectedPlaylist = Playlist.builder().id(playlistId).songs(List.of(song)).build();

        when(playlistRepository.save(any())).thenReturn(expectedPlaylist);

        Playlist actualPlaylist = playlistService.addSong(profileId, playlistId, songId);

        assertEquals(profileId, actualPlaylist.getId());
        assertEquals(expectedPlaylist.getSongs().size(), actualPlaylist.getSongs().size());
        assertTrue(actualPlaylist.getSongs().containsAll(expectedPlaylist.getSongs()));
        verify(profileRepository, times(1)).findById(anyLong());
        verify(playlistRepository, times(1)).findById(anyLong());
        verify(songRepository, times(1)).findById(anyLong());
        verify(playlistRepository, times(1)).save(any());
    }

    @Test
    public void addSongWhenPlaylistDoesNotExist() {
        when(playlistRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> playlistService.addSong(1L, 1L, 1L));
        verify(playlistRepository, times(1)).findById(anyLong());
        verify(playlistRepository, times(0)).save(any());
    }

    @Test
    public void addSongWhenSongDoesNotExist() {
        when(playlistRepository.findById(anyLong())).thenReturn(Optional.of(Playlist.builder().build()));
        when(songRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> playlistService.addSong(1L, 1L, 1L));
        verify(songRepository, times(1)).findById(anyLong());
        verify(playlistRepository, times(0)).save(any());
    }

    @Test
    public void addSongWhenProfileDoesNotExist() {
        when(profileRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(playlistRepository.findById(anyLong())).thenReturn(Optional.of(Playlist.builder().build()));
        when(songRepository.findById(anyLong())).thenReturn(Optional.of(Song.builder().build()));

        assertThrows(RuntimeException.class, () -> playlistService.addSong(1L, 1L, 1L));
        verify(profileRepository, times(1)).findById(anyLong());
        verify(playlistRepository, times(0)).save(any());
    }

    @Test
    public void removeSong() {
        final Long profileId = 1L, playlistId = 1L, songId = 1L;

        Profile profile = Profile.builder().id(profileId).build();
        Song song = Song.builder().id(songId).build();
        Playlist playlist = Playlist.builder().id(playlistId).songs(new ArrayList<>(List.of(song))).build();

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));
        when(songRepository.findById(songId)).thenReturn(Optional.of(song));

        Playlist expectedPlaylist = Playlist.builder().id(playlistId).songs(List.of()).build();

        when(playlistRepository.save(any())).thenReturn(expectedPlaylist);

        Playlist actualPlaylist = playlistService.removeSong(profileId, playlistId, songId);

        assertEquals(profileId, actualPlaylist.getId());
        assertEquals(expectedPlaylist.getSongs().size(), actualPlaylist.getSongs().size());
        verify(profileRepository, times(1)).findById(anyLong());
        verify(playlistRepository, times(1)).findById(anyLong());
        verify(songRepository, times(1)).findById(anyLong());
        verify(playlistRepository, times(1)).save(any());
    }

    @Test
    public void removeSongWhenPlaylistDoesNotExist() {
        when(playlistRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> playlistService.removeSong(1L, 1L, 1L));
        verify(playlistRepository, times(1)).findById(anyLong());
        verify(playlistRepository, times(0)).save(any());
    }

    @Test
    public void removeSongWhenSongDoesNotExist() {
        when(playlistRepository.findById(anyLong())).thenReturn(Optional.of(Playlist.builder().build()));
        when(songRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> playlistService.removeSong(1L, 1L, 1L));
        verify(songRepository, times(1)).findById(anyLong());
        verify(playlistRepository, times(0)).save(any());
    }

    @Test
    public void removeSongWhenProfileDoesNotExist() {
        when(profileRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(playlistRepository.findById(anyLong())).thenReturn(Optional.of(Playlist.builder().build()));
        when(songRepository.findById(anyLong())).thenReturn(Optional.of(Song.builder().build()));

        assertThrows(RuntimeException.class, () -> playlistService.removeSong(1L, 1L, 1L));
        verify(profileRepository, times(1)).findById(anyLong());
        verify(playlistRepository, times(0)).save(any());
    }

    @Test
    public void deleteById() {
        final Long profileId = 1L, playlistId = 1L;
        when(profileRepository.findById(profileId)).thenReturn(Optional.of(Profile.builder().id(profileId).build()));

        playlistService.deleteById(profileId, playlistId);

        verify(playlistRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void deleteByIdWhenProfileDoesNotExist() {
        final Long profileId = 1L, playlistId = 1L;
        when(profileRepository.findById(profileId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> playlistService.deleteById(profileId, playlistId));
        verify(playlistRepository, times(0)).deleteById(anyLong());
    }
}
