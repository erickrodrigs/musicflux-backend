package com.erickrodrigues.musicflux.mappers;

import com.erickrodrigues.musicflux.domain.RecentlyPlayed;
import com.erickrodrigues.musicflux.dtos.RecentlyPlayedDetailsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SongMapper.class})
public interface RecentlyPlayedMapper {

    @Mapping(target = "profileId", source = "recentlyPlayed.profile.id")
    RecentlyPlayedDetailsDto toRecentlyPlayedDetailsDto(RecentlyPlayed recentlyPlayed);
}
