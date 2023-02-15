package com.erickrodrigues.musicflux.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "profiles")
public class Profile extends BaseEntity {

    @OneToOne
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Playlist> playlists = new HashSet<>();

    @OneToMany(mappedBy = "profile")
    private Set<Favorite> favorites = new HashSet<>();

    @OneToMany(mappedBy = "profile")
    private Set<RecentlyListened> recentlyListenedSongs = new HashSet<>();

    @Builder
    public Profile(Long id,
                   User user,
                   Set<Playlist> playlists,
                   Set<Favorite> favorites,
                   Set<RecentlyListened> recentlyListenedSongs) {
        super(id);
        this.user = user;
        this.playlists = playlists;
        this.favorites = favorites;
        this.recentlyListenedSongs = recentlyListenedSongs;
    }
}
