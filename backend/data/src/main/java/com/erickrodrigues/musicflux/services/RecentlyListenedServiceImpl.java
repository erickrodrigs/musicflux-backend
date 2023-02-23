package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.RecentlyListened;
import com.erickrodrigues.musicflux.repositories.RecentlyListenedRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecentlyListenedServiceImpl extends BaseService implements RecentlyListenedService {

    private final RecentlyListenedRepository recentlyListenedRepository;

    public RecentlyListenedServiceImpl(RecentlyListenedRepository recentlyListenedRepository) {
        this.recentlyListenedRepository = recentlyListenedRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<RecentlyListened> findAllByProfileId(Pageable pageable, Long profileId) {
        return recentlyListenedRepository.findAllByProfileIdOrderByCreatedAtDesc(pageable, profileId);
    }
}
