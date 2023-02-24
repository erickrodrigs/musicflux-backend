package com.erickrodrigues.musicflux.vo;

public enum SearchableType {
    ARTIST("artist"), ALBUM("album"), SONG("song"), PLAYLIST("playlist");

    private final String type;

    SearchableType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
