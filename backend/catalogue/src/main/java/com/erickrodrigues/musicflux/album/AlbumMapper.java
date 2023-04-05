package com.erickrodrigues.musicflux.album;

import com.erickrodrigues.musicflux.artist.ArtistMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ArtistMapper.class})
public interface AlbumMapper {

    AlbumDetailsDto toAlbumDetailsDto(Album album);

    List<AlbumDetailsDto> toListOfAlbumDetailsDto(List<Album> albums);
}
