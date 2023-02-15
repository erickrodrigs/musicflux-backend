package com.erickrodrigues.musicflux.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "songs")
public class Song extends BaseEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "length")
    private Duration length;

    @Column(name = "number_of_plays")
    private Long numberOfPlays;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    @ManyToMany
    @JoinTable(name = "songs_genres",
            joinColumns = @JoinColumn(name = "song_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    @Builder
    public Song(Long id, String title, Duration length, Long numberOfPlays, Album album, Set<Genre> genres) {
        super(id);
        this.title = title;
        this.length = length;
        this.numberOfPlays = numberOfPlays;
        this.album = album;
        this.genres = genres;
    }
}
