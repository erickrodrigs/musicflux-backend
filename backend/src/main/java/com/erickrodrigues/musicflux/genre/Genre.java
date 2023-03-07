package com.erickrodrigues.musicflux.genre;

import com.erickrodrigues.musicflux.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "genres")
public class Genre extends BaseEntity {

    @Column(name = "name")
    private String name;
}
