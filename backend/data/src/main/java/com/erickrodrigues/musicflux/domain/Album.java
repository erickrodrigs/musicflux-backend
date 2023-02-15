package com.erickrodrigues.musicflux.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
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
    @Builder.Default
    private Set<Song> songs = new HashSet<>();

    @ManyToMany(mappedBy = "albums")
    @Builder.Default
    private Set<Artist> artists = new HashSet<>();
}
