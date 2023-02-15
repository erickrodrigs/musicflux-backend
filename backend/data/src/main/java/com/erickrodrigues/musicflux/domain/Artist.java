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
    private Set<Album> albums = new HashSet<>();

    @Builder
    public Artist(Long id, String name, String biography, Set<Album> albums) {
        super(id);
        this.name = name;
        this.biography = biography;
        this.albums = albums;
    }
}
