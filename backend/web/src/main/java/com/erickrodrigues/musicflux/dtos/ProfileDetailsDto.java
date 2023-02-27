package com.erickrodrigues.musicflux.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileDetailsDto {

    private Long id;

    private String name;

    private String username;

    private String email;
}
