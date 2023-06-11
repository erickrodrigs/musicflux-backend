package com.erickrodrigues.musicflux.track;

import java.util.List;

public interface TrackService {

    Track findById(Long trackId);

    List<Track> findAllByTitleContainingIgnoreCase(String text);

    List<Track> findAllByGenreName(String genreName);

    Track play(Long trackId);
}
