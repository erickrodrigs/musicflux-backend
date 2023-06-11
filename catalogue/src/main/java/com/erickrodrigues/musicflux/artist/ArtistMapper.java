package com.erickrodrigues.musicflux.artist;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArtistMapper {

    ArtistDto toArtistDto(Artist artist);

    ArtistDetailsDto toArtistDetailsDto(Artist artist);

    List<ArtistDetailsDto> toListOfArtistDetailsDto(List<Artist> artists);
}
