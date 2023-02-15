package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.RecentlyListened;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecentlyListenedService {

    Page<RecentlyListened> findAll(Pageable pageable, Long profileId);
}
