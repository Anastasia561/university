package pl.edu.backend.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.backend.auth.dto.AuthRequestDto;
import pl.edu.backend.auth.dto.AuthResponseDto;
import pl.edu.backend.auth.dto.RefreshTokenDto;
import pl.edu.backend.auth.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponseDto login(@Valid @RequestBody AuthRequestDto authRequestDto) {
        return authService.login(authRequestDto);
    }

    @PostMapping("/refresh")
    public AuthResponseDto refreshToken(@Valid @RequestBody RefreshTokenDto refreshTokenDto) {
        return authService.refresh(refreshTokenDto.refreshToken());
    }

    @PostMapping("/logout")
    public void logout(@Valid @RequestBody RefreshTokenDto refreshTokenDto) {
        authService.logout(refreshTokenDto.refreshToken());
    }
}
