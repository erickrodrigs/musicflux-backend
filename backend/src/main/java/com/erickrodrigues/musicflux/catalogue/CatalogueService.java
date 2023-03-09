package com.erickrodrigues.musicflux.catalogue;

import java.util.List;

public interface CatalogueService {

    CatalogueResult findAllByTypesAndText(List<SearchableType> types, String text);

    CatalogueResult findAllByGenreName(String genreName);
}
