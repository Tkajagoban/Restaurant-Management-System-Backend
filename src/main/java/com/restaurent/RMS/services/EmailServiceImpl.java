package com.restaurent.RMS.services;

import com.restaurent.RMS.config.EmailConfig;
import com.restaurent.RMS.dtos.request.EmailRequestDto;
import com.restaurent.RMS.dtos.response.EmailResponseDto;
import com.restaurent.RMS.entities.Email;
import com.restaurent.RMS.exceptionHandlers.AlreadyExistException;
import com.restaurent.RMS.exceptionHandlers.ResourceNotFoundException;
import com.restaurent.RMS.mappers.EmailMapper;
import com.restaurent.RMS.repositories.EmailRepository;
import com.restaurent.RMS.utils.PasswordGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private EmailMapper emailMapper;

   // private final JavaMailSender mailSender;
    @Autowired
    private EmailConfig emailConfig;



    @Override
    public EmailResponseDto createEmail(EmailRequestDto dto) {


        if (emailRepository.count()>0){
            throw new AlreadyExistException("Email configuration already exists. You cannot add more than one.");
        }
        if (emailRepository.existsBySentEmail(dto.getSentEmail())) {
            throw new AlreadyExistException("Email configuration already exists");
        }


        Email email = emailMapper.toEntity(dto);
        Email savedEmail = emailRepository.save(email);


        return emailMapper.toResponseDto(savedEmail);
    }

    @Override
    public List<EmailResponseDto> getAllEmail() {

        List<Email> emails = emailRepository.findAll();

        if (emails.isEmpty()) {
            throw new ResourceNotFoundException("No emails found");
        }

        return emails.stream()
                .map(emailMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public EmailResponseDto updateEmail(Long id, EmailRequestDto emailRequestDto) {
        Email ExitingEmail = emailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id not found"));

        if (emailRequestDto.getSentEmail() != null &&
                !emailRequestDto.getSentEmail().equals(ExitingEmail.getSentEmail())) {

            emailRepository.findBySentEmail(emailRequestDto.getSentEmail())
                    .filter(email -> !email.getId().equals(id))
                    .ifPresent(e -> {
                        throw new AlreadyExistException("email already exit");
                    });
        }

        //hostName uniqueness check
        if (emailRequestDto.getHostName() != null &&
                !emailRequestDto.getHostName().equals(ExitingEmail.getHostName())) {

            emailRepository.findByHostName(emailRequestDto.getHostName())
                    .filter(email -> !email.getId().equals(ExitingEmail.getId()))
                    .ifPresent(e -> {
                        throw new AlreadyExistException("hostname already exits");
                    });
        }


        //MapStruct updates fields
        emailMapper.updateEntityFromDto(emailRequestDto, ExitingEmail);

        //Password update rule
        if (emailRequestDto.getPassword() == null ||
                emailRequestDto.getPassword().isBlank()) {
            ExitingEmail.setPassword(ExitingEmail.getPassword());
        }

        Email updatedEmail = emailRepository.save(ExitingEmail);
        return emailMapper.toResponseDto(updatedEmail);


    }

    @Override
    public void sendUserCredentialEmail(String toEmail, String rawPassword) {


        JavaMailSender mailSender = emailConfig.getJavaMailSender();
        Email emailConfigEntity = emailConfig
                .getActiveEmailConfig()
                .orElseThrow(() -> new IllegalStateException("Email configuration not found"));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);

        if (emailConfigEntity.getCcMailAddress() != null &&
                !emailConfigEntity.getCcMailAddress().isBlank()) {
            message.setCc(emailConfigEntity.getCcMailAddress());
        }
        message.setSubject("Your Account Login Credentials");

        String body = "Hello,\n\n"
                + "Your account has been created successfully.\n\n"
                + "Username: " + toEmail + "\n"
                + "Password: " + rawPassword + "\n\n"
                + "Please change your password after your first login.\n\n"
                + "Regards,\n"
                + "Restaurant Management System";

        message.setText(body);

        mailSender.send(message);
    }

}
