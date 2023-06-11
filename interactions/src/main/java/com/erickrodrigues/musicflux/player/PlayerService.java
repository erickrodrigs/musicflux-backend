package com.erickrodrigues.musicflux.player;

import com.erickrodrigues.musicflux.track.Track;

public interface PlayerService {

    Track play(Long userId, Long trackId);
}
