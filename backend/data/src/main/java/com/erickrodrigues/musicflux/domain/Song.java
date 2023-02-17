package com.erickrodrigues.musicflux.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "songs")
public class Song extends BaseEntity implements Comparable<Song> {

    @Column(name = "title")
    private String title;

    @Column(name = "length")
    private Duration length;

    @Column(name = "number_of_plays")
    @Builder.Default
    private Long numberOfPlays = 0L;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    @ManyToMany
    @JoinTable(name = "songs_genres",
            joinColumns = @JoinColumn(name = "song_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @Builder.Default
    private Set<Genre> genres = new HashSet<>();

    public void play() {
        this.numberOfPlays += 1;
    }

    @Override
    public int compareTo(Song o) {
        return (int) (o.getNumberOfPlays() - this.getNumberOfPlays());
    }
}
