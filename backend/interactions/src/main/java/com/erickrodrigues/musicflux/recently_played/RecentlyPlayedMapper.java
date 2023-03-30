package com.erickrodrigues.musicflux.recently_played;

import com.erickrodrigues.musicflux.track.TrackMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TrackMapper.class})
public interface RecentlyPlayedMapper {

    @Mapping(target = "userId", source = "recentlyPlayed.user.id")
    RecentlyPlayedDetailsDto toRecentlyPlayedDetailsDto(RecentlyPlayed recentlyPlayed);
}
