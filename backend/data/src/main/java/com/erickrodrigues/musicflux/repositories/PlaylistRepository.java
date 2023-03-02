package com.erickrodrigues.musicflux.repositories;

import com.erickrodrigues.musicflux.domain.Playlist;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlaylistRepository extends CrudRepository<Playlist, Long> {

    List<Playlist> findAllByNameContainingIgnoreCase(String name);

    List<Playlist> findAllByProfileId(Long profileId);
}
