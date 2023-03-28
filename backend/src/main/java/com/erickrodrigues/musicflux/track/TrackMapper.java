package com.erickrodrigues.musicflux.track;

import com.erickrodrigues.musicflux.genre.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Duration;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TrackMapper {

    @Mapping(target = "albumId", source = "track.album.id")
    TrackDto toTrackDetailsDto(Track track);

    List<TrackDto> toListOfTrackDetailsDto(List<Track> tracks);

    default String genreToName(Genre genre) {
        return genre.getName();
    }

    default Long lengthToLong(Duration length) {
        return length.toSeconds();
    }
}
