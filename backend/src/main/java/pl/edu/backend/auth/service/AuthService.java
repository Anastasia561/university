package pl.edu.backend.auth.service;

import pl.edu.backend.auth.dto.AuthRequestDto;
import pl.edu.backend.auth.dto.AuthResponseDto;

public interface AuthService {
    AuthResponseDto login(AuthRequestDto request);

    AuthResponseDto refresh(String refreshToken);

    void logout(String refreshToken);
}
