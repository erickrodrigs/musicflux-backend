package com.erickrodrigues.musicflux.catalogue;

import com.erickrodrigues.musicflux.album.AlbumMapper;
import com.erickrodrigues.musicflux.artist.ArtistMapper;
import com.erickrodrigues.musicflux.track.TrackMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {
        ArtistMapper.class,
        AlbumMapper.class,
        TrackMapper.class,
})
public interface CatalogueMapper {

    CatalogueResultDto toCatalogueResultDto(CatalogueResult catalogueResult);
}
