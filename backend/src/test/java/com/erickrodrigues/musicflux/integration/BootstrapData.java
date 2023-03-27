package com.erickrodrigues.musicflux.integration;

import com.erickrodrigues.musicflux.album.Album;
import com.erickrodrigues.musicflux.album.AlbumRepository;
import com.erickrodrigues.musicflux.artist.Artist;
import com.erickrodrigues.musicflux.artist.ArtistRepository;
import com.erickrodrigues.musicflux.favorite.Favorite;
import com.erickrodrigues.musicflux.favorite.FavoriteRepository;
import com.erickrodrigues.musicflux.genre.Genre;
import com.erickrodrigues.musicflux.genre.GenreRepository;
import com.erickrodrigues.musicflux.playlist.Playlist;
import com.erickrodrigues.musicflux.playlist.PlaylistRepository;
import com.erickrodrigues.musicflux.recently_played.RecentlyPlayed;
import com.erickrodrigues.musicflux.recently_played.RecentlyPlayedRepository;
import com.erickrodrigues.musicflux.song.Song;
import com.erickrodrigues.musicflux.song.SongRepository;
import com.erickrodrigues.musicflux.user.User;
import com.erickrodrigues.musicflux.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BootstrapData {

    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final SongRepository songRepository;
    private final GenreRepository genreRepository;
    private final PlaylistRepository playlistRepository;
    private final FavoriteRepository favoriteRepository;
    private final RecentlyPlayedRepository recentlyPlayedRepository;
    private final UserRepository userRepository;

    public void execute() {
        final List<Artist> artists = insertArtists();
        final List<Album> albums = insertAlbums(artists);
        final List<Genre> genres = insertGenres();
        final List<Song> songs = insertSongs(albums, genres);
        final List<User> users = insertUsers();
        insertPlaylists(users, songs);
        insertFavorites(users, songs);
        insertRecentlyPlayedSongs(users, songs);
    }

    private void insertRecentlyPlayedSongs(List<User> users, List<Song> songs) {
        recentlyPlayedRepository.save(RecentlyPlayed.builder()
                .user(users.get(0))
                .song(songs.get(0))
                .createdAt(LocalDateTime.now())
                .build()
        );
        recentlyPlayedRepository.save(RecentlyPlayed.builder()
                .user(users.get(0))
                .song(songs.get(1))
                .createdAt(LocalDateTime.now())
                .build()
        );
    }

    private void insertFavorites(List<User> users, List<Song> songs) {
        favoriteRepository.save(Favorite.builder()
                .user(users.get(0))
                .song(songs.get(1))
                .build()
        );
    }

    private void insertPlaylists(List<User> users, List<Song> songs) {
        playlistRepository.save(Playlist.builder()
                .name("my favorites dm songs of all time")
                .user(users.get(0))
                .songs(List.of(
                        songs.get(2),
                        songs.get(3),
                        songs.get(4),
                        songs.get(5),
                        songs.get(6)
                ))
                .build()
        );
    }

    private List<User> insertUsers() {
        final User user = userRepository.save(User.builder()
                .name("Erick")
                .username("erickrodrigs")
                .email("erick@erick.com")
                .password("$2a$10$Wmnt48WgTfT5r9.wKIxkn.gB.jV.4Qa..BqmJHvfyeKukfEYnraQC")
                .build()
        );

        return List.of(user);
    }

    private List<Song> insertSongs(List<Album> albums, List<Genre> genres) {
        final Song song1 = songRepository.save(Song.builder()
                .title("Black Celebration")
                .length(Duration.ofSeconds(295))
                .numberOfPlays(6975821L)
                .album(albums.get(0))
                .genres(genres)
                .build()
        );
        final Song song2 = songRepository.save(Song.builder()
                .title("Fly On The Windscreen (Final)")
                .length(Duration.ofSeconds(318))
                .numberOfPlays(6975822L)
                .album(albums.get(0))
                .genres(genres)
                .build()
        );
        final Song song3 = songRepository.save(Song.builder()
                .title("A Question Of Lust")
                .length(Duration.ofSeconds(260))
                .numberOfPlays(6975823L)
                .album(albums.get(0))
                .genres(genres)
                .build()
        );
        final Song song4 = songRepository.save(Song.builder()
                .title("Sometimes")
                .length(Duration.ofSeconds(113))
                .numberOfPlays(6975824L)
                .album(albums.get(0))
                .genres(genres)
                .build()
        );
        final Song song5 = songRepository.save(Song.builder()
                .title("It Doesn't Matter Two")
                .length(Duration.ofSeconds(170))
                .numberOfPlays(6975825L)
                .album(albums.get(0))
                .genres(genres)
                .build()
        );
        final Song song6 = songRepository.save(Song.builder()
                .title("World Full Of Nothing")
                .length(Duration.ofSeconds(170))
                .numberOfPlays(6975826L)
                .album(albums.get(0))
                .genres(genres)
                .build()
        );
        final Song song7 = songRepository.save(Song.builder()
                .title("The Things You Said")
                .length(Duration.ofSeconds(242))
                .numberOfPlays(6975827L)
                .album(albums.get(1))
                .genres(genres)
                .build()
        );

        return List.of(song1, song2, song3, song4, song5, song6, song7);
    }

    private List<Genre> insertGenres() {
        final Genre genre = genreRepository.save(Genre.builder().name("Synth-pop").build());

        return List.of(genre);
    }

    private List<Album> insertAlbums(List<Artist> artists) {
        final Album album1 = albumRepository.save(Album
                .builder()
                .title("Black Celebration")
                .coverUrl("https://i.scdn.co/image/ab67616d0000b273878ae6fc96ee5954168838fc")
                .releaseDate(LocalDate.parse("1986-03-17"))
                .artists(artists)
                .build()
        );
        final Album album2 = albumRepository.save(Album
                .builder()
                .title("Music For The Masses")
                .coverUrl("https://i.scdn.co/image/ab67616d00001e025c2d57aa294b71136bcc510d")
                .releaseDate(LocalDate.parse("1987-09-28"))
                .artists(artists)
                .build()
        );

        artists.get(0).setAlbums(List.of(album1, album2));

        artistRepository.save(artists.get(0));

        return List.of(album1, album2);
    }

    private List<Artist> insertArtists() {
        final Artist artist = artistRepository.save(Artist.builder()
                .name("Depeche Mode")
                .biography("The best band in the world")
                .build()
        );

        return List.of(artist);
    }
}
