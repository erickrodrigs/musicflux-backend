package com.erickrodrigues.musicflux.auth;

import com.erickrodrigues.musicflux.shared.BaseEntity;
import com.erickrodrigues.musicflux.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "tokens")
public class Token extends BaseEntity {

    @Column
    private String token;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TokenType tokenType = TokenType.BEARER;

    private boolean revoked;

    private boolean expired;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
