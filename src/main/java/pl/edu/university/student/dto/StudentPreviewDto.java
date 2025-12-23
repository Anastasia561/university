package pl.edu.university.student.dto;

public record StudentPreviewDto(
        Integer id,
        String firstName,
        String lastName,
        String email
) {
}
