package com.erickrodrigues.musicflux.artist;

import com.erickrodrigues.musicflux.shared.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistServiceImpl extends BaseService implements ArtistService {

    private final ArtistRepository artistRepository;

    @Override
    public List<Artist> findAllByNameContainingIgnoreCase(String text) {
        return artistRepository.findAllByNameContainingIgnoreCase(text);
    }
}
