package com.webit.webit.repository;

import com.webit.webit.model.PasswordResetToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends MongoRepository<PasswordResetToken, String> {

    Optional<PasswordResetToken> findByEmailAndVerificationCodeAndVerifiedFalseAndUsedFalseAndExpiresAtAfter(
            String email,
            String verificationCode,
            LocalDateTime currentTime
    );

    Optional<PasswordResetToken> findByEmailAndVerifiedTrueAndUsedFalseAndExpiresAtAfter(
            String email,
            LocalDateTime currentTime
    );

    void deleteByEmail(String email);
}