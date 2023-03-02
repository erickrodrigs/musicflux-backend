package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.vo.SearchableType;
import com.erickrodrigues.musicflux.vo.SearchResult;

import java.util.List;

public interface SearchService {

    SearchResult execute(List<SearchableType> types, String text);
}
