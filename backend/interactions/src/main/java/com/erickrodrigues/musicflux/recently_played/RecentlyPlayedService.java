package com.erickrodrigues.musicflux.recently_played;

import com.erickrodrigues.musicflux.track.Track;
import com.erickrodrigues.musicflux.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecentlyPlayedService {

    Page<RecentlyPlayed> findAllByUserId(Pageable pageable, Long userId);

    RecentlyPlayed save(Track track, User user);
}
