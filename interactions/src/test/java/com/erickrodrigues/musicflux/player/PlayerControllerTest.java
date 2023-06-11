package com.erickrodrigues.musicflux.player;

import com.erickrodrigues.musicflux.track.Track;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PlayerControllerTest {

    @Mock
    private PlayerService playerService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PlayerController playerController;

    @Test
    public void shouldPlayATrack() throws Exception {
        final long userId = 1L, trackId = 1L;
        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(playerController).build();
        when(playerService.play(userId, trackId)).thenReturn(Track.builder()
                .title("My track")
                .build()
        );
        when(restTemplate.getForObject(anyString(), any())).thenReturn(new byte[] { (byte)0xe0 });

        mockMvc.perform(get("/me/tracks/" + trackId + "/play")
                        .requestAttr("userId", userId))
                .andExpect(status().isOk());

        verify(playerService, times(1)).play(userId, trackId);
    }
}
