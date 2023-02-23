package com.erickrodrigues.musicflux.repositories;

import com.erickrodrigues.musicflux.domain.RecentlyListened;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecentlyListenedRepository extends JpaRepository<RecentlyListened, Long> {

    Page<RecentlyListened> findAllByProfileIdOrderByCreatedAtDesc(Pageable pageable, Long profileId);
}
