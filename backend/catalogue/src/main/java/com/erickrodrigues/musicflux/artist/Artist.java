package com.erickrodrigues.musicflux.artist;

import com.erickrodrigues.musicflux.album.Album;
import com.erickrodrigues.musicflux.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "artists")
public class Artist extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "biography")
    private String biography;

    @Column(name = "photo_url")
    private String photoUrl;

    @ManyToMany
    @JoinTable(name = "artists_albums",
            joinColumns = @JoinColumn(name = "artist_id"),
            inverseJoinColumns = @JoinColumn(name = "album_id")
    )
    @Builder.Default
    private List<Album> albums = new ArrayList<>();
}
