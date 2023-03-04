package com.erickrodrigues.musicflux.mappers;

import com.erickrodrigues.musicflux.domain.Favorite;
import com.erickrodrigues.musicflux.dtos.FavoriteDetailsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SongMapper.class})
public interface FavoriteMapper {

    @Mapping(target = "profileId", source = "favorite.profile.id")
    FavoriteDetailsDto toFavoriteDetailsDto(Favorite favorite);
}
