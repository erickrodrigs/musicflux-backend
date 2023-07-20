package com.erickrodrigues.musicflux.album;

import com.erickrodrigues.musicflux.shared.BaseService;
import com.erickrodrigues.musicflux.track.Track;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumServiceImpl extends BaseService implements AlbumService {

    private final AlbumRepository albumRepository;

    @Override
    public Album findById(Long albumId) {
        return super.getEntityOrThrowException(albumId, albumRepository, Album.class);
    }

    @Override
    public List<Album> findAllByTitleContainingIgnoreCase(String text) {
        return albumRepository.findAllByTitleContainingIgnoreCase(text);
    }

    @Override
    public List<Track> getAlbumTracks(Long albumId) {
        return super.getEntityOrThrowException(albumId, albumRepository, Album.class).getTracks();
    }
}
