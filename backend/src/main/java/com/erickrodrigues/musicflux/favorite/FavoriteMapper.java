package com.erickrodrigues.musicflux.favorite;

import com.erickrodrigues.musicflux.track.TrackMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TrackMapper.class})
public interface FavoriteMapper {

    @Mapping(target = "userId", source = "favorite.user.id")
    FavoriteDetailsDto toFavoriteDetailsDto(Favorite favorite);

    List<FavoriteDetailsDto> toListOfFavoriteDetailsDto(List<Favorite> favorites);
}
