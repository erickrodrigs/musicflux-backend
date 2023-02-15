package com.erickrodrigues.musicflux.repositories;

import com.erickrodrigues.musicflux.domain.Album;
import org.springframework.data.repository.CrudRepository;

public interface AlbumRepository extends CrudRepository<Album, Long> {}
