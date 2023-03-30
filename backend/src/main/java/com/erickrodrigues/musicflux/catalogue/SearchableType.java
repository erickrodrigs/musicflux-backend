package com.erickrodrigues.musicflux.catalogue;

public enum SearchableType {
    ARTIST("artist"), ALBUM("album"), TRACK("track"), PLAYLIST("playlist"), GENRE("genre");

    private final String type;

    SearchableType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
