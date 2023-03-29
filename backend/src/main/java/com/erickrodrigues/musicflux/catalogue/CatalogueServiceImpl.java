package com.erickrodrigues.musicflux.catalogue;

import com.erickrodrigues.musicflux.album.Album;
import com.erickrodrigues.musicflux.album.AlbumService;
import com.erickrodrigues.musicflux.artist.Artist;
import com.erickrodrigues.musicflux.artist.ArtistService;
import com.erickrodrigues.musicflux.playlist.PlaylistService;
import com.erickrodrigues.musicflux.track.Track;
import com.erickrodrigues.musicflux.track.TrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CatalogueServiceImpl implements CatalogueService {

    private final ArtistService artistService;
    private final AlbumService albumService;
    private final TrackService trackService;
    private final PlaylistService playlistService;

    @Override
    public CatalogueResult findAllByTypeAndText(SearchableType type, String text) {
        final CatalogueResult catalogueResult = new CatalogueResult();

        switch (type) {
            case ARTIST -> catalogueResult.setArtists(artistService.findAllByNameContainingIgnoreCase(text));
            case ALBUM -> catalogueResult.setAlbums(albumService.findAllByTitleContainingIgnoreCase(text));
            case TRACK -> catalogueResult.setTracks(trackService.findAllByTitleContainingIgnoreCase(text));
            case PLAYLIST -> catalogueResult.setPlaylists(playlistService.findAllByNameContainingIgnoreCase(text));
            case GENRE -> {
                final List<Track> tracks = trackService.findAllByGenreName(text);
                final List<Album> albums = tracks
                        .stream()
                        .map(Track::getAlbum)
                        .collect(Collectors.toSet())
                        .stream()
                        .toList();
                final List<Artist> artists = new HashSet<>(albums
                        .stream()
                        .map(Album::getArtists)
                        .reduce(
                                new ArrayList<>(),
                                (acc, artistsList) -> Stream.concat(acc.stream(), artistsList.stream()).toList()
                        ))
                        .stream()
                        .toList();

                catalogueResult.setArtists(artists);
                catalogueResult.setAlbums(albums);
                catalogueResult.setTracks(tracks);
            }
        }

        return catalogueResult;
    }
}
