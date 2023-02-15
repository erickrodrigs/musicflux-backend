package com.erickrodrigues.musicflux.repositories;

import com.erickrodrigues.musicflux.domain.Playlist;
import org.springframework.data.repository.CrudRepository;

public interface PlaylistRepository extends CrudRepository<Playlist, Long> {}
