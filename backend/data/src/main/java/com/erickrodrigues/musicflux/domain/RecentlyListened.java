package com.erickrodrigues.musicflux.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "recently_listened_songs")
public class RecentlyListened extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "song_id")
    private Song song;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
