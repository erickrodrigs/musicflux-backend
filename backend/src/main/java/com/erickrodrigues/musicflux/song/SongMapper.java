package com.erickrodrigues.musicflux.song;

import com.erickrodrigues.musicflux.genre.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Duration;
import java.util.List;

@Mapper(componentModel = "spring")
public interface SongMapper {

    @Mapping(target = "albumId", source = "song.album.id")
    SongDetailsDto toSongDetailsDto(Song song);

    List<SongDetailsDto> toListOfSongDetailsDto(List<Song> songs);

    default String genreToName(Genre genre) {
        return genre.getName();
    }

    default Long lengthToLong(Duration length) {
        return length.toSeconds();
    }
}
