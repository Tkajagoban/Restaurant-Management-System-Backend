package com.restaurent.RMS.services;

import com.restaurent.RMS.config.EmailConfig;
import com.restaurent.RMS.entities.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.restaurent.RMS.entities.Email;


@Service
@RequiredArgsConstructor
@Slf4j
public class OptEmailServiceImpl implements OptEmailService {

   // private final JavaMailSender mailSender;
   @Autowired
   private EmailConfig emailConfig;

    private final TokenService tokenService;

    @Override
    public void sendOtpEmail(String recipientEmail, String otp) {
        JavaMailSender mailSender = emailConfig.getJavaMailSender();

        if (mailSender == null) {
            throw new IllegalStateException(
                    "Email service not configured. Contact admin");
        }

        if (!recipientEmail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email format");

        }
        Email emailConfigEntity = emailConfig
                .getActiveEmailConfig()
                .orElseThrow(() -> new IllegalStateException("Email configuration not found"));



        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);

        if (emailConfigEntity.getCcMailAddress() != null &&
                !emailConfigEntity.getCcMailAddress().isBlank()) {
            message.setCc(emailConfigEntity.getCcMailAddress());
        }
        message.setSubject("Your OTP Code");
        message.setText("Your OTP for RMS verification is: " + otp);

        try {
            mailSender.send(message);
            log.info("OTP sent successfully to: {}", recipientEmail);
        } catch (Exception e) {
            log.error("Failed to send OTP to {}: {}", recipientEmail, e.getMessage());
            throw new RuntimeException("Failed to send email. Please check configuration.");
        }

    }
}
