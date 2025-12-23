package pl.edu.university.student.dto;

import java.time.LocalDate;
import java.util.List;

public record StudentViewDto(
        String firstName,
        String lastName,
        String email,
        LocalDate birthdate,
        List<EnrollmentForStudentDto> enrollments
) {
}
