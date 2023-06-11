package com.erickrodrigues.musicflux.favorite;

import com.erickrodrigues.musicflux.shared.ResourceAlreadyExistsException;
import com.erickrodrigues.musicflux.track.TrackService;
import com.erickrodrigues.musicflux.track.Track;
import com.erickrodrigues.musicflux.user.User;
import com.erickrodrigues.musicflux.shared.BaseService;
import com.erickrodrigues.musicflux.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl extends BaseService implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserService userService;
    private final TrackService trackService;

    @Transactional
    @Override
    public Favorite likeTrack(Long userId, Long trackId) {
        final Track track = trackService.findById(trackId);
        final User user = userService.findById(userId);

        favoriteRepository
                .findByTrackId(trackId)
                .ifPresent((f) -> {
                    throw new ResourceAlreadyExistsException("Track with that ID is already liked");
                });

        final Favorite favorite = Favorite.builder()
                .user(user)
                .track(track)
                .build();

        return favoriteRepository.save(favorite);
    }

    @Transactional
    @Override
    public void dislikeTrack(Long userId, Long favoriteId) {
        final Favorite favorite = super.getEntityOrThrowException(favoriteId, favoriteRepository, Favorite.class);
        userService.findById(userId);

        favoriteRepository.delete(favorite);
    }

    @Override
    public List<Favorite> findAllByUserId(Long userId) {
        return favoriteRepository.findAllByUserId(userId);
    }

    @Override
    public Map<Long, Boolean> checkWhetherTracksAreLiked(Long userId, List<Long> tracksIds) {
        userService.findById(userId);
        return tracksIds
                .stream()
                .map(trackService::findById)
                .collect(Collectors.toMap(Track::getId, (track) ->
                        favoriteRepository
                                .findByTrackId(track.getId())
                                .isPresent()
                ));
    }
}
