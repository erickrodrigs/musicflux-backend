package com.erickrodrigues.musicflux.album;

import com.erickrodrigues.musicflux.artist.Artist;
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
