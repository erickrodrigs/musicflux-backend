package com.erickrodrigues.musicflux.recently_played;

import com.erickrodrigues.musicflux.track.TrackMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TrackMapper.class})
public interface RecentlyPlayedMapper {

    @Mapping(target = "track", source = "recentlyPlayed.track")
    @Mapping(target = "album", source = "recentlyPlayed.track.album")
    @Mapping(target = "artists", source = "recentlyPlayed.track.album.artists")
    RecentlyPlayedDetailsDto toRecentlyPlayedDetailsDto(RecentlyPlayed recentlyPlayed);
}
