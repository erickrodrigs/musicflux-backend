package com.erickrodrigues.musicflux.catalogue;

import com.erickrodrigues.musicflux.album.AlbumService;
import com.erickrodrigues.musicflux.artist.ArtistService;
import com.erickrodrigues.musicflux.playlist.PlaylistService;
import com.erickrodrigues.musicflux.song.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final ArtistService artistService;
    private final AlbumService albumService;
    private final SongService songService;
    private final PlaylistService playlistService;

    @Override
    public SearchResult execute(List<SearchableType> types, String text) {
        final SearchResult searchResult = new SearchResult();

        types.forEach(type -> {
            switch (type) {
                case ARTIST -> searchResult.setArtists(artistService.findAllByNameContainingIgnoreCase(text));
                case ALBUM -> searchResult.setAlbums(albumService.findAllByTitleContainingIgnoreCase(text));
                case SONG -> searchResult.setSongs(songService.findAllByTitleContainingIgnoreCase(text));
                case PLAYLIST -> searchResult.setPlaylists(playlistService.findAllByNameContainingIgnoreCase(text));
            }
        });

        return searchResult;
    }
}
