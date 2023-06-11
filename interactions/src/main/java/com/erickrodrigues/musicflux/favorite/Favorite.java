package com.erickrodrigues.musicflux.favorite;

import com.erickrodrigues.musicflux.track.Track;
import com.erickrodrigues.musicflux.user.User;
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
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "favorites")
public class Favorite extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne
    @JoinColumn(name = "track_id")
    private Track track;
}
