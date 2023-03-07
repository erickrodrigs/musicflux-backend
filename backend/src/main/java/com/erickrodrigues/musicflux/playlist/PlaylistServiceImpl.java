package com.erickrodrigues.musicflux.playlist;

import com.erickrodrigues.musicflux.profile.ProfileRepository;
import com.erickrodrigues.musicflux.song.SongRepository;
import com.erickrodrigues.musicflux.profile.Profile;
import com.erickrodrigues.musicflux.song.Song;
import com.erickrodrigues.musicflux.shared.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaylistServiceImpl extends BaseService implements PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final ProfileRepository profileRepository;
    private final SongRepository songRepository;

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
    public List<Playlist> findAllByNameContainingIgnoreCase(String text) {
        return playlistRepository.findAllByNameContainingIgnoreCase(text);
    }

    @Override
    public List<Playlist> findAllByProfileId(Long profileId) {
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
