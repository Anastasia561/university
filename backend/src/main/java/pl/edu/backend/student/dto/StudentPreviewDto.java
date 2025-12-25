package pl.edu.backend.student.dto;

import java.util.UUID;

public record StudentPreviewDto(
        UUID id,
        String firstName,
        String lastName,
        String email
) {
}
