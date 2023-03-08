package com.erickrodrigues.musicflux.favorite;

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
public class FavoriteServiceImpl extends BaseService implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final SongRepository songRepository;

    @Transactional
    @Override
    public Favorite likeSong(Long userId, Long songId) {
        final Song song = super.getEntityOrThrowException(songId, songRepository, Song.class);
        final User user = super.getEntityOrThrowException(userId, userRepository, User.class);
        final Favorite favorite = Favorite.builder()
                .user(user)
                .song(song)
                .build();

        return favoriteRepository.save(favorite);
    }

    @Transactional
    @Override
    public void dislikeSong(Long userId, Long favoriteId) {
        final Favorite favorite = super.getEntityOrThrowException(favoriteId, favoriteRepository, Favorite.class);
        super.getEntityOrThrowException(userId, userRepository, User.class);

        favoriteRepository.delete(favorite);
    }

    @Override
    public List<Favorite> findAllByUserId(Long userId) {
        return favoriteRepository.findAllByUserId(userId);
    }
}
