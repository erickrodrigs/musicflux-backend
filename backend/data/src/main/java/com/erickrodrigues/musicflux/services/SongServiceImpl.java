package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Album;
import com.erickrodrigues.musicflux.domain.Profile;
import com.erickrodrigues.musicflux.domain.RecentlyPlayed;
import com.erickrodrigues.musicflux.domain.Song;
import com.erickrodrigues.musicflux.repositories.AlbumRepository;
import com.erickrodrigues.musicflux.repositories.ProfileRepository;
import com.erickrodrigues.musicflux.repositories.RecentlyPlayedRepository;
import com.erickrodrigues.musicflux.repositories.SongRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SongServiceImpl extends BaseService implements SongService {

    private final SongRepository songRepository;
    private final ProfileRepository profileRepository;
    private final AlbumRepository albumRepository;
    private final RecentlyPlayedRepository recentlyPlayedRepository;

    public SongServiceImpl(SongRepository songRepository,
                           ProfileRepository profileRepository,
                           AlbumRepository albumRepository,
                           RecentlyPlayedRepository recentlyPlayedRepository) {
        this.songRepository = songRepository;
        this.profileRepository = profileRepository;
        this.albumRepository = albumRepository;
        this.recentlyPlayedRepository = recentlyPlayedRepository;
    }

    @Transactional
    @Override
    public void play(Long profileId, Long songId) {
        final Song song = super.getEntityOrThrowException(songId, songRepository, Song.class);
        final Profile profile = super.getEntityOrThrowException(profileId, profileRepository, Profile.class);

        song.play();

        final RecentlyPlayed recentlyPlayed = RecentlyPlayed.builder()
                .profile(profile)
                .song(song)
                .build();

        songRepository.save(song);
        profileRepository.save(profile);
        recentlyPlayedRepository.save(recentlyPlayed);
    }

    @Override
    public List<Song> findAllByTitleContainingIgnoreCase(String text) {
        return songRepository.findAllByTitleContainingIgnoreCase(text);
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
