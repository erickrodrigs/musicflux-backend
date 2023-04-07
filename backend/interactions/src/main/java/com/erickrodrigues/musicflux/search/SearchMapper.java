package com.erickrodrigues.musicflux.search;

import com.erickrodrigues.musicflux.playlist.PlaylistMapper;
import com.erickrodrigues.musicflux.track.TrackMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { TrackMapper.class, PlaylistMapper.class })
public interface SearchMapper {

    SearchResultDto toSearchResultDto(SearchResult searchResult);
}
