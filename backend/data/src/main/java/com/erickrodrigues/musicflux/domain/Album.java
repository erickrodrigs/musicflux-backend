package com.erickrodrigues.musicflux.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "album")
    @Builder.Default
    private List<Song> songs = new ArrayList<>();

    @ManyToMany(mappedBy = "albums")
    @Builder.Default
    private List<Artist> artists = new ArrayList<>();
}
