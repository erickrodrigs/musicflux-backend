package com.erickrodrigues.musicflux.mappers;

import com.erickrodrigues.musicflux.domain.Album;
import com.erickrodrigues.musicflux.domain.Artist;
import com.erickrodrigues.musicflux.dtos.AlbumDetailsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AlbumMapper {

    @Mapping(target = "artistsIds", source = "album.artists")
    AlbumDetailsDto toAlbumDetailsDto(Album album);

    default Long artistToId(Artist artist) {
        return artist.getId();
    }
}
