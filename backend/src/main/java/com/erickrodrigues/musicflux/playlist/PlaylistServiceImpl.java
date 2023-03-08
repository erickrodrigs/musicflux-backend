package com.erickrodrigues.musicflux.playlist;

import com.erickrodrigues.musicflux.user.UserRepository;
import com.erickrodrigues.musicflux.user.User;
import com.erickrodrigues.musicflux.song.SongRepository;
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
    private final UserRepository userRepository;
    private final SongRepository songRepository;

    @Transactional
    @Override
    public Playlist create(Long userId, String name) {
        final User user = super.getEntityOrThrowException(userId, userRepository, User.class);
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
    public Playlist addSong(Long userId, Long playlistId, Long songId) {
        final Playlist playlist = super.getEntityOrThrowException(playlistId, playlistRepository, Playlist.class);
        final Song song = super.getEntityOrThrowException(songId, songRepository, Song.class);
        super.getEntityOrThrowException(userId, userRepository, User.class);

        playlist.addSong(song);
        return playlistRepository.save(playlist);
    }

    @Transactional
    @Override
    public Playlist removeSong(Long userId, Long playlistId, Long songId) {
        final Playlist playlist = super.getEntityOrThrowException(playlistId, playlistRepository, Playlist.class);
        final Song song = super.getEntityOrThrowException(songId, songRepository, Song.class);
        super.getEntityOrThrowException(userId, userRepository, User.class);

        playlist.removeSong(song);
        return playlistRepository.save(playlist);
    }

    @Override
    public void deleteById(Long userId, Long playlistId) {
        super.getEntityOrThrowException(userId, userRepository, User.class);
        playlistRepository.deleteById(playlistId);
    }
}
