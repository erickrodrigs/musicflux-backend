package com.erickrodrigues.musicflux.catalogue;

import java.util.List;

public interface SearchService {

    SearchResult execute(List<SearchableType> types, String text);
}
