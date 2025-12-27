package pl.edu.backend.auth.service;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import pl.edu.backend.auth.core.CustomUserDetails;
import pl.edu.backend.auth.dto.AuthRequestDto;
import pl.edu.backend.auth.dto.TokenResponseDto;
import pl.edu.backend.auth.jwt.service.JwtService;
import pl.edu.backend.auth.refreshtoken.service.RefreshTokenService;
import pl.edu.backend.exception.InvalidRefreshTokenException;
import pl.edu.backend.user.model.User;
import pl.edu.backend.user.service.UserService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public TokenResponseDto login(AuthRequestDto request) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

            CustomUserDetails customUser = (CustomUserDetails) authentication.getPrincipal();

            String accessToken = jwtService.generateAccessToken(customUser);
            String refreshToken = jwtService.generateRefreshToken(customUser);

            User user = userService.findById(customUser.getId());

            refreshTokenService.createToken(user, refreshToken);

            return new TokenResponseDto(accessToken, refreshToken);
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    @Override
    public TokenResponseDto refresh(String refreshToken) {
        try {
            if (!jwtService.isRefreshToken(refreshToken)) {
                throw new InvalidRefreshTokenException("Token is not a refresh token");
            }

            String username = jwtService.extractUsername(refreshToken);
            CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);

            if (!jwtService.isTokenValid(refreshToken, userDetails)) {
                throw new InvalidRefreshTokenException("Refresh token is invalid");
            }

            String newRefresh = jwtService.generateRefreshToken(userDetails);
            refreshTokenService.rotateToken(refreshToken, newRefresh);
            String newAccess = jwtService.generateAccessToken(userDetails);

            return new TokenResponseDto(newAccess, newRefresh);

        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }
    }

    @Override
    public void logout(String refreshToken) {
        String username = jwtService.extractUsername(refreshToken);
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);

        if (!jwtService.isRefreshToken(refreshToken) || !jwtService.isTokenValid(refreshToken, userDetails)) {
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        refreshTokenService.revokeRefreshToken(refreshToken);
    }
}
