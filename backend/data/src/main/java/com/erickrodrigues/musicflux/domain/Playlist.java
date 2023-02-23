package com.erickrodrigues.musicflux.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "playlists")
public class Playlist extends BaseEntity {

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Profile profile;

    @ManyToMany
    @JoinTable(name = "playlists_songs",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    @Builder.Default
    private Set<Song> songs = new HashSet<>();

    public void addSong(Song song) {
        if (songs.contains(song)) {
            throw new RuntimeException("Song already included in the playlist");
        }

        songs.add(song);
    }

    public void removeSong(Song song) {
        if (!songs.contains(song)) {
            throw new RuntimeException("Song is not included in the playlist");
        }

        songs.remove(song);
    }
}
