package pl.edu.university.enrollment.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record EnrollmentCreateDto(
        @NotBlank(message = "Course code is required")
        String courseCode,
        @Email(message = "Must be a valid email format")
        @NotBlank(message = "Student email is required")
        String studentEmail,
        @Future(message = "Enrollment date must be in the future")
        LocalDate enrollmentDate,
        @Min(value = 2, message = "Final grade can not be less than 2")
        @Max(value = 5, message = "Final grade can not be more than 5")
        Double finalGrade
) {
}
