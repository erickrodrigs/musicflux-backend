package com.erickrodrigues.musicflux.track;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TrackControllerTest {

    @Mock
    private TrackService trackService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TrackController trackController;

    @Test
    public void shouldPlayATrack() throws Exception {
        final long userId = 1L, trackId = 1L;
        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(trackController).build();
        when(trackService.play(userId, trackId)).thenReturn(Track.builder()
                .title("My track")
                .build()
        );
        when(restTemplate.getForObject(anyString(), any())).thenReturn(new byte[] { (byte)0xe0 });

        mockMvc.perform(get("/users/me/tracks/" + trackId)
                        .requestAttr("userId", userId))
                .andExpect(status().isOk());

        verify(trackService, times(1)).play(anyLong(), anyLong());
    }
}
