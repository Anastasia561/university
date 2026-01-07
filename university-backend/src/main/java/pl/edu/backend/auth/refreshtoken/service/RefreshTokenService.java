package pl.edu.backend.auth.refreshtoken.service;

import pl.edu.backend.user.model.User;

public interface RefreshTokenService {

    void createToken(User user, String tokenValue);

    void revokeRefreshToken(String tokenValue);
}
