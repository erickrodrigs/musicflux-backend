package com.erickrodrigues.musicflux.recently_played;

import com.erickrodrigues.musicflux.song.SongMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SongMapper.class})
public interface RecentlyPlayedMapper {

    @Mapping(target = "userId", source = "recentlyPlayed.user.id")
    RecentlyPlayedDetailsDto toRecentlyPlayedDetailsDto(RecentlyPlayed recentlyPlayed);
}
