package com.erickrodrigues.musicflux.favorite;

import com.erickrodrigues.musicflux.song.SongMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SongMapper.class})
public interface FavoriteMapper {

    @Mapping(target = "userId", source = "favorite.user.id")
    FavoriteDetailsDto toFavoriteDetailsDto(Favorite favorite);
}
