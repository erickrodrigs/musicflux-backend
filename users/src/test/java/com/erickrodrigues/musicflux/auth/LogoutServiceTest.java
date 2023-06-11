package com.erickrodrigues.musicflux.auth;

import com.erickrodrigues.musicflux.token.Token;
import com.erickrodrigues.musicflux.token.TokenRepository;
import com.erickrodrigues.musicflux.token.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LogoutServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private LogoutService logoutService;

    @Test
    public void shouldInvalidateAccessToken() {
        // given
        final String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInN1YiI6ImVyaWNrcm9kcmlncyIsImlhdCI6MTY4MTE" +
                "4MTI3NiwiZXhwIjoxNjgxMTgyNzE2fQ._taCYXPHi8n4fmsA-kzil6gc4ZHy036Nh_D_k9vI4KE";
        final Token token = Token
                .builder()
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);
        when(tokenRepository.findByToken(jwtToken)).thenReturn(Optional.of(token));

        // when
        logoutService.logout(httpServletRequest, null, null);

        // then
        assertTrue(token.isExpired());
        assertTrue(token.isRevoked());
        verify(httpServletRequest, times(1)).getHeader("Authorization");
        verify(tokenRepository, times(1)).findByToken(jwtToken);
        verify(tokenRepository, times(1)).save(token);
    }
}
