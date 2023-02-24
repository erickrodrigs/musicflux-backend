package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.vo.SearchableType;
import com.erickrodrigues.musicflux.vo.SearchResult;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class SearchServiceImpl implements SearchService {

    private final ArtistService artistService;
    private final AlbumService albumService;
    private final SongService songService;
    private final PlaylistService playlistService;

    public SearchServiceImpl(ArtistService artistService,
                             AlbumService albumService,
                             SongService songService,
                             PlaylistService playlistService) {
        this.artistService = artistService;
        this.albumService = albumService;
        this.songService = songService;
        this.playlistService = playlistService;
    }

    @Override
    public SearchResult execute(Set<SearchableType> types, String text) {
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
