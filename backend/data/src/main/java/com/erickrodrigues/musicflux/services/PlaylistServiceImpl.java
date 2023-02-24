package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Playlist;
import com.erickrodrigues.musicflux.domain.Profile;
import com.erickrodrigues.musicflux.domain.Song;
import com.erickrodrigues.musicflux.repositories.PlaylistRepository;
import com.erickrodrigues.musicflux.repositories.ProfileRepository;
import com.erickrodrigues.musicflux.repositories.SongRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class PlaylistServiceImpl extends BaseService implements PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final ProfileRepository profileRepository;
    private final SongRepository songRepository;

    public PlaylistServiceImpl(PlaylistRepository playlistRepository,
                               ProfileRepository profileRepository,
                               SongRepository songRepository) {
        this.playlistRepository = playlistRepository;
        this.profileRepository = profileRepository;
        this.songRepository = songRepository;
    }

    @Transactional
    @Override
    public Playlist create(Long profileId, String name) {
        final Profile profile = super.getEntityOrThrowException(profileId, profileRepository, Profile.class);
        Playlist playlist = Playlist.builder()
                .name(name)
                .profile(profile)
                .build();

        return playlistRepository.save(playlist);
    }

    @Override
    public Playlist findById(Long playlistId) {
        return super.getEntityOrThrowException(playlistId, playlistRepository, Playlist.class);
    }

    @Override
    public Set<Playlist> findAllByNameContainingIgnoreCase(String text) {
        return playlistRepository.findAllByNameContainingIgnoreCase(text);
    }

    @Override
    public Set<Playlist> findAllByProfileId(Long profileId) {
        return playlistRepository.findAllByProfileId(profileId);
    }

    @Transactional
    @Override
    public Playlist addSong(Long profileId, Long playlistId, Long songId) {
        final Playlist playlist = super.getEntityOrThrowException(playlistId, playlistRepository, Playlist.class);
        final Song song = super.getEntityOrThrowException(songId, songRepository, Song.class);
        super.getEntityOrThrowException(profileId, profileRepository, Profile.class);

        playlist.addSong(song);
        return playlistRepository.save(playlist);
    }

    @Transactional
    @Override
    public Playlist removeSong(Long profileId, Long playlistId, Long songId) {
        final Playlist playlist = super.getEntityOrThrowException(playlistId, playlistRepository, Playlist.class);
        final Song song = super.getEntityOrThrowException(songId, songRepository, Song.class);
        super.getEntityOrThrowException(profileId, profileRepository, Profile.class);

        playlist.removeSong(song);
        return playlistRepository.save(playlist);
    }

    @Override
    public void deleteById(Long profileId, Long playlistId) {
        super.getEntityOrThrowException(profileId, profileRepository, Profile.class);
        playlistRepository.deleteById(playlistId);
    }
}
