package com.erickrodrigues.musicflux.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "profiles")
public class Profile extends BaseEntity {

    @OneToOne
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "profile")
    @Builder.Default
    private Set<Playlist> playlists = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "profile")
    @Builder.Default
    private Set<Favorite> favorites = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "profile")
    @Builder.Default
    private Set<RecentlyListened> recentlyListenedSongs = new HashSet<>();

    public void addPlaylist(Playlist playlist) {
        playlists.add(playlist);
    }

    public void addRecentlyListenedSong(Song song) {
        recentlyListenedSongs.add(
                RecentlyListened.builder()
                        .profile(this)
                        .song(song)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }
}
