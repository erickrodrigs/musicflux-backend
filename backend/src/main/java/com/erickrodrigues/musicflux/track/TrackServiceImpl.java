package com.erickrodrigues.musicflux.track;

import com.erickrodrigues.musicflux.recently_played.RecentlyPlayedService;
import com.erickrodrigues.musicflux.shared.ResourceNotFoundException;
import com.erickrodrigues.musicflux.user.User;
import com.erickrodrigues.musicflux.shared.BaseService;
import com.erickrodrigues.musicflux.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrackServiceImpl extends BaseService implements TrackService {

    private final TrackRepository trackRepository;
    private final UserService userService;
    private final RecentlyPlayedService recentlyPlayedService;

    @Transactional
    @Override
    public Track play(Long userId, Long trackId) {
        final User user = userService.findById(userId);
        Track track = findById(trackId);

        track.play();
        track = trackRepository.save(track);
        recentlyPlayedService.save(track, user);

        return track;
    }

    @Override
    public Track findById(Long trackId) {
        return trackRepository
                .findById(trackId)
                .orElseThrow(() -> new ResourceNotFoundException("Track with that ID does not exist"));
    }

    @Override
    public List<Track> findAllByTitleContainingIgnoreCase(String text) {
        return trackRepository.findAllByTitleContainingIgnoreCase(text);
    }

    @Override
    public List<Track> findAllByGenreName(String genreName) {
        return trackRepository.findAllByGenresNameIgnoreCase(genreName);
    }

    @Override
    public List<Track> findAllByAlbumId(Long albumId) {
        final List<Track> tracks = trackRepository.findAllByAlbumId(albumId);

        if (tracks.isEmpty()) throw new ResourceNotFoundException("Album with that ID does not exist");

        return tracks;
    }

    @Override
    public List<Track> findMostPlayedTracksByArtistId(Long artistId) {
        return trackRepository
                .findAllByAlbumArtistsId(artistId)
                .stream()
                .sorted()
                .limit(5)
                .collect(Collectors.toList());
    }
}
