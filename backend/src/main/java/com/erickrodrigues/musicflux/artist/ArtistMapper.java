package com.erickrodrigues.musicflux.artist;

import com.erickrodrigues.musicflux.artist.Artist;
import com.erickrodrigues.musicflux.artist.ArtistDetailsDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArtistMapper {

    ArtistDetailsDto toArtistDetailsDto(Artist artist);
}
