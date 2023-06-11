package com.erickrodrigues.musicflux.search;

public interface SearchService {

    SearchResult findAllByTypeAndText(SearchableType type, String text);
}
