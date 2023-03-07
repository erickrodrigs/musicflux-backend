package com.erickrodrigues.musicflux.favorite;

import com.erickrodrigues.musicflux.profile.ProfileRepository;
import com.erickrodrigues.musicflux.song.SongRepository;
import com.erickrodrigues.musicflux.profile.Profile;
import com.erickrodrigues.musicflux.song.Song;
import com.erickrodrigues.musicflux.shared.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl extends BaseService implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ProfileRepository profileRepository;
    private final SongRepository songRepository;

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
    public List<Favorite> findAllByProfileId(Long profileId) {
        return favoriteRepository.findAllByProfileId(profileId);
    }
}
