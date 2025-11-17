package com.webit.webit.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "password_reset_tokens")
public class PasswordResetToken {

    @Id
    private String id;

    @Indexed
    private String email;

    private String verificationCode; // 6-digit OTP

    private boolean verified;

    private boolean used;

    @Indexed(expireAfterSeconds = 900) // Auto-delete after 15 minutes
    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private LocalDateTime verifiedAt;
}