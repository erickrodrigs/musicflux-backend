package com.erickrodrigues.musicflux.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailsDto {

    private Long id;

    private String name;

    private String username;

    private String email;
}
