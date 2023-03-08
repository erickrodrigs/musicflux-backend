package com.erickrodrigues.musicflux.playlist;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlaylistMapper {

    @Mapping(target = "userId", source = "playlist.user.id")
    PlaylistDetailsDto toPlaylistDetailsDto(Playlist playlist);
}
