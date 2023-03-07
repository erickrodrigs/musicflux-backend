package com.erickrodrigues.musicflux.profile;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    ProfileDetailsDto toProfileDetailsDto(Profile profile);
}
