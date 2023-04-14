package com.erickrodrigues.musicflux.track;

import com.erickrodrigues.musicflux.album.AlbumMapper;
import com.erickrodrigues.musicflux.genre.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Duration;
import java.util.List;

@Mapper(componentModel = "spring", uses = {AlbumMapper.class})
public interface TrackMapper {

    @Mapping(target = "artists", source = "track.album.artists")
    TrackDetailsDto toTrackDetailsDto(Track track);

    List<TrackDetailsDto> toListOfTrackDetailsDto(List<Track> tracks);

    default String genreToName(Genre genre) {
        return genre.getName();
    }

    default Long lengthToLong(Duration length) {
        return length.toSeconds();
    }
}
