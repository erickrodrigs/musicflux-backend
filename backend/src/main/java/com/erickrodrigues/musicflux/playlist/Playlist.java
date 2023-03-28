package com.erickrodrigues.musicflux.playlist;

import com.erickrodrigues.musicflux.track.Track;
import com.erickrodrigues.musicflux.user.User;
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
    @JoinTable(name = "playlists_tracks",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "track_id")
    )
    @Builder.Default
    private List<Track> tracks = new ArrayList<>();

    public void addTrack(Track track) {
        if (tracks.contains(track)) {
            throw new ResourceAlreadyExistsException("Track already included in the playlist");
        }

        tracks.add(track);
    }

    public void removeTrack(Track track) {
        if (!tracks.contains(track)) {
            throw new ResourceNotFoundException("Track is not included in the playlist");
        }

        tracks.remove(track);
    }
}
