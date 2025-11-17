package com.webit.webit.controller;

import com.webit.webit.dto.request.ForgotPasswordRequest;
import com.webit.webit.dto.request.ResetPasswordRequest;
import com.webit.webit.dto.request.VerifyResetCodeRequest;
import com.webit.webit.dto.response.ApiResponse;
import com.webit.webit.service.impl.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/password-reset")
@RequiredArgsConstructor
@Slf4j
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    /**
     * Endpoint 1: Gửi mã xác thực qua email
     * POST /api/auth/password-reset/send-code
     */
    @PostMapping("/send-code")
    public ApiResponse<String> sendResetCode(@RequestBody @Valid ForgotPasswordRequest request) {
        passwordResetService.sendResetCode(request.getEmail());
        return ApiResponse.<String>builder()
                .message("Mã xác thực đã được gửi đến email của bạn")
                .result("Code sent successfully")
                .build();
    }

    /**
     * Endpoint 2: Xác thực mã OTP
     * POST /api/auth/password-reset/verify-code
     */
    @PostMapping("/verify-code")
    public ApiResponse<String> verifyResetCode(@RequestBody @Valid VerifyResetCodeRequest request) {
        passwordResetService.verifyResetCode(request.getEmail(), request.getCode());
        return ApiResponse.<String>builder()
                .message("Mã xác thực hợp lệ. Bạn có thể đặt lại mật khẩu")
                .result("Code verified successfully")
                .build();
    }

    /**
     * Endpoint 3: Đặt lại mật khẩu mới
     * POST /api/auth/password-reset/reset
     */
    @PostMapping("/reset")
    public ApiResponse<String> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        passwordResetService.resetPassword(request.getEmail(), request.getNewPassword());
        return ApiResponse.<String>builder()
                .message("Đặt lại mật khẩu thành công. Vui lòng đăng nhập lại")
                .result("Password reset successfully")
                .build();
    }

    /**
     * Endpoint 4: Gửi lại mã (nếu user không nhận được email)
     * POST /api/auth/password-reset/resend-code
     */
    @PostMapping("/resend-code")
    public ApiResponse<String> resendResetCode(@RequestBody @Valid ForgotPasswordRequest request) {
        passwordResetService.resendResetCode(request.getEmail());
        return ApiResponse.<String>builder()
                .message("Mã xác thực mới đã được gửi lại")
                .result("Code resent successfully")
                .build();
    }
}