package pl.edu.backend.user.dto;

public record UserProfileDto(
        String firstName,
        String lastName,
        String email
) {
}
