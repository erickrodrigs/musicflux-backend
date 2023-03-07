package com.erickrodrigues.musicflux.playlist;

import com.erickrodrigues.musicflux.playlist.Playlist;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlaylistRepository extends CrudRepository<Playlist, Long> {

    List<Playlist> findAllByNameContainingIgnoreCase(String name);

    List<Playlist> findAllByProfileId(Long profileId);
}
