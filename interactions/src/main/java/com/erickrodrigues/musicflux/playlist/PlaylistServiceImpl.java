package com.erickrodrigues.musicflux.playlist;

import com.erickrodrigues.musicflux.shared.InvalidActionException;
import com.erickrodrigues.musicflux.track.TrackService;
import com.erickrodrigues.musicflux.user.User;
import com.erickrodrigues.musicflux.track.Track;
import com.erickrodrigues.musicflux.shared.BaseService;
import com.erickrodrigues.musicflux.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

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
    public Playlist addTracks(Long userId, Long playlistId, List<Long> tracksIds) {
        final User user = userService.findById(userId);
        final Playlist playlist = super.getEntityOrThrowException(playlistId, playlistRepository, Playlist.class);

        if (!Objects.equals(user.getId(), playlist.getUser().getId())) {
            throw new InvalidActionException("Cannot add tracks to another user's playlist");
        }

        tracksIds.forEach((trackId) -> {
            final Track track = trackService.findById(trackId);

            playlist.addTrack(track);
        });

        return playlistRepository.save(playlist);
    }

    @Transactional
    @Override
    public Playlist removeTracks(Long userId, Long playlistId, List<Long> tracksIds) {
        final User user = userService.findById(userId);
        final Playlist playlist = super.getEntityOrThrowException(playlistId, playlistRepository, Playlist.class);

        if (!Objects.equals(user.getId(), playlist.getUser().getId())) {
            throw new InvalidActionException("Cannot remove tracks from another user's playlist");
        }

        tracksIds.forEach((trackId) -> {
            final Track track = trackService.findById(trackId);
            playlist.removeTrack(track);
        });

        return playlistRepository.save(playlist);
    }

    @Override
    public void deleteById(Long userId, Long playlistId) {
        final User user = userService.findById(userId);
        final Playlist playlist = super.getEntityOrThrowException(playlistId, playlistRepository, Playlist.class);

        if (!Objects.equals(user.getId(), playlist.getUser().getId())) {
            throw new InvalidActionException("Cannot delete a playlist from another user");
        }

        playlistRepository.deleteById(playlistId);
    }
}
