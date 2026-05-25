package com.restaurent.RMS.services;

import com.restaurent.RMS.dtos.response.EmailResponseDto;
import org.springframework.stereotype.Service;
import java.util.List;
import com.restaurent.RMS.dtos.request.EmailRequestDto;


@Service


public interface EmailService {
    List<EmailResponseDto> getAllEmail();
    EmailResponseDto createEmail( EmailRequestDto emailRequestDto);

    EmailResponseDto updateEmail(Long id,  EmailRequestDto emailRequestDto);
    void sendUserCredentialEmail(String toEmail, String rawPassword);
}
