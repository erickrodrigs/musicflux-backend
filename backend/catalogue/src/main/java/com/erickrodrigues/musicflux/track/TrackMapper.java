package com.erickrodrigues.musicflux.track;

import com.erickrodrigues.musicflux.album.AlbumMapper;
import com.erickrodrigues.musicflux.genre.Genre;
import org.mapstruct.Mapper;

import java.time.Duration;
import java.util.List;

@Mapper(componentModel = "spring", uses = {AlbumMapper.class})
public interface TrackMapper {

    TrackDto toTrackDetailsDto(Track track);

    List<TrackDto> toListOfTrackDetailsDto(List<Track> tracks);

    default String genreToName(Genre genre) {
        return genre.getName();
    }

    default Long lengthToLong(Duration length) {
        return length.toSeconds();
    }
}
