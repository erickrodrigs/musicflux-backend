package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.vo.SearchableType;
import com.erickrodrigues.musicflux.vo.SearchResult;

import java.util.Set;

public interface SearchService {

    SearchResult execute(Set<SearchableType> types, String text);
}
