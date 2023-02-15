package com.erickrodrigues.musicflux.repositories;

import com.erickrodrigues.musicflux.domain.RecentlyListened;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RecentlyListenedRepository extends PagingAndSortingRepository<RecentlyListened, Long> {}
