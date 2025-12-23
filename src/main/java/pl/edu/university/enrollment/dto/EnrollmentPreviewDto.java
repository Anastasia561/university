package pl.edu.university.enrollment.dto;

import java.time.LocalDate;

public record EnrollmentPreviewDto(
        Integer id,
        String courseCode,
        String studentEmail,
        LocalDate enrollmentDate,
        Double finalGrade
) {
}
