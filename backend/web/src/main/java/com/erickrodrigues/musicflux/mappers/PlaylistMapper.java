package com.erickrodrigues.musicflux.mappers;

import com.erickrodrigues.musicflux.domain.Playlist;
import com.erickrodrigues.musicflux.dtos.PlaylistDetailsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlaylistMapper {

    @Mapping(target = "profileId", source = "playlist.profile.id")
    PlaylistDetailsDto toPlaylistDetailsDto(Playlist playlist);
}
