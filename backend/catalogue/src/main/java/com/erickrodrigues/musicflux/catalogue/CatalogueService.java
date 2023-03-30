package com.erickrodrigues.musicflux.catalogue;

public interface CatalogueService {

    CatalogueResult findAllByTypeAndText(SearchableType type, String text);
}
