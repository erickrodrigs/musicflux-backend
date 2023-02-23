package com.erickrodrigues.musicflux.repositories;

import com.erickrodrigues.musicflux.domain.Playlist;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface PlaylistRepository extends CrudRepository<Playlist, Long> {

    Set<Playlist> findAllByNameContainingIgnoreCase(String name);

    Set<Playlist> findAllByProfileId(Long profileId);
}
