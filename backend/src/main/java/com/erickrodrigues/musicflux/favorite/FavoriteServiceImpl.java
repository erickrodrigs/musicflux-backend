package com.erickrodrigues.musicflux.favorite;

import com.erickrodrigues.musicflux.shared.ResourceAlreadyExistsException;
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
public class FavoriteServiceImpl extends BaseService implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserService userService;
    private final SongService songService;

    @Transactional
    @Override
    public Favorite likeSong(Long userId, Long songId) {
        final Song song = songService.findById(songId);
        final User user = userService.findById(userId);

        favoriteRepository
                .findBySongId(songId)
                .ifPresent((f) -> {
                    throw new ResourceAlreadyExistsException("Song with that ID is already liked");
                });

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
        userService.findById(userId);

        favoriteRepository.delete(favorite);
    }

    @Override
    public List<Favorite> findAllByUserId(Long userId) {
        return favoriteRepository.findAllByUserId(userId);
    }
}
