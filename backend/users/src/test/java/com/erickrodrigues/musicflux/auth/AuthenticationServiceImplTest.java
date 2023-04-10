package com.erickrodrigues.musicflux.auth;

import com.erickrodrigues.musicflux.shared.ResourceAlreadyExistsException;
import com.erickrodrigues.musicflux.token.Token;
import com.erickrodrigues.musicflux.token.TokenRepository;
import com.erickrodrigues.musicflux.token.TokenType;
import com.erickrodrigues.musicflux.user.User;
import com.erickrodrigues.musicflux.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {

    private static final String WRONG_JWT_TOKEN = "Wrong jwt token";
    private static final String TOKENS_NOT_REVOKED_NOR_EXPIRED = "There are tokens not revoked nor expired";

    @Mock
    private UserService userService;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    public void shouldRegisterANewUserWithAccessToken() {
        // given
        final Long userId = 1L;
        final String name = "Erick", username = "erick123", email = "erick@erick.com", password = "erick123";
        final String jwtToken = "my-token";
        final User user = User
                .builder()
                .id(userId)
                .name(name)
                .username(username)
                .email(email)
                .password(password)
                .build();
        final Token token = createToken(jwtToken, user);
        final Map<String, Object> extraClaims = Map.of("userId", user.getId());
        when(userService.register(name, username, email, password)).thenReturn(user);
        when(jwtService.generateToken(extraClaims, user)).thenReturn(jwtToken);

        // when
        final String actualToken = authenticationService.register(name, username, email, password);

        // then
        assertEquals(jwtToken, actualToken, WRONG_JWT_TOKEN);
        verify(userService, times(1)).register(name, username, email, password);
        verify(jwtService, times(1)).generateToken(extraClaims, user);
        verify(tokenRepository, times(1)).save(eq(token));
    }

    @Test
    public void shouldThrowAnExceptionWhenRegisteringANewUserWithInvalidInfo() {
        // given
        final String name = "Erick", username = "erick123", email = "erick@erick.com", password = "erick123";
        when(userService.register(name, username, email, password)).thenThrow(ResourceAlreadyExistsException.class);

        // then
        assertThrows(ResourceAlreadyExistsException.class, () ->
                authenticationService.register(name, username, email, password)
        );
        verify(jwtService, never()).generateToken(anyMap(), any());
        verify(tokenRepository, never()).save(any());
    }

    private Token createToken(final String token, final User user) {
        return Token
                .builder()
                .user(user)
                .token(token)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
    }
}
