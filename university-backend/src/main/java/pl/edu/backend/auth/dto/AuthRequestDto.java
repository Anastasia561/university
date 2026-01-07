package pl.edu.backend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthRequestDto(
        @NotBlank(message = "Email ir required")
        @Email(message = "Email must be in valid format")
        String email,
        @NotBlank(message = "Password is required")
        String password) {
}
