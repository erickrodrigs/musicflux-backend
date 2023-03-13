package com.erickrodrigues.musicflux.album;

import com.erickrodrigues.musicflux.artist.Artist;
import com.erickrodrigues.musicflux.shared.BaseEntity;
import com.erickrodrigues.musicflux.song.Song;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "albums")
public class Album extends BaseEntity implements Comparable<Album> {

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

    @Override
    public int compareTo(Album o) {
        if (Objects.isNull(this.getReleaseDate()) || Objects.isNull(o.getReleaseDate())) {
            return this.getId().compareTo(o.getId());
        }

        return this.getReleaseDate().compareTo(o.getReleaseDate());
    }
}
