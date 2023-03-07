package com.erickrodrigues.musicflux.profile;

public interface ProfileService {

    Profile findByUsername(String username);

    Profile register(String name, String username, String email, String password);
}
