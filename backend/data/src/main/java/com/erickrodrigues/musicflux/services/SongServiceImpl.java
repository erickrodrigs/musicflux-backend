package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Album;
import com.erickrodrigues.musicflux.domain.Profile;
import com.erickrodrigues.musicflux.domain.Song;
import com.erickrodrigues.musicflux.repositories.AlbumRepository;
import com.erickrodrigues.musicflux.repositories.ProfileRepository;
import com.erickrodrigues.musicflux.repositories.SongRepository;
import org.springframework.data.repository.CrudRepository;
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
        final Song song = this.getEntityOrThrowException(songId, songRepository, Song.class);
        final Profile profile = this.getEntityOrThrowException(profileId, profileRepository, Profile.class);

        song.play();
        profile.addRecentlyListenedSong(song);

        this.songRepository.save(song);
        this.profileRepository.save(profile);
    }

    @Override
    public Set<Song> findAllByTitle(String title) {
        return this.songRepository.findAllByTitleContainingIgnoreCase(title);
    }

    @Override
    public Set<Song> findAllByAlbumId(Long albumId) {
        final Album album = this.getEntityOrThrowException(albumId, albumRepository, Album.class);
        return album.getSongs();
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

    private <T> T getEntityOrThrowException(Long entityId, CrudRepository<T, Long> repository, Class<T> tClass) {
        final Optional<T> optionalEntity = repository.findById(entityId);

        if (optionalEntity.isEmpty()) {
            throw new RuntimeException(tClass.getSimpleName() + " with that ID does not exist");
        }

        return optionalEntity.get();
    }
}
