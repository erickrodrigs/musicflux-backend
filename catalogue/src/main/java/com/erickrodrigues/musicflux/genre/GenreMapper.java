package com.erickrodrigues.musicflux.genre;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    List<GenreDto> toListOfGenreDto(List<Genre> genres);
}
