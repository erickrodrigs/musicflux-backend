package com.erickrodrigues.musicflux.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @OneToOne
    private Profile profile;

    @Builder
    public User(Long id, String name, String username, String email, String password, Profile profile) {
        super(id);
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.profile = profile;
    }
}
