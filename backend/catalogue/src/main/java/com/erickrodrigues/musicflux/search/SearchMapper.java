package com.erickrodrigues.musicflux.search;

import com.erickrodrigues.musicflux.track.TrackMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { TrackMapper.class })
public interface SearchMapper {

    SearchResultDto toSearchResultDto(SearchResult searchResult);
}
