package pl.edu.backend.exception.dto;

import java.util.Map;

public record ValidationErrorResponseDto(int status, String message, Map<String, String> fieldErrors) {
}
