package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Profile;

public interface ProfileService {

    Profile login(String username, String password);

    Profile signUp(String name, String username, String email, String password);
}
