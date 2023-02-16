package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Profile;
import com.erickrodrigues.musicflux.domain.User;

public interface UserService {

    Profile login(String username, String password);

    User signUp(User user);
}