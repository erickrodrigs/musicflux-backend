package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.RecentlyPlayed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecentlyPlayedService {

    Page<RecentlyPlayed> findAllByProfileId(Pageable pageable, Long profileId);
}
