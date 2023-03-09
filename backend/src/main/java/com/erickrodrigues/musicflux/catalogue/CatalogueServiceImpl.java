package com.erickrodrigues.musicflux.catalogue;

import com.erickrodrigues.musicflux.album.Album;
import com.erickrodrigues.musicflux.album.AlbumService;
import com.erickrodrigues.musicflux.artist.Artist;
import com.erickrodrigues.musicflux.artist.ArtistService;
import com.erickrodrigues.musicflux.playlist.PlaylistService;
import com.erickrodrigues.musicflux.song.Song;
import com.erickrodrigues.musicflux.song.SongService;
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
    private final SongService songService;
    private final PlaylistService playlistService;

    @Override
    public CatalogueResult findAllByTypesAndText(List<SearchableType> types, String text) {
        final CatalogueResult catalogueResult = new CatalogueResult();

        types.forEach(type -> {
            switch (type) {
                case ARTIST -> catalogueResult.setArtists(artistService.findAllByNameContainingIgnoreCase(text));
                case ALBUM -> catalogueResult.setAlbums(albumService.findAllByTitleContainingIgnoreCase(text));
                case SONG -> catalogueResult.setSongs(songService.findAllByTitleContainingIgnoreCase(text));
                case PLAYLIST -> catalogueResult.setPlaylists(playlistService.findAllByNameContainingIgnoreCase(text));
            }
        });

        return catalogueResult;
    }

    @Override
    public CatalogueResult findAllByGenreName(String genreName) {
        final List<Song> songs = songService.findAllByGenreName(genreName);
        final List<Album> albums = songs
                .stream()
                .map(Song::getAlbum)
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

        return CatalogueResult
                .builder()
                .artists(artists)
                .albums(albums)
                .songs(songs)
                .build();
    }
}
