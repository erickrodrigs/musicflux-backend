package com.erickrodrigues.musicflux.song;

import com.erickrodrigues.musicflux.recently_played.RecentlyPlayedService;
import com.erickrodrigues.musicflux.shared.ResourceNotFoundException;
import com.erickrodrigues.musicflux.user.User;
import com.erickrodrigues.musicflux.shared.BaseService;
import com.erickrodrigues.musicflux.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SongServiceImpl extends BaseService implements SongService {

    private final SongRepository songRepository;
    private final UserService userService;
    private final RecentlyPlayedService recentlyPlayedService;

    @Transactional
    @Override
    public Song play(Long userId, Long songId) {
        final User user = userService.findById(userId);
        Song song = findById(songId);

        song.play();
        song = songRepository.save(song);
        recentlyPlayedService.save(song, user);

        return song;
    }

    @Override
    public Song findById(Long songId) {
        return songRepository
                .findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song with that ID does not exist"));
    }

    @Override
    public List<Song> findAllByTitleContainingIgnoreCase(String text) {
        return songRepository.findAllByTitleContainingIgnoreCase(text);
    }

    @Override
    public List<Song> findAllByGenreName(String genreName) {
        return songRepository.findAllByGenresNameIgnoreCase(genreName);
    }

    @Override
    public List<Song> findAllByAlbumId(Long albumId) {
        final List<Song> songs = songRepository.findAllByAlbumId(albumId);

        if (songs.isEmpty()) throw new ResourceNotFoundException("Album with that ID does not exist");

        return songs;
    }

    @Override
    public List<Song> findMostPlayedSongsByArtistId(Long artistId) {
        return songRepository
                .findAllByAlbumArtistsId(artistId)
                .stream()
                .sorted()
                .limit(5)
                .collect(Collectors.toList());
    }
}
