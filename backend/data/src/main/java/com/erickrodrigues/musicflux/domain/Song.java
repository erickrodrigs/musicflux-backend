package com.erickrodrigues.musicflux.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

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
    private List<Genre> genres = new ArrayList<>();

    public void play() {
        this.numberOfPlays += 1;
    }

    @Override
    public int compareTo(Song o) {
        final int comparison = o.getNumberOfPlays().compareTo(this.getNumberOfPlays());

        if (comparison == 0) {
            return this.getTitle().compareTo(o.getTitle());
        }

        return comparison;
    }
}
