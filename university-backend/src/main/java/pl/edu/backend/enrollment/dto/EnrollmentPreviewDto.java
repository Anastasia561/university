package pl.edu.backend.enrollment.dto;

import java.time.LocalDate;
import java.util.UUID;

public record EnrollmentPreviewDto(
        UUID id,
        String courseCode,
        String studentEmail,
        LocalDate enrollmentDate,
        Double finalGrade
) {
}
