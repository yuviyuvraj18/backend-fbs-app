package com.fbs.app.service;

import com.fbs.app.model.PasswordResetToken;
import com.fbs.app.model.UserModel;
import com.fbs.app.repository.PasswordResetTokenRepository;
import com.fbs.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@RequiredArgsConstructor

@Service
public class PasswordResetService {


    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;


    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    // 1) Send OTP to user's email
    public void sendOtp(String email) {
// ensure email exists
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not registered"));


        String otp = String.format("%06d", new Random().nextInt(999999));


        PasswordResetToken token = new PasswordResetToken();
        token.setEmail(email);
        token.setOtp(otp);
        token.setExpiryTime(LocalDateTime.now().plusMinutes(10));


        tokenRepository.save(token);


        emailService.sendOtpEmail(email, otp);
    }


    // 2) Verify OTP and reset password
    public void resetPassword(String email, String otp, String newPassword) {
        PasswordResetToken token = tokenRepository.findByEmailAndOtp(email, otp)
                .orElseThrow(() -> new RuntimeException("Invalid OTP"));


        if (token.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }


        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));


        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);


        tokenRepository.delete(token);
    }


    // Optional: cleanup expired tokens (you can invoke on startup or schedule it)
    public void cleanExpiredTokens() {
        tokenRepository.deleteAllByExpiryTimeBefore(LocalDateTime.now());
    }
}
