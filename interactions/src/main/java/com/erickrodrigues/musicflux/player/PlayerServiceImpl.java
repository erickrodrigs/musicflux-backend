package com.erickrodrigues.musicflux.player;

import com.erickrodrigues.musicflux.recently_played.RecentlyPlayedService;
import com.erickrodrigues.musicflux.track.Track;
import com.erickrodrigues.musicflux.track.TrackService;
import com.erickrodrigues.musicflux.user.User;
import com.erickrodrigues.musicflux.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final UserService userService;
    private final TrackService trackService;
    private final RecentlyPlayedService recentlyPlayedService;

    @Transactional
    @Override
    public Track play(Long userId, Long trackId) {
        final User user = userService.findById(userId);
        Track track = trackService.play(trackId);
        recentlyPlayedService.save(track, user);

        return track;
    }
}
