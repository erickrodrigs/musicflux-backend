package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Album;
import com.erickrodrigues.musicflux.domain.Artist;
import com.erickrodrigues.musicflux.repositories.AlbumRepository;
import com.erickrodrigues.musicflux.repositories.ArtistRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;

    public AlbumServiceImpl(AlbumRepository albumRepository, ArtistRepository artistRepository) {
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
    }

    @Override
    public Set<Album> findAllByTitle(String title) {
        return this.albumRepository.findAllByTitleContainingIgnoreCase(title);
    }

    @Override
    public Set<Album> findAllByArtistId(Long artistId) {
        final Optional<Artist> optionalArtist = this.artistRepository.findById(artistId);

        if (optionalArtist.isEmpty()) {
            throw new RuntimeException("Artist with that ID does not exist");
        }

        return optionalArtist.get().getAlbums();
    }
}
