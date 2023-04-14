package com.erickrodrigues.musicflux.recently_played;

import com.erickrodrigues.musicflux.artist.Artist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RecentlyPlayedMapper {

    @Mapping(target = "trackTitle", source = "recentlyPlayed.track.title")
    @Mapping(target = "albumTitle", source = "recentlyPlayed.track.album.title")
    @Mapping(target = "artistsNames", source = "recentlyPlayed.track.album.artists")
    RecentlyPlayedDetailsDto toRecentlyPlayedDetailsDto(RecentlyPlayed recentlyPlayed);

    default String artistToName(Artist artist) {
        return artist.getName();
    }
}
