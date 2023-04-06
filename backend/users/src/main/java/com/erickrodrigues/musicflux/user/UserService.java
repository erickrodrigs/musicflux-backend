package com.erickrodrigues.musicflux.user;

import java.util.Map;

public interface UserService {

    User findById(Long id);

    User findByUsername(String username);

    User register(String name, String username, String email, String password);

    User update(Long id, Map<String, Object> updates);
}
