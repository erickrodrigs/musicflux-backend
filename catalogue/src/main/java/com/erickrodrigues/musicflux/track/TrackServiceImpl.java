package com.erickrodrigues.musicflux.track;

import com.erickrodrigues.musicflux.shared.ResourceNotFoundException;
import com.erickrodrigues.musicflux.shared.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackServiceImpl extends BaseService implements TrackService {

    private final TrackRepository trackRepository;

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

    @Transactional
    @Override
    public Track play(Long trackId) {
        final Track track = findById(trackId);
        track.play();
        return trackRepository.save(track);
    }
}
