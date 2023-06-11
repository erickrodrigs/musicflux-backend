package com.erickrodrigues.musicflux.recently_played;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecentlyPlayedRepository extends JpaRepository<RecentlyPlayed, Long> {

    Page<RecentlyPlayed> findAllByUserIdOrderByCreatedAtDesc(Pageable pageable, Long userId);
}
