package com.webit.webit.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendPasswordResetEmail(String toEmail, String verificationCode) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Reset Your Password - IT Connect");

            String htmlContent = buildPasswordResetEmail(verificationCode);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Password reset email sent to: {}", toEmail);

        } catch (MessagingException e) {
            log.error("Failed to send password reset email to: {}", toEmail, e);
            throw new RuntimeException("Không thể gửi email. Vui lòng thử lại sau.");
        }
    }

    private String buildPasswordResetEmail(String code) {
        String template = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); 
                              color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }
                    .content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }
                    .code-box { background: white; border: 2px dashed #667eea; 
                                padding: 20px; text-align: center; margin: 20px 0; border-radius: 8px; }
                    .code { font-size: 32px; font-weight: bold; letter-spacing: 8px; 
                            color: #667eea; font-family: 'Courier New', monospace; }
                    .footer { text-align: center; margin-top: 20px; color: #666; font-size: 12px; }
                    .warning { background: #fff3cd; border-left: 4px solid #ffc107; 
                               padding: 12px; margin: 15px 0; border-radius: 4px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🔐 Password Reset Request</h1>
                    </div>
                    <div class="content">
                        <p>Xin chào,</p>
                        <p>Bạn đã yêu cầu đặt lại mật khẩu cho tài khoản IT Connect của mình. 
                           Sử dụng mã xác thực bên dưới để tiếp tục:</p>
                        
                        <div class="code-box">
                            <div class="code">%s</div>
                        </div>
                        
                        <div class="warning">
                            <strong>⚠️ Lưu ý quan trọng:</strong>
                            <ul style="margin: 10px 0;">
                                <li>Mã này sẽ hết hạn sau <strong>15 phút</strong></li>
                                <li>Không chia sẻ mã này với bất kỳ ai</li>
                                <li>Nếu bạn không yêu cầu đặt lại mật khẩu, hãy bỏ qua email này</li>
                            </ul>
                        </div>
                        
                        <p>Trân trọng,<br><strong>IT Connect Team</strong></p>
                    </div>
                    <div class="footer">
                        <p>Email này được gửi tự động. Vui lòng không trả lời email này.</p>
                        <p>&copy; 2024 IT Connect. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """;
        return String.format(template, code);
    }
}