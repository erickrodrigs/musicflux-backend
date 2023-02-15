package com.erickrodrigues.musicflux.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "artists")
public class Artist extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "biography")
    private String biography;

    @ManyToMany
    @JoinTable(name = "artists_albums",
            joinColumns = @JoinColumn(name = "artist_id"),
            inverseJoinColumns = @JoinColumn(name = "album_id")
    )
    @Builder.Default
    private Set<Album> albums = new HashSet<>();
}
