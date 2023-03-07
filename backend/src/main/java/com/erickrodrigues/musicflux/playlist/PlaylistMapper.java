package com.erickrodrigues.musicflux.playlist;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlaylistMapper {

    @Mapping(target = "profileId", source = "playlist.profile.id")
    PlaylistDetailsDto toPlaylistDetailsDto(Playlist playlist);
}
