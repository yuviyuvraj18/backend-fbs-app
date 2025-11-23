package com.fbs.app.service;

import com.fbs.app.model.RefreshTokenModel;
import com.fbs.app.model.UserModel;
import com.fbs.app.repository.RefreshTokenRepository;
import com.fbs.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${jwt.refresh-expiration-ms}")
    private Long refreshDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                               UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public RefreshTokenModel createRefreshToken(Long userId) {
        UserModel user = (UserModel) userRepository.findById(userId).orElseThrow();

        RefreshTokenModel refreshToken = new RefreshTokenModel();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString()); // opaque token
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public Optional<RefreshTokenModel> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshTokenModel verifyExpiration(RefreshTokenModel token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token was expired. Please login again.");
        }
        return token;
    }

    public int deleteByUserId(Long userId) {
        UserModel user = (UserModel) userRepository.findById(userId).orElseThrow();
        return refreshTokenRepository.deleteByUser(user);
    }
}
