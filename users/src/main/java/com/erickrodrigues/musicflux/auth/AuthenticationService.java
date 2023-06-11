package com.erickrodrigues.musicflux.auth;

public interface AuthenticationService {

    String register(String name, String username, String email, String password);

    String authenticate(String username, String password);
}
