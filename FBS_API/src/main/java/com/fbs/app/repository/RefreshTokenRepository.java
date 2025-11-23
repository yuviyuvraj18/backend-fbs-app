package com.fbs.app.repository;

import com.fbs.app.model.RefreshTokenModel;
import com.fbs.app.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenModel, Long> {
    Optional<RefreshTokenModel> findByToken(String token);
    int deleteByUser(UserModel user);
    void deleteByToken(String token);
}

