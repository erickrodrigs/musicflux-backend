package com.erickrodrigues.musicflux.profile;

public interface ProfileService {

    Profile login(String username, String password);

    Profile signUp(String name, String username, String email, String password);
}
