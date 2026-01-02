package pl.edu.backend.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.backend.auth.dto.AuthRequestDto;
import pl.edu.backend.auth.dto.AuthResponseDto;
import pl.edu.backend.auth.dto.TokenResponseDto;
import pl.edu.backend.auth.jwt.JwtProperties;
import pl.edu.backend.auth.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtProperties jwtProperties;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto authRequestDto) {
        TokenResponseDto dto = authService.login(authRequestDto);
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", dto.refreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/api/auth")
                .maxAge(jwtProperties.getRefreshTokenExpirationTimeSec())
                .sameSite("Lax")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(new AuthResponseDto(dto.accessToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refreshToken(@CookieValue(name = "refreshToken", required = false) String refreshToken) {
        TokenResponseDto dto = authService.refresh(refreshToken);
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", dto.refreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/api/auth")
                .maxAge(jwtProperties.getRefreshTokenExpirationTimeSec())
                .sameSite("Lax")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(new AuthResponseDto(dto.accessToken()));
    }

    @PostMapping("/logout")
    public void logout(@CookieValue(name = "refreshToken", required = false) String refreshToken) {
        authService.logout(refreshToken);
    }
}
