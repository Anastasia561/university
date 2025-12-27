package pl.edu.backend.auth.service;

import pl.edu.backend.auth.dto.AuthRequestDto;
import pl.edu.backend.auth.dto.TokenResponseDto;

public interface AuthService {
    TokenResponseDto login(AuthRequestDto request);

    TokenResponseDto refresh(String refreshToken);

    void logout(String refreshToken);
}
