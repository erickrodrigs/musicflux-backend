package com.erickrodrigues.musicflux.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "genres")
public class Genre extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Builder
    public Genre(Long id, String name) {
        super(id);
        this.name = name;
    }
}
