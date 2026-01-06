package pl.edu.backend.user.dto;

import jakarta.validation.constraints.NotBlank;
import pl.edu.backend.validation.annotation.Password;

public record PasswordDto(
        @Password
        @NotBlank(message = "Password is required")
        String password,

        @Password
        @NotBlank(message = "Password is required")
        String repeatPassword
) {
}
