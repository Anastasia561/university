package pl.edu.backend.auth.refreshtoken.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.backend.auth.refreshtoken.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);
}
