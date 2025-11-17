package com.webit.webit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Request để gửi email reset password
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyResetCodeRequest {
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Mã xác thực không được để trống")
    @Size(min = 6, max = 6, message = "Mã xác thực phải có 6 ký tự")
    private String code;
}