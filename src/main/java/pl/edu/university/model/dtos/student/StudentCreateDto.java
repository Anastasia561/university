package pl.edu.university.model.dtos.student;

import jakarta.validation.constraints.*;
import lombok.*;
import pl.edu.university.validation.annotation.MinAge;
import pl.edu.university.validation.annotation.UniqueEmail;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentCreateDto {
    @NotBlank(message = "First name is required")
    @Size(min = 3, max = 50, message = "First name should contain between 3 and 50 symbols")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 3, max = 50, message = "Last name should contain between 3 and 50 symbols")
    private String lastName;

    @UniqueEmail
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @MinAge(18)
    @Past(message = "Birth date must be in the past")
    @NotNull(message = "Birth date is required")
    private LocalDate birthDate;
}
