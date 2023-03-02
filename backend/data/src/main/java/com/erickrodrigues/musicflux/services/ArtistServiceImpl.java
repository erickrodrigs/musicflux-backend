package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Artist;
import com.erickrodrigues.musicflux.repositories.ArtistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistServiceImpl extends BaseService implements ArtistService {

    private final ArtistRepository artistRepository;

    public ArtistServiceImpl(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public List<Artist> findAllByNameContainingIgnoreCase(String text) {
        return artistRepository.findAllByNameContainingIgnoreCase(text);
    }
}
