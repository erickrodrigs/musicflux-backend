package com.erickrodrigues.musicflux.repositories;

import com.erickrodrigues.musicflux.domain.RecentlyPlayed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecentlyPlayedRepository extends JpaRepository<RecentlyPlayed, Long> {

    Page<RecentlyPlayed> findAllByProfileIdOrderByCreatedAtDesc(Pageable pageable, Long profileId);
}
