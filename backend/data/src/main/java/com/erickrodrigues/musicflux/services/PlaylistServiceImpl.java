package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Playlist;
import com.erickrodrigues.musicflux.domain.Profile;
import com.erickrodrigues.musicflux.domain.Song;
import com.erickrodrigues.musicflux.repositories.PlaylistRepository;
import com.erickrodrigues.musicflux.repositories.ProfileRepository;
import com.erickrodrigues.musicflux.repositories.SongRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
public class PlaylistServiceImpl implements PlaylistService {

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
        final Profile profile = this.getEntityOrThrowException(profileId, this.profileRepository, Profile.class);
        Playlist playlist = Playlist.builder()
                .name(name)
                .profile(profile)
                .build();

        profile.addPlaylist(playlist);
        playlist = playlistRepository.save(playlist);
        profileRepository.save(profile);

        return playlist;
    }

    @Override
    public Set<Playlist> findAllByName(String name) {
        return playlistRepository.findAllByNameContainingIgnoreCase(name);
    }

    @Override
    public Set<Playlist> findAllByProfileId(Long profileId) {
        final Profile profile = this.getEntityOrThrowException(profileId, this.profileRepository, Profile.class);

        return profile.getPlaylists();
    }

    @Transactional
    @Override
    public Playlist addSong(Long profileId, Long playlistId, Long songId) {
        final Playlist playlist = this.getEntityOrThrowException(playlistId, this.playlistRepository, Playlist.class);
        final Song song = this.getEntityOrThrowException(songId, this.songRepository, Song.class);
        this.getEntityOrThrowException(profileId, this.profileRepository, Profile.class);

        playlist.addSong(song);
        return playlistRepository.save(playlist);
    }

    @Transactional
    @Override
    public Playlist removeSong(Long profileId, Long playlistId, Long songId) {
        final Playlist playlist = this.getEntityOrThrowException(playlistId, this.playlistRepository, Playlist.class);
        final Song song = this.getEntityOrThrowException(songId, this.songRepository, Song.class);
        this.getEntityOrThrowException(profileId, this.profileRepository, Profile.class);

        playlist.removeSong(song);
        return playlistRepository.save(playlist);
    }

    private <T> T getEntityOrThrowException(Long entityId, CrudRepository<T, Long> repository, Class<T> tClass) {
        final Optional<T> optionalEntity = repository.findById(entityId);

        if (optionalEntity.isEmpty()) {
            throw new RuntimeException(tClass.getSimpleName() + " with that ID does not exist");
        }

        return optionalEntity.get();
    }
}
