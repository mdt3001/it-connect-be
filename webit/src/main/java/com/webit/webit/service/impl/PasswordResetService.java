package com.webit.webit.service.impl;

import com.webit.webit.exception.AppException;
import com.webit.webit.exception.ErrorCode;
import com.webit.webit.model.PasswordResetToken;
import com.webit.webit.model.User;
import com.webit.webit.repository.PasswordResetTokenRepository;
import com.webit.webit.repository.UserRepository;
import com.webit.webit.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;

    @Value("${app.password-reset.token-expiration-minutes:15}")
    private int tokenExpirationMinutes;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(8);
    private final SecureRandom random = new SecureRandom();

    /**
     * Bước 1: Gửi mã xác thực qua email
     */
    @Transactional
    public void sendResetCode(String email) {
        // Kiểm tra user tồn tại
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Xóa các token cũ của email này
        tokenRepository.deleteByEmail(email);

        // Generate 6-digit OTP
        String verificationCode = generateSixDigitCode();

        // Tạo token mới
        PasswordResetToken token = PasswordResetToken.builder()
                .email(email)
                .verificationCode(verificationCode)
                .verified(false)
                .used(false)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(tokenExpirationMinutes))
                .build();

        tokenRepository.save(token);

        // Gửi email
        emailService.sendPasswordResetEmail(email, verificationCode);

        log.info("Password reset code sent to: {}", email);
    }

    /**
     * Bước 2: Xác thực mã OTP
     */
    @Transactional
    public void verifyResetCode(String email, String code) {
        PasswordResetToken token = tokenRepository
                .findByEmailAndVerificationCodeAndVerifiedFalseAndUsedFalseAndExpiresAtAfter(
                        email,
                        code,
                        LocalDateTime.now()
                )
                .orElseThrow(() -> new RuntimeException("Mã xác thực không hợp lệ hoặc đã hết hạn"));

        // Mark as verified
        token.setVerified(true);
        token.setVerifiedAt(LocalDateTime.now());
        tokenRepository.save(token);

        log.info("Reset code verified for email: {}", email);
    }

    /**
     * Bước 3: Reset mật khẩu mới
     */
    @Transactional
    public void resetPassword(String email, String newPassword) {
        // Kiểm tra token đã được verify
        PasswordResetToken token = tokenRepository
                .findByEmailAndVerifiedTrueAndUsedFalseAndExpiresAtAfter(
                        email,
                        LocalDateTime.now()
                )
                .orElseThrow(() -> new RuntimeException(
                        "Phiên đặt lại mật khẩu không hợp lệ. Vui lòng thử lại từ đầu."
                ));

        // Tìm user và update password
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Mark token as used
        token.setUsed(true);
        tokenRepository.save(token);

        log.info("Password reset successfully for email: {}", email);
    }

    /**
     * Generate random 6-digit code
     */
    private String generateSixDigitCode() {
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    /**
     * Resend code (nếu user chưa nhận được email)
     */
    @Transactional
    public void resendResetCode(String email) {
        // Kiểm tra user tồn tại
        if (!userRepository.existsByEmail(email)) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        // Xóa token cũ và gửi lại
        tokenRepository.deleteByEmail(email);
        sendResetCode(email);

        log.info("Reset code resent to: {}", email);
    }
}