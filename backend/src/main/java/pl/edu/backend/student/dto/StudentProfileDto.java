package pl.edu.backend.student.dto;

import java.time.LocalDate;

public record StudentProfileDto(
        String firstName,
        String lastName,
        String email,
        LocalDate birthdate
) {
}
