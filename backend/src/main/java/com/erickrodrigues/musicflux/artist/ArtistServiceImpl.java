package com.erickrodrigues.musicflux.artist;

import com.erickrodrigues.musicflux.album.Album;
import com.erickrodrigues.musicflux.shared.BaseService;
import com.erickrodrigues.musicflux.track.Track;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistServiceImpl extends BaseService implements ArtistService {

    private final ArtistRepository artistRepository;

    @Override
    public List<Artist> findAllByNameContainingIgnoreCase(String text) {
        return artistRepository.findAllByNameContainingIgnoreCase(text);
    }

    @Override
    public List<Album> getArtistAlbums(Long artistId) {
        return super.getEntityOrThrowException(artistId, artistRepository, Artist.class)
                .getAlbums()
                .stream()
                .sorted()
                .toList();
    }

    @Override
    public List<Track> getTopTracks(Long artistId) {
        final Artist artist = super.getEntityOrThrowException(artistId, artistRepository, Artist.class);
        final List<Track> tracks = new ArrayList<>();

        artist.getAlbums().forEach((album) -> tracks.addAll(album.getTracks()));

        return tracks
                .stream()
                .sorted()
                .limit(5)
                .toList();
    }
}
