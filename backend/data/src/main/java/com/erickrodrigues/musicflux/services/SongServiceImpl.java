package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Album;
import com.erickrodrigues.musicflux.domain.Profile;
import com.erickrodrigues.musicflux.domain.Song;
import com.erickrodrigues.musicflux.repositories.AlbumRepository;
import com.erickrodrigues.musicflux.repositories.ProfileRepository;
import com.erickrodrigues.musicflux.repositories.SongRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;
    private final ProfileRepository profileRepository;
    private final AlbumRepository albumRepository;

    public SongServiceImpl(SongRepository songRepository,
                           ProfileRepository profileRepository,
                           AlbumRepository albumRepository) {
        this.songRepository = songRepository;
        this.profileRepository = profileRepository;
        this.albumRepository = albumRepository;
    }

    @Transactional
    @Override
    public void play(Long profileId, Long songId) {
        final Optional<Song> optionalSong = this.songRepository.findById(songId);

        if (optionalSong.isEmpty()) {
            throw new RuntimeException("Song with that ID does not exist");
        }

        final Optional<Profile> optionalProfile = this.profileRepository.findById(profileId);

        if (optionalProfile.isEmpty()) {
            throw new RuntimeException("Profile with that ID does not exist");
        }

        final Song song = optionalSong.get();
        final Profile profile = optionalProfile.get();

        song.play();
        profile.addRecentlyListenedSong(song);

        this.songRepository.save(song);
        this.profileRepository.save(profile);
    }

    @Override
    public Set<Song> findAllByTitle(String title) {
        return this.songRepository.findAllByTitleIsContainingIgnoreCase(title);
    }

    @Override
    public Set<Song> findAllByAlbumId(Long albumId) {
        return this.songRepository.findAllByAlbumId(albumId);
    }

    @Override
    public Set<Song> findMostListenedSongsByArtistId(Long artistId) {
        final Set<Album> albums = this.albumRepository.findAllByArtistsIn(Set.of(artistId));
        final TreeSet<Song> allSongs = new TreeSet<>();

        albums.forEach(album -> allSongs.addAll(album.getSongs()));

        return allSongs
                .stream()
                .limit(5)
                .collect(Collectors.toCollection(TreeSet::new));
    }
}
