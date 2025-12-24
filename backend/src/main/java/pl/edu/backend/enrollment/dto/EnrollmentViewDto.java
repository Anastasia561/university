package pl.edu.backend.enrollment.dto;

import java.time.LocalDate;

public record EnrollmentViewDto(
        StudentForEnrollmentDto student,
        CourseForEnrollmentDto course,
        LocalDate enrollmentDate,
        Double finalGrade
) {
}
