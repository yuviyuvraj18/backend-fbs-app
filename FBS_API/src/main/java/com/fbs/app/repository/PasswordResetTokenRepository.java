package com.fbs.app.repository;

import com.fbs.app.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByEmailAndOtp(String email, String otp);
    void deleteAllByExpiryTimeBefore(java.time.LocalDateTime time);
}
