package com.erickrodrigues.musicflux.playlist;

import com.erickrodrigues.musicflux.track.TrackService;
import com.erickrodrigues.musicflux.user.User;
import com.erickrodrigues.musicflux.track.Track;
import com.erickrodrigues.musicflux.shared.BaseService;
import com.erickrodrigues.musicflux.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaylistServiceImpl extends BaseService implements PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final UserService userService;
    private final TrackService trackService;

    @Transactional
    @Override
    public Playlist create(Long userId, String name) {
        final User user = userService.findById(userId);
        Playlist playlist = Playlist.builder()
                .name(name)
                .user(user)
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
    public List<Playlist> findAllByUserId(Long userId) {
        return playlistRepository.findAllByUserId(userId);
    }

    @Transactional
    @Override
    public Playlist addTrack(Long userId, Long playlistId, Long trackId) {
        final Playlist playlist = super.getEntityOrThrowException(playlistId, playlistRepository, Playlist.class);
        final Track track = trackService.findById(trackId);
        userService.findById(userId);

        playlist.addTrack(track);
        return playlistRepository.save(playlist);
    }

    @Transactional
    @Override
    public Playlist removeTrack(Long userId, Long playlistId, Long trackId) {
        final Playlist playlist = super.getEntityOrThrowException(playlistId, playlistRepository, Playlist.class);
        final Track track = trackService.findById(trackId);
        userService.findById(userId);

        playlist.removeTrack(track);
        return playlistRepository.save(playlist);
    }

    @Override
    public void deleteById(Long userId, Long playlistId) {
        userService.findById(userId);
        super.getEntityOrThrowException(playlistId, playlistRepository, Playlist.class);
        playlistRepository.deleteById(playlistId);
    }
}
