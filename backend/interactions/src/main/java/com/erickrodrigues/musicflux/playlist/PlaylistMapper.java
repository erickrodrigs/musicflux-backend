package com.erickrodrigues.musicflux.playlist;

import com.erickrodrigues.musicflux.track.TrackMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TrackMapper.class})
public interface PlaylistMapper {

    @Mapping(target = "userId", source = "playlist.user.id")
    PlaylistDto toPlaylistDto(Playlist playlist);

    @Mapping(target = "userId", source = "playlist.user.id")
    PlaylistDetailsDto toPlaylistDetailsDto(Playlist playlist);

    List<PlaylistDto> toListOfPlaylistDto(List<Playlist> playlists);
}
