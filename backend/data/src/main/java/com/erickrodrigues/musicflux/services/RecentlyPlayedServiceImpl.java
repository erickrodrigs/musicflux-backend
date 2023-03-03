package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.RecentlyPlayed;
import com.erickrodrigues.musicflux.repositories.RecentlyPlayedRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecentlyPlayedServiceImpl extends BaseService implements RecentlyPlayedService {

    private final RecentlyPlayedRepository recentlyPlayedRepository;

    public RecentlyPlayedServiceImpl(RecentlyPlayedRepository recentlyPlayedRepository) {
        this.recentlyPlayedRepository = recentlyPlayedRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<RecentlyPlayed> findAllByProfileId(Pageable pageable, Long profileId) {
        return recentlyPlayedRepository.findAllByProfileIdOrderByCreatedAtDesc(pageable, profileId);
    }
}
