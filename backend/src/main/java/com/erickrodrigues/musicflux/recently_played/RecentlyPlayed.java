package com.erickrodrigues.musicflux.recently_played;

import com.erickrodrigues.musicflux.song.Song;
import com.erickrodrigues.musicflux.profile.Profile;
import com.erickrodrigues.musicflux.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "recently_played_songs")
public class RecentlyPlayed extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "profile_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "song_id")
    private Song song;

    @CreationTimestamp
    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
