package com.erickrodrigues.musicflux.user;

import com.erickrodrigues.musicflux.shared.NullOrNotBlank;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserDto {

    @NullOrNotBlank(message = "Name must not be blank")
    private String name;

    @NullOrNotBlank(message = "Username must not be blank")
    private String username;

    @Email(message = "Email is not valid")
    private String email;

    @NullOrNotBlank(message = "Password must not be blank")
    private String password;
}
