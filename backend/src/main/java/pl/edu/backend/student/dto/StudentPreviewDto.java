package pl.edu.backend.student.dto;

public record StudentPreviewDto(
        Integer id,
        String firstName,
        String lastName,
        String email
) {
}
