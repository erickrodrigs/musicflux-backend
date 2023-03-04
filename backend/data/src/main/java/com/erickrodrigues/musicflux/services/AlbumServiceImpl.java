package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Album;
import com.erickrodrigues.musicflux.domain.Artist;
import com.erickrodrigues.musicflux.repositories.AlbumRepository;
import com.erickrodrigues.musicflux.repositories.ArtistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlbumServiceImpl extends BaseService implements AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;

    public AlbumServiceImpl(AlbumRepository albumRepository, ArtistRepository artistRepository) {
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
    }

    @Override
    public List<Album> findAllByTitleContainingIgnoreCase(String text) {
        return albumRepository.findAllByTitleContainingIgnoreCase(text);
    }

    @Override
    public List<Album> findAllByArtistId(Long artistId) {
        return super.getEntityOrThrowException(artistId, artistRepository, Artist.class)
                .getAlbums()
                .stream()
                .sorted()
                .toList();
    }
}
