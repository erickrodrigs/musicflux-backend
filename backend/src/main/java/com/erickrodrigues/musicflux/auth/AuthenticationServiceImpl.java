package com.erickrodrigues.musicflux.auth;

import com.erickrodrigues.musicflux.profile.Profile;
import com.erickrodrigues.musicflux.profile.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final ProfileService profileService;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public String register(String name, String username, String email, String password) {
        final Profile savedProfile = profileService.register(name, username, email, passwordEncoder.encode(password));
        final String jwtToken = jwtService.generateToken(savedProfile);

        saveProfileToken(savedProfile, jwtToken);

        return jwtToken;
    }

    @Override
    public String authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        final Profile profile = profileService.findByUsername(username);
        final String jwtToken = jwtService.generateToken(profile);

        revokeAllProfileTokens(profile);
        saveProfileToken(profile, jwtToken);

        return jwtToken;
    }

    private void saveProfileToken(Profile profile, String jwtToken) {
        final Token token = Token.builder()
                .profile(profile)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);
    }

    private void revokeAllProfileTokens(Profile profile) {
        final List<Token> validProfileTokens = tokenRepository.findAllValidTokensByProfile(profile.getId());

        if (validProfileTokens.isEmpty()) return;

        validProfileTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenRepository.saveAll(validProfileTokens);
    }
}
