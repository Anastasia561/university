package pl.edu.university.enrollment.dto;

public record CourseForEnrollmentDto(
        String code,
        String name,
        Integer credit,
        String description
) {
}
