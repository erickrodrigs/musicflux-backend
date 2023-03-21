package com.erickrodrigues.musicflux.playlist;

import com.erickrodrigues.musicflux.song.SongService;
import com.erickrodrigues.musicflux.user.User;
import com.erickrodrigues.musicflux.song.Song;
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
    private final SongService songService;

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
    public Playlist addSong(Long userId, Long playlistId, Long songId) {
        final Playlist playlist = super.getEntityOrThrowException(playlistId, playlistRepository, Playlist.class);
        final Song song = songService.findById(songId);
        userService.findById(userId);

        playlist.addSong(song);
        return playlistRepository.save(playlist);
    }

    @Transactional
    @Override
    public Playlist removeSong(Long userId, Long playlistId, Long songId) {
        final Playlist playlist = super.getEntityOrThrowException(playlistId, playlistRepository, Playlist.class);
        final Song song = songService.findById(songId);
        userService.findById(userId);

        playlist.removeSong(song);
        return playlistRepository.save(playlist);
    }

    @Override
    public void deleteById(Long userId, Long playlistId) {
        userService.findById(userId);
        super.getEntityOrThrowException(playlistId, playlistRepository, Playlist.class);
        playlistRepository.deleteById(playlistId);
    }
}
