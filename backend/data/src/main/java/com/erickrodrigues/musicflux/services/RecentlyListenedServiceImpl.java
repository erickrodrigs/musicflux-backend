package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Profile;
import com.erickrodrigues.musicflux.domain.RecentlyListened;
import com.erickrodrigues.musicflux.repositories.ProfileRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class RecentlyListenedServiceImpl extends BaseService implements RecentlyListenedService {

    private final ProfileRepository profileRepository;

    public RecentlyListenedServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<RecentlyListened> findAllByProfileId(Pageable pageable, Long profileId) {
        final Profile profile = super.getEntityOrThrowException(profileId, profileRepository, Profile.class);

        final List<RecentlyListened> recentlyListenedSongs = profile
                .getRecentlyListenedSongs()
                .stream()
                .sorted(Comparator.comparing(RecentlyListened::getCreatedAt).reversed())
                .toList();

        if (pageable.getOffset() >= recentlyListenedSongs.size()) {
            return Page.empty();
        }

        final int startIndex = (int) pageable.getOffset();
        final int endIndex = (int) (
                (pageable.getOffset() + pageable.getPageSize()) > recentlyListenedSongs.size()
                        ? recentlyListenedSongs.size()
                        : pageable.getOffset() + pageable.getPageSize()
        );

        return new PageImpl<>(
                recentlyListenedSongs.subList(startIndex, endIndex),
                pageable,
                recentlyListenedSongs.size()
        );
    }
}
