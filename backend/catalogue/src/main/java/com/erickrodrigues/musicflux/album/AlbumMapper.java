package com.erickrodrigues.musicflux.album;

import com.erickrodrigues.musicflux.artist.Artist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AlbumMapper {

    @Mapping(target = "artistsIds", source = "album.artists")
    AlbumDetailsDto toAlbumDetailsDto(Album album);

    List<AlbumDetailsDto> toListOfAlbumDetailsDto(List<Album> albums);

    default Long artistToId(Artist artist) {
        return artist.getId();
    }
}
