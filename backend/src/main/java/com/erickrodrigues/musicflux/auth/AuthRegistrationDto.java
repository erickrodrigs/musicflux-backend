package com.erickrodrigues.musicflux.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRegistrationDto {

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Username is mandatory")
    private String username;

    @Email(message = "Email is not valid")
    private String email;

    @NotBlank(message = "Password is mandatory")
    private String password;
}
