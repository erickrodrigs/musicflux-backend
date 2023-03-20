package com.erickrodrigues.musicflux.album;

import com.erickrodrigues.musicflux.shared.BaseService;
import com.erickrodrigues.musicflux.shared.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AlbumServiceImpl extends BaseService implements AlbumService {

    private final AlbumRepository albumRepository;

    @Override
    public List<Album> findAllByTitleContainingIgnoreCase(String text) {
        return albumRepository.findAllByTitleContainingIgnoreCase(text);
    }

    @Override
    public List<Album> findAllByArtistId(Long artistId) {
        final List<Album> albums = albumRepository.findAllByArtistsId(artistId);

        if (Objects.isNull(albums) || albums.isEmpty()) {
            throw new ResourceNotFoundException("Artist with that ID does not exist");
        }

        return albums
                .stream()
                .sorted()
                .toList();
    }
}
