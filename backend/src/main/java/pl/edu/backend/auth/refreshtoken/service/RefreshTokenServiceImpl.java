package pl.edu.backend.auth.refreshtoken.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.backend.auth.refreshtoken.model.RefreshToken;
import pl.edu.backend.auth.refreshtoken.repository.RefreshTokenRepository;
import pl.edu.backend.user.model.User;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public void createToken(User user, String tokenValue) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(tokenValue);
        refreshToken.setUser(user);
        refreshTokenRepository.save(refreshToken);
    }

    @Override
    @Transactional
    public void revokeRefreshToken(String tokenValue) {
        RefreshToken token = refreshTokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new EntityNotFoundException("Token not found"));
        refreshTokenRepository.delete(token);
    }
}
