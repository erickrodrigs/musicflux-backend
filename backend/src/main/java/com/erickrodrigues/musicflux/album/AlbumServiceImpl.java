package com.erickrodrigues.musicflux.album;

import com.erickrodrigues.musicflux.artist.Artist;
import com.erickrodrigues.musicflux.artist.ArtistRepository;
import com.erickrodrigues.musicflux.shared.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumServiceImpl extends BaseService implements AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;

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
