package com.erickrodrigues.musicflux.song;

import com.erickrodrigues.musicflux.user.User;
import com.erickrodrigues.musicflux.recently_played.RecentlyPlayed;
import com.erickrodrigues.musicflux.album.AlbumRepository;
import com.erickrodrigues.musicflux.user.UserRepository;
import com.erickrodrigues.musicflux.album.Album;
import com.erickrodrigues.musicflux.recently_played.RecentlyPlayedRepository;
import com.erickrodrigues.musicflux.shared.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SongServiceImpl extends BaseService implements SongService {

    private final SongRepository songRepository;
    private final UserRepository userRepository;
    private final AlbumRepository albumRepository;
    private final RecentlyPlayedRepository recentlyPlayedRepository;

    @Transactional
    @Override
    public void play(Long userId, Long songId) {
        final Song song = super.getEntityOrThrowException(songId, songRepository, Song.class);
        final User user = super.getEntityOrThrowException(userId, userRepository, User.class);

        song.play();

        final RecentlyPlayed recentlyPlayed = RecentlyPlayed.builder()
                .user(user)
                .song(song)
                .build();

        songRepository.save(song);
        userRepository.save(user);
        recentlyPlayedRepository.save(recentlyPlayed);
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
        return super.getEntityOrThrowException(albumId, albumRepository, Album.class).getSongs();
    }

    @Override
    public List<Song> findMostPlayedSongsByArtistId(Long artistId) {
        final List<Album> albums = albumRepository.findAllByArtistsIn(List.of(artistId));
        final List<Song> allSongs = new ArrayList<>();

        albums.forEach(album -> allSongs.addAll(album.getSongs()));

        return allSongs
                .stream()
                .sorted()
                .limit(5)
                .collect(Collectors.toList());
    }
}
