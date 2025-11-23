package com.fbs.app.service;

import com.fbs.app.model.UserModel;
import com.fbs.app.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public TokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public void revokeRefreshToken(String token) {
        // find and delete or mark it invalid
        refreshTokenRepository.findByToken(token).ifPresent(rt -> {
            refreshTokenRepository.delete(rt); // or set rt.setRevoked(true); save(rt);
        });
    }

    // Optional: revoke all refresh tokens for a user on forced logout
    public void revokeAllForUser(UserModel user) {
        refreshTokenRepository.deleteByUser(user);
    }
}
