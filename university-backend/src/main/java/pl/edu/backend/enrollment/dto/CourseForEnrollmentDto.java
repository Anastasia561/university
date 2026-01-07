package pl.edu.backend.enrollment.dto;

public record CourseForEnrollmentDto(
        String code,
        String name,
        Integer credit,
        String description
) {
}
