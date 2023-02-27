package com.erickrodrigues.musicflux.mappers;

import com.erickrodrigues.musicflux.domain.Profile;
import com.erickrodrigues.musicflux.dtos.ProfileDetailsDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    ProfileDetailsDto toProfileDetailsDto(Profile profile);
}
