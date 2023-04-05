package com.erickrodrigues.musicflux.user;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDetailsDto toUserDetailsDto(User user);
}
