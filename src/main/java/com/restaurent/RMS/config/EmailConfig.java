package com.restaurent.RMS.config;


import com.restaurent.RMS.entities.Email;
import com.restaurent.RMS.repositories.EmailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Optional;
import java.util.Properties;

@Configuration
@Slf4j
public class EmailConfig {
    @Autowired
    private EmailRepository emailRepository;


    public JavaMailSender getJavaMailSender() {

        Optional<Email> optionalEmail =
                emailRepository.findFirstByOrderByIdAsc();

        if (optionalEmail.isEmpty()) {
            log.warn("Email config not found. Mail disabled.");
            return new JavaMailSenderImpl();
        }
        Email email = optionalEmail.get();

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(email.getHostName());

        Long portLong  = email.getPort();
        mailSender.setPort(portLong.intValue());
        mailSender.setUsername(email.getSentEmail());
        mailSender.setPassword(email.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol",
                email.getProtocol() != null ? email.getProtocol() : "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", email.getHostName());

        log.info("Email loaded from DB: {}", email.getSentEmail());

        return mailSender;
    }
    public Optional<Email> getActiveEmailConfig() {
        return emailRepository.findFirstByOrderByIdAsc();
    }

}
