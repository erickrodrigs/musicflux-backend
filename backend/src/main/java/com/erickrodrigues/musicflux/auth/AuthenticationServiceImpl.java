package com.erickrodrigues.musicflux.auth;

import com.erickrodrigues.musicflux.user.User;
import com.erickrodrigues.musicflux.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public String register(String name, String username, String email, String password) {
        final User savedUser = userService.register(name, username, email, passwordEncoder.encode(password));
        final String jwtToken = jwtService.generateToken(savedUser);

        saveUserToken(savedUser, jwtToken);

        return jwtToken;
    }

    @Override
    public String authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        final User user = userService.findByUsername(username);
        final String jwtToken = jwtService.generateToken(user);

        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return jwtToken;
    }

    private void saveUserToken(User user, String jwtToken) {
        final Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        final List<Token> validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());

        if (validUserTokens.isEmpty()) return;

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenRepository.saveAll(validUserTokens);
    }
}
