package com.erickrodrigues.musicflux.mappers;

import com.erickrodrigues.musicflux.domain.Artist;
import com.erickrodrigues.musicflux.dtos.ArtistDetailsDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArtistMapper {

    ArtistDetailsDto toArtistDetailsDto(Artist artist);
}
