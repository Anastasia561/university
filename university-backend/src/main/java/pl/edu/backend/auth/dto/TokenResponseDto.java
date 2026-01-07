package pl.edu.backend.auth.dto;

public record TokenResponseDto (String accessToken, String refreshToken) {
}
