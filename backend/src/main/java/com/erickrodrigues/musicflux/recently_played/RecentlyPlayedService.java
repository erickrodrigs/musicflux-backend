package com.erickrodrigues.musicflux.recently_played;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecentlyPlayedService {

    Page<RecentlyPlayed> findAllByProfileId(Pageable pageable, Long profileId);
}
