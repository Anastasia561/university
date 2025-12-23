package pl.edu.university.student.dto;

import jakarta.validation.constraints.*;
import pl.edu.university.validation.annotation.MinAge;
import pl.edu.university.validation.annotation.UniqueEmail;

import java.time.LocalDate;

public record StudentCreateDto(
        @NotBlank(message = "First name is required")
        @Size(min = 3, max = 50, message = "First name should contain between 3 and 50 symbols")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(min = 3, max = 50, message = "Last name should contain between 3 and 50 symbols")
        String lastName,

        @UniqueEmail
        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,

        @MinAge(18)
        @Past(message = "Birth date must be in the past")
        @NotNull(message = "Birth date is required")
        LocalDate birthdate
) {
}
