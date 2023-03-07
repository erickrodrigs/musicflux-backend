package com.erickrodrigues.musicflux.favorite;

import com.erickrodrigues.musicflux.profile.Profile;
import com.erickrodrigues.musicflux.song.Song;
import com.erickrodrigues.musicflux.shared.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "favorites")
public class Favorite extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "profile_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "song_id")
    private Song song;
}
