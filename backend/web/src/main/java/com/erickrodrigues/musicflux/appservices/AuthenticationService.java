package com.erickrodrigues.musicflux.appservices;

public interface AuthenticationService {

    String register(String username, String email, String password);

    String authenticate(String username, String password);
}
