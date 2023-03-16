package com.erickrodrigues.musicflux.playlist;

import com.erickrodrigues.musicflux.user.User;
import com.erickrodrigues.musicflux.song.Song;
import com.erickrodrigues.musicflux.shared.ResourceAlreadyExistsException;
import com.erickrodrigues.musicflux.shared.ResourceNotFoundException;
import com.erickrodrigues.musicflux.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "playlists")
public class Playlist extends BaseEntity {

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToMany
    @JoinTable(name = "playlists_songs",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    @Builder.Default
    private List<Song> songs = new ArrayList<>();

    public void addSong(Song song) {
        if (songs.contains(song)) {
            throw new ResourceAlreadyExistsException("Song already included in the playlist");
        }

        songs.add(song);
    }

    public void removeSong(Song song) {
        if (!songs.contains(song)) {
            throw new ResourceNotFoundException("Song is not included in the playlist");
        }

        songs.remove(song);
    }
}
