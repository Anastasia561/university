package pl.edu.backend.student.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import pl.edu.backend.validation.annotation.MinAge;

import java.time.LocalDate;

public record StudentUpdateDto(
        @NotBlank(message = "First name is required")
        @Size(min = 3, max = 50, message = "First name should contain between 3 and 50 symbols")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(min = 3, max = 50, message = "Last name should contain between 3 and 50 symbols")
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,

        @MinAge(18)
        @Past(message = "Birth date must be in the past")
        @NotNull(message = "Birth date is required")
        LocalDate birthdate
) {
}
