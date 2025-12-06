package pl.edu.university.model.dtos.enrollment;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class EnrollmentCreateDto {
    @NotBlank(message = "Course code is required")
    private String courseCode;
    @Email(message = "Must be a valid email format")
    @NotBlank(message = "Student email is required")
    private String studentEmail;
    @Future(message = "Enrollment date must be in the future")
    private LocalDate enrollmentDate;
    @Min(value = 2, message = "Final grade can not be less than 2")
    @Max(value = 5, message = "Final grade can not be more than 5")
    private Double finalGrade;
}
