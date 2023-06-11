package com.erickrodrigues.musicflux.search;

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
public class SearchServiceImpl implements SearchService {

    private final ArtistService artistService;
    private final AlbumService albumService;
    private final TrackService trackService;
    private final PlaylistService playlistService;

    @Override
    public SearchResult findAllByTypeAndText(SearchableType type, String text) {
        final SearchResult searchResult = new SearchResult();

        switch (type) {
            case ARTIST -> searchResult.setArtists(artistService.findAllByNameContainingIgnoreCase(text));
            case ALBUM -> searchResult.setAlbums(albumService.findAllByTitleContainingIgnoreCase(text));
            case TRACK -> searchResult.setTracks(trackService.findAllByTitleContainingIgnoreCase(text));
            case PLAYLIST -> searchResult.setPlaylists(playlistService.findAllByNameContainingIgnoreCase(text));
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

                searchResult.setArtists(artists);
                searchResult.setAlbums(albums);
                searchResult.setTracks(tracks);
            }
        }

        return searchResult;
    }
}
