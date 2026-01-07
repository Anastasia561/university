package pl.edu.backend.enrollment.dto;

public record StudentForEnrollmentDto(
        String firstName,
        String lastName,
        String email
) {
}
