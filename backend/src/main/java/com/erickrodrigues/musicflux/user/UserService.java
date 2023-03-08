package com.erickrodrigues.musicflux.user;

public interface UserService {

    User findByUsername(String username);

    User register(String name, String username, String email, String password);
}
