package com.erickrodrigues.musicflux.auth;

import com.erickrodrigues.musicflux.profile.Profile;
import com.erickrodrigues.musicflux.profile.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final ProfileRepository profileRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public String register(String name, String username, String email, String password) {
        var profile = Profile.builder()
                .name(name)
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
        var savedProfile = profileRepository.save(profile);
        var jwtToken = jwtService.generateToken(savedProfile);
        saveProfileToken(profile, jwtToken);

        return jwtToken;
    }

    @Override
    public String authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        var profile = profileRepository.findByUsername(username)
                .orElseThrow();
        var jwtToken = jwtService.generateToken(profile);
        revokeAllProfileTokens(profile);
        saveProfileToken(profile, jwtToken);

        return jwtToken;
    }

    private void saveProfileToken(Profile profile, String jwtToken) {
        var token = Token.builder()
                .profile(profile)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);
    }

    private void revokeAllProfileTokens(Profile profile) {
        var validProfileTokens = tokenRepository.findAllValidTokensByProfile(profile.getId());
        if (validProfileTokens.isEmpty()) return;

        validProfileTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenRepository.saveAll(validProfileTokens);
    }
}
