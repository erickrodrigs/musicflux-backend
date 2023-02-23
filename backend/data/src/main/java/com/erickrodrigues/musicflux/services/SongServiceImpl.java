package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Album;
import com.erickrodrigues.musicflux.domain.Profile;
import com.erickrodrigues.musicflux.domain.RecentlyListened;
import com.erickrodrigues.musicflux.domain.Song;
import com.erickrodrigues.musicflux.repositories.AlbumRepository;
import com.erickrodrigues.musicflux.repositories.ProfileRepository;
import com.erickrodrigues.musicflux.repositories.RecentlyListenedRepository;
import com.erickrodrigues.musicflux.repositories.SongRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SongServiceImpl extends BaseService implements SongService {

    private final SongRepository songRepository;
    private final ProfileRepository profileRepository;
    private final AlbumRepository albumRepository;
    private final RecentlyListenedRepository recentlyListenedRepository;

    public SongServiceImpl(SongRepository songRepository,
                           ProfileRepository profileRepository,
                           AlbumRepository albumRepository,
                           RecentlyListenedRepository recentlyListenedRepository) {
        this.songRepository = songRepository;
        this.profileRepository = profileRepository;
        this.albumRepository = albumRepository;
        this.recentlyListenedRepository = recentlyListenedRepository;
    }

    @Transactional
    @Override
    public void play(Long profileId, Long songId) {
        final Song song = super.getEntityOrThrowException(songId, songRepository, Song.class);
        final Profile profile = super.getEntityOrThrowException(profileId, profileRepository, Profile.class);

        song.play();

        final RecentlyListened recentlyListened = RecentlyListened.builder()
                .profile(profile)
                .song(song)
                .build();

        songRepository.save(song);
        profileRepository.save(profile);
        recentlyListenedRepository.save(recentlyListened);
    }

    @Override
    public Set<Song> findAllByTitle(String title) {
        return songRepository.findAllByTitleContainingIgnoreCase(title);
    }

    @Override
    public Set<Song> findAllByAlbumId(Long albumId) {
        return super.getEntityOrThrowException(albumId, albumRepository, Album.class).getSongs();
    }

    @Override
    public Set<Song> findMostListenedSongsByArtistId(Long artistId) {
        final Set<Album> albums = albumRepository.findAllByArtistsIn(Set.of(artistId));
        final TreeSet<Song> allSongs = new TreeSet<>();

        albums.forEach(album -> allSongs.addAll(album.getSongs()));

        return allSongs
                .stream()
                .limit(5)
                .collect(Collectors.toCollection(TreeSet::new));
    }
}
