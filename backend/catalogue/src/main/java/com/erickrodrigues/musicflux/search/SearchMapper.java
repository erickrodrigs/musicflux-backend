package com.erickrodrigues.musicflux.search;

import com.erickrodrigues.musicflux.album.AlbumMapper;
import com.erickrodrigues.musicflux.artist.ArtistMapper;
import com.erickrodrigues.musicflux.track.TrackMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {
        ArtistMapper.class,
        AlbumMapper.class,
        TrackMapper.class,
})
public interface SearchMapper {

    SearchResultDto toSearchResultDto(SearchResult searchResult);
}
