package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Favorite;
import com.erickrodrigues.musicflux.domain.Profile;
import com.erickrodrigues.musicflux.domain.Song;
import com.erickrodrigues.musicflux.repositories.FavoriteRepository;
import com.erickrodrigues.musicflux.repositories.ProfileRepository;
import com.erickrodrigues.musicflux.repositories.SongRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class FavoriteServiceImpl extends BaseService implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ProfileRepository profileRepository;
    private final SongRepository songRepository;

    public FavoriteServiceImpl(FavoriteRepository favoriteRepository,
                               ProfileRepository profileRepository,
                               SongRepository songRepository) {
        this.favoriteRepository = favoriteRepository;
        this.profileRepository = profileRepository;
        this.songRepository = songRepository;
    }

    @Transactional
    @Override
    public Favorite likeSong(Long profileId, Long songId) {
        final Song song = super.getEntityOrThrowException(songId, songRepository, Song.class);
        final Profile profile = super.getEntityOrThrowException(profileId, profileRepository, Profile.class);
        final Favorite favorite = Favorite.builder()
                .profile(profile)
                .song(song)
                .build();

        return favoriteRepository.save(favorite);
    }

    @Transactional
    @Override
    public void dislikeSong(Long profileId, Long favoriteId) {
        final Favorite favorite = super.getEntityOrThrowException(favoriteId, favoriteRepository, Favorite.class);
        super.getEntityOrThrowException(profileId, profileRepository, Profile.class);

        favoriteRepository.delete(favorite);
    }

    @Override
    public Set<Favorite> findAllByProfileId(Long profileId) {
        return favoriteRepository.findAllByProfileId(profileId);
    }
}
