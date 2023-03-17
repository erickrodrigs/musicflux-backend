package com.erickrodrigues.musicflux.recently_played;

import com.erickrodrigues.musicflux.shared.BaseService;
import com.erickrodrigues.musicflux.song.Song;
import com.erickrodrigues.musicflux.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecentlyPlayedServiceImpl extends BaseService implements RecentlyPlayedService {

    private final RecentlyPlayedRepository recentlyPlayedRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<RecentlyPlayed> findAllByUserId(Pageable pageable, Long userId) {
        return recentlyPlayedRepository.findAllByUserIdOrderByCreatedAtDesc(pageable, userId);
    }

    @Override
    public RecentlyPlayed save(Song song, User user) {
        return recentlyPlayedRepository.save(RecentlyPlayed.builder()
                .user(user)
                .song(song)
                .build()
        );
    }
}
