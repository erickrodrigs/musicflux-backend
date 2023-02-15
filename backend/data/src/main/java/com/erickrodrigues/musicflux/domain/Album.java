package com.erickrodrigues.musicflux.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "albums")
public class Album extends BaseEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "cover_url")
    private String coverUrl;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Song> songs = new HashSet<>();

    @ManyToMany(mappedBy = "albums")
    private Set<Artist> artists = new HashSet<>();

    @Builder
    public Album(Long id, String title, String coverUrl, LocalDate releaseDate, Set<Song> songs, Set<Artist> artists) {
        super(id);
        this.title = title;
        this.coverUrl = coverUrl;
        this.releaseDate = releaseDate;
        this.songs = songs;
        this.artists = artists;
    }
}
